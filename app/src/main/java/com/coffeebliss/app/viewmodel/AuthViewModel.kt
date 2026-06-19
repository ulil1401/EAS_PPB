package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import com.coffeebliss.app.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registeredMember: Member? = null,
    val loggedInMember: Member? = null
)

class AuthViewModel(
    private val repository: CoffeeBlissRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, errorMessage = null) }
    }

    fun register(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.registerMember(
                name = _uiState.value.name,
                email = _uiState.value.email,
                phone = _uiState.value.phone
            ).onSuccess { member ->
                sessionManager.loggedInMemberId = member.id
                _uiState.update {
                    it.copy(isLoading = false, registeredMember = member, loggedInMember = member)
                }
                onSuccess(member.id)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.message)
                }
            }
        }
    }

    fun login(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.login(
                email = _uiState.value.email,
                phone = _uiState.value.phone
            ).onSuccess { member ->
                sessionManager.loggedInMemberId = member.id
                _uiState.update {
                    it.copy(isLoading = false, loggedInMember = member)
                }
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
