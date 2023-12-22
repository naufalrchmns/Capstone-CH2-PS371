package com.naufal.capstonewasteclassification.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.databinding.ActivityLoginBinding
import com.naufal.capstonewasteclassification.pref.UserPreference
import com.naufal.capstonewasteclassification.pref.dataStore
import com.naufal.capstonewasteclassification.pref.Result
import com.naufal.capstonewasteclassification.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }
    private fun setupView() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.editTextPassword.text.toString()
            val result = viewModel.login(email, password)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                result.observe(this) {
                    when (it) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val data = it.data
                            viewModel.saveToken(data.loginResult.token)
                            Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                            Log.d("LoginActivity", "Token: ${data.loginResult.token}")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                }
            } else {
                if (email.isEmpty()) binding.emailEditText.error = "Email harus diisi"
                if (password.isEmpty()) binding.editTextPassword.error = "Password harus terdiri dari 8 karakter atau lebih"
            }
        }
    }
}