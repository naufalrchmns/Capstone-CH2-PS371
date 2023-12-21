package com.naufal.capstonewasteclassification.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.auth.register.RegisterActivity
import com.naufal.capstonewasteclassification.databinding.ActivityMainBinding
import com.naufal.capstonewasteclassification.pref.UserPreference
import com.naufal.capstonewasteclassification.pref.dataStore

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        btn = findViewById(R.id.button2)

        btn.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        // Hide the ActionBar
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top-level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_scan, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
