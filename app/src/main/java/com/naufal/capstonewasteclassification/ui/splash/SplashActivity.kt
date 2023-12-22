package com.naufal.capstonewasteclassification.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.auth.register.RegisterActivity
import com.naufal.capstonewasteclassification.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}