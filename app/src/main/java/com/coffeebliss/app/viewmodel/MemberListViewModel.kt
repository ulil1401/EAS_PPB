package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MemberListViewModel(repository: CoffeeBlissRepository) : ViewModel() {

    val members: StateFlow<List<Member>> = repository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val memberCount: StateFlow<Int> = repository.getMemberCount()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
}
