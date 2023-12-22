package com.naufal.capstonewasteclassification.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.naufal.capstonewasteclassification.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val userPreference: UserPreference
) : ViewModel() {

    fun checkTokenAvailable(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreference.deleteToken()
        }
    }
}
