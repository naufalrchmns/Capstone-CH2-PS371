package com.naufal.capstonewasteclassification.api

import com.naufal.capstonewasteclassification.pref.UserRepository

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}