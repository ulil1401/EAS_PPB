package com.coffeebliss.app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import com.coffeebliss.app.util.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val photoPath: String? = null,
    val pendingPhotoUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

class EditProfileViewModel(
    private val repository: CoffeeBlissRepository,
    private val memberId: Long,
    private val savePhoto: suspend (Uri) -> Result<String>
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMember(memberId).first()?.let { member ->
                _uiState.update {
                    it.copy(
                        name = member.name,
                        email = member.email,
                        phone = member.phone,
                        photoPath = member.photoPath
                    )
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.update { it.copy(pendingPhotoUri = uri, errorMessage = null) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSaved = false) }

            var photoPath = _uiState.value.photoPath
            _uiState.value.pendingPhotoUri?.let { uri ->
                savePhoto(uri).onSuccess { path ->
                    if (photoPath != null && photoPath != path) {
                        ImageStorage.deletePhoto(photoPath)
                    }
                    photoPath = path
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                    return@launch
                }
            }

            repository.updateMember(
                memberId = memberId,
                name = _uiState.value.name,
                email = _uiState.value.email,
                phone = _uiState.value.phone,
                photoPath = photoPath
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true,
                        photoPath = photoPath,
                        pendingPhotoUri = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
