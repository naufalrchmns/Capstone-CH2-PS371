package com.naufal.capstonewasteclassification.pref

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.naufal.capstonewasteclassification.api.ApiService
import com.naufal.capstonewasteclassification.response.Login
import com.naufal.capstonewasteclassification.response.Register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val api: ApiService
) {
    private val loginResult = MediatorLiveData<Result<Login>>()
    private val registerResult = MediatorLiveData<Result<Register>>()

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<Register>> {
        registerResult.value = Result.Loading
        val client = api.register(
            name,
            email,
            password
        )
        client.enqueue(object : Callback<Register> {
            override fun onResponse(
                call: Call<Register>,
                response: Response<Register>
            ) {
                if (response.isSuccessful) {
                    val registerInfo = response.body()
                    if (registerInfo != null) {
                        registerResult.value = Result.Success(registerInfo)
                    } else {
                        registerResult.value = Result.Error(REGISTER_ERROR_MESSAGE)
                        Log.e(TAG, "Failed: Register Info is null")
                    }
                } else {
                    registerResult.value = Result.Error(REGISTER_ERROR_MESSAGE)
                    Log.e(TAG, "Failed: Response Unsuccessful - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                registerResult.value = Result.Error(REGISTER_ERROR_MESSAGE)
                Log.e(TAG, "Failed: Response Failure - ${t.message.toString()}")
            }

        })

        return registerResult
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<Login>> {
        loginResult.value = Result.Loading
        val client = api.login(
            email,
            password
        )
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    val loginInfo = response.body()
                    if (loginInfo != null) {
                        loginResult.value = Result.Success(loginInfo)
                    } else {
                        loginResult.value = Result.Error(LOGIN_ERROR_MESSAGE)
                        Log.e(TAG, "Failed: Login Info is null")
                    }
                } else {
                    loginResult.value = Result.Error(LOGIN_ERROR_MESSAGE)
                    Log.e(TAG, "Failed: Response Unsuccessful - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                loginResult.value = Result.Error(LOGIN_ERROR_MESSAGE)
                Log.e(TAG, "Failed: Response Failure - ${t.message.toString()}")
            }
        })

        return loginResult
    }


    companion object {
        private val TAG = UserRepository::class.java.simpleName
        private const val LOGIN_ERROR_MESSAGE = "Login failed, please try again."
        private const val REGISTER_ERROR_MESSAGE = "Register failed, please try again."
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}

