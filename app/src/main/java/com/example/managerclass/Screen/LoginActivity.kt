package com.example.managerclass.Screen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.managerclass.Model.User
import com.example.managerclass.SQLite.userDao
import com.example.managerclass.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var userdao: userDao? = null
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE)
        binding.btnLogin.setOnClickListener {
            onClickLogin()
        }
        binding.txtUsername.setText(sharedPreferences?.getString("taikhoan", ""))
        binding.txtPassword.setText(sharedPreferences?.getString("matkhau", ""))
        binding.checkBox.isChecked = sharedPreferences?.getBoolean("checked", false)!!
    }

    private fun onClickLogin() {
        val username = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()

        if (username.isEmpty()) {//check empty
            binding.txtUsername.error = "Tài khoản không được để trống"
            return
        } else {
            binding.txtUsername.error = null
        }
        if (password.isEmpty()) {
            binding.txtPassword.error = "Mật khẩu không được để trống"
            return
        } else {
            binding.txtPassword.error = null
        }
        val user = User(username, password)
        userdao = userDao(this)
        var user1 = User();
        var check: Boolean = userdao!!.checkUser(user)
        if (check) {//check user
            user1 = userdao!!.getUser(user)
            if (user1.getPassWorld().equals(user.getPassWorld())) {
                val intent = Intent(this, ClassActivity::class.java)
                intent.putExtra("user", user1)
                intent.putExtra("idUser", user1.getId())
                if (binding.checkBox.isChecked) {
                    sharedPreferences?.edit()?.putString("taikhoan", username)
                        ?.putString("matkhau", password)?.putBoolean("checked", true)?.apply()
                } else {
                    sharedPreferences?.edit()?.putString("taikhoan", "")?.putString("matkhau", "")
                        ?.putBoolean("checked", false)?.apply()
                }
                startActivity(intent)
                finish()
            } else {
                binding.txtPassword.error = "Mật khẩu không đúng"
            }
        } else {
            binding.txtUsername.error = "Tài khoản không đúng"
        }

    }

    fun onClickBack(view: View) {
        startActivity(Intent(this, SplashScreenActivity::class.java))
    }
}