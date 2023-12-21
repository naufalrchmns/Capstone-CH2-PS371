package com.naufal.capstonewasteclassification.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.naufal.capstonewasteclassification.pref.Result
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.auth.login.LoginActivity
import com.naufal.capstonewasteclassification.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        loginButton()
    }
    private fun setupView() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.editTextPassword.text.toString()
            val result = viewModel.registerUser(name, email, password)
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
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
                            Toast.makeText(this, "Account berhasil dibuat", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                if (name.isEmpty()) binding.nameEditText.error = "Nama tidak bisa kosong"
                if (email.isEmpty()) binding.emailEditText.error = "Email tidak bisa kosong"
                if (password.isEmpty()) binding.editTextPassword.error = "Password harus 8 karakter atau lebih"
            }
        }
    }
    private fun loginButton(){
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}