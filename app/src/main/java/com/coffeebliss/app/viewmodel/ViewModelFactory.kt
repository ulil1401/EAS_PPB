package com.coffeebliss.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coffeebliss.app.CoffeeBlissApplication
import com.coffeebliss.app.util.ImageStorage

class ViewModelFactory(
    private val application: CoffeeBlissApplication
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = application.repository
        return when {
            modelClass.isAssignableFrom(MemberListViewModel::class.java) ->
                MemberListViewModel(repository) as T
            modelClass.isAssignableFrom(AddMemberViewModel::class.java) ->
                AddMemberViewModel(repository) as T
            modelClass.isAssignableFrom(RedeemViewModel::class.java) ->
                RedeemViewModel(repository) as T
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) ->
                throw IllegalArgumentException("EditProfileViewModel requires memberId")
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

class MemberDetailViewModelFactory(
    private val application: CoffeeBlissApplication,
    private val memberId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MemberDetailViewModel::class.java) ->
                MemberDetailViewModel(application.repository, memberId) as T
            modelClass.isAssignableFrom(TransactionViewModel::class.java) ->
                TransactionViewModel(application.repository, memberId) as T
            modelClass.isAssignableFrom(RedeemViewModel::class.java) ->
                RedeemViewModel(application.repository) as T
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) ->
                EditProfileViewModel(
                    repository = application.repository,
                    memberId = memberId,
                    savePhoto = { uri ->
                        ImageStorage.saveProfilePhoto(application, memberId, uri)
                    }
                ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}
