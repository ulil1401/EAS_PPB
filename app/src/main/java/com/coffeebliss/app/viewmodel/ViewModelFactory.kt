package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coffeebliss.app.CoffeeBlissApplication
import com.coffeebliss.app.util.SessionManager

class ViewModelFactory(
    private val application: CoffeeBlissApplication
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = application.repository
        val sessionManager = application.sessionManager

        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repository, sessionManager) as T
            modelClass.isAssignableFrom(RedeemViewModel::class.java) ->
                RedeemViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                throw IllegalArgumentException("HomeViewModel requires memberId")
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }

    fun createHomeViewModel(memberId: Long): HomeViewModel {
        return HomeViewModel(application.repository, memberId)
    }
}

class HomeViewModelFactory(
    private val application: CoffeeBlissApplication,
    private val memberId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application.repository, memberId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
