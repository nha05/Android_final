package com.example.managerclass.Screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.managerclass.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            onLoginButtonClick()
        }
        binding.registerButton.setOnClickListener {
            onRegisterButtonClick()
        }
    }

    private fun onLoginButtonClick() {
        //chuyển sang màn hình login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onRegisterButtonClick() {
        //chuyển sang màn hình register
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Ấn quay lại lần nữa để thoát chương trình", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}