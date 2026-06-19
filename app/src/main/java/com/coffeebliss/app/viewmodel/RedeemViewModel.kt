package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val message: String? = null,
    val isError: Boolean = false
)

class RedeemViewModel(
    private val repository: CoffeeBlissRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemUiState())
    val uiState: StateFlow<RedeemUiState> = _uiState.asStateFlow()

    fun redeem(memberId: Long, reward: Reward) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            repository.redeemReward(memberId, reward)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = "${reward.name} berhasil ditukar!",
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
