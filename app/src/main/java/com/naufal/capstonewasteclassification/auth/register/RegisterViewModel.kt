package com.naufal.capstonewasteclassification.auth.register

import androidx.lifecycle.ViewModel
import com.naufal.capstonewasteclassification.pref.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {

    fun registerUser(name: String, email: String, password:String) = userRepository.register(name, email, password)

}