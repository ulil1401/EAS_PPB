package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddMemberUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AddMemberViewModel(
    private val repository: CoffeeBlissRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMemberUiState())
    val uiState: StateFlow<AddMemberUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun saveMember(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.addMember(
                name = _uiState.value.name,
                email = _uiState.value.email,
                phone = _uiState.value.phone
            ).onSuccess { member ->
                _uiState.update { it.copy(isLoading = false) }
                onSuccess(member.id)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
