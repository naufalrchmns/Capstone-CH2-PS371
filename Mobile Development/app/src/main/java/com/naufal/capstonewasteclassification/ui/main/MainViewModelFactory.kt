package com.naufal.capstonewasteclassification.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.naufal.capstonewasteclassification.pref.UserPreference

class MainViewModelFactory private constructor(
    private val userPreference: UserPreference
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userPreference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object Factory {
        @Volatile
        private var instance: MainViewModelFactory? = null

        fun getInstance(
            context: Context,
            userPreference: UserPreference
        ): MainViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(
                    userPreference
                ).also { instance = it }
            }
    }
}