package com.naufal.capstonewasteclassification.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.capstonewasteclassification.pref.UserPreference
import com.naufal.capstonewasteclassification.pref.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val userPreference: UserPreference
) : ViewModel(){
    fun login(email: String, password: String) = repository.login(email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreference.saveToken(token)
        }
    }
}