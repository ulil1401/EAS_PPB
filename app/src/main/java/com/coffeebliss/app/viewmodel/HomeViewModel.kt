package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val amountInput: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false
)

class HomeViewModel(
    private val repository: CoffeeBlissRepository,
    memberId: Long
) : ViewModel() {

    val member: StateFlow<Member?> = repository.getMember(memberId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions: StateFlow<List<Transaction>> = repository.getTransactions(memberId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val redemptions: StateFlow<List<Redemption>> = repository.getRedemptions(memberId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onAmountChange(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(amountInput = filtered, message = null) }
    }

    fun addTransaction(memberId: Long) {
        val amount = _uiState.value.amountInput.toLongOrNull()
        if (amount == null || amount <= 0) {
            _uiState.update {
                it.copy(message = "Masukkan nominal transaksi yang valid", isError = true)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.addTransaction(memberId, amount)
                .onSuccess { transaction ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            amountInput = "",
                            message = "Transaksi berhasil! +${transaction.pointsEarned} poin",
                            isError = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = error.message,
                            isError = true
                        )
                    }
                }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
