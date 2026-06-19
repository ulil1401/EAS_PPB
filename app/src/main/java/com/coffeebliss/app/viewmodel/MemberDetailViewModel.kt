package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MemberDetailViewModel(
    repository: CoffeeBlissRepository,
    memberId: Long
) : ViewModel() {

    val member: StateFlow<Member?> = repository.getMember(memberId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val transactions: StateFlow<List<Transaction>> = repository.getTransactions(memberId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val redemptions: StateFlow<List<Redemption>> = repository.getRedemptions(memberId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
