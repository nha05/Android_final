package com.example.managerclass.Screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managerclass.Model.User
import com.example.managerclass.SQLite.userDao
import com.example.managerclass.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var userdao: userDao? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userdao = userDao(this)

        binding.btnRegister.setOnClickListener {
           onRegister()
        }
    }

    fun onClickBack(view: View) {
        startActivity(Intent(this, SplashScreenActivity::class.java))
    }
    fun onRegister() {
        val name = binding.txtUsername.text.toString()
        val password = binding.txtPassword.text.toString()
        if (name.isEmpty()) {
            binding.txtUsername.error = "Vui lòng nhập tên đăng nhập"
            return
        }else if(password.isEmpty()){
            binding.txtPassword.error = "Vui lòng nhập mật khẩu"
            return
        } else {
            binding.txtUsername.error = null
            binding.txtPassword.error = null
            binding.txtUsername.text!!.clear()
            binding.txtPassword.text!!.clear()
        }
        val user = User(name, password)
        val check: Boolean = userdao!!.insertUser(user)
        if (check) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
            //chuyển sang màn hình đăng nhập
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            binding.txtUsername.error = "Tên đăng nhập đã tồn tại"
        }
    }

}