package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.TransactionResult
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import com.coffeebliss.app.util.PointCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUiState(
    val amountInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successResult: TransactionResult? = null
) {
    val pointsPreview: Int
        get() = PointCalculator.calculatePoints(amountInput.toLongOrNull() ?: 0L)
}

class TransactionViewModel(
    private val repository: CoffeeBlissRepository,
    private val memberId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(amountInput = value.filter { c -> c.isDigit() }, errorMessage = null) }
    }

    fun saveTransaction() {
        val amount = _uiState.value.amountInput.toLongOrNull()
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(errorMessage = "Masukkan nominal yang valid") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.addTransaction(memberId, amount)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(isLoading = false, amountInput = "", successResult = result)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun dismissSuccessDialog() {
        _uiState.update { it.copy(successResult = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
