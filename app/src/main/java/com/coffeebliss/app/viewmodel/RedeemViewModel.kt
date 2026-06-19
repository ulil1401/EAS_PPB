package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.RedeemResult
import com.coffeebliss.app.data.model.Reward
import com.coffeebliss.app.data.model.RewardCatalog
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RedeemUiState(
    val rewards: List<Reward> = RewardCatalog.rewards,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val pendingReward: Reward? = null,
    val showConfirmDialog: Boolean = false,
    val successResult: RedeemResult? = null
)

class RedeemViewModel(
    private val repository: CoffeeBlissRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemUiState())
    val uiState: StateFlow<RedeemUiState> = _uiState.asStateFlow()

    fun requestRedeem(reward: Reward) {
        _uiState.update { it.copy(pendingReward = reward, showConfirmDialog = true) }
    }

    fun dismissConfirmDialog() {
        _uiState.update { it.copy(showConfirmDialog = false, pendingReward = null) }
    }

    fun confirmRedeem(memberId: Long) {
        val reward = _uiState.value.pendingReward ?: return
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, showConfirmDialog = false, errorMessage = null)
            }
            repository.redeemReward(memberId, reward)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pendingReward = null,
                            successResult = result
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pendingReward = null,
                            errorMessage = error.message
                        )
                    }
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
