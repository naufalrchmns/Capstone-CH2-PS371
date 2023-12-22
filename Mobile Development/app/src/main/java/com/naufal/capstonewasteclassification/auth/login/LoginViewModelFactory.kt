package com.naufal.capstonewasteclassification.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naufal.capstonewasteclassification.api.Injection
import com.naufal.capstonewasteclassification.pref.UserPreference
import com.naufal.capstonewasteclassification.pref.UserRepository

class LoginViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val userPreference: UserPreference
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null

        fun getInstance(userPreference: UserPreference): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    Injection.provideRepository(),
                    userPreference
                ) }
    }
}