package com.example.managerclass.Screen

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managerclass.Adapter.ClassAdapter
import com.example.managerclass.Model.lopHoc
import com.example.managerclass.R
import com.example.managerclass.SQLite.lopHocDao
import com.example.managerclass.databinding.ActivityClassBinding

class ClassActivity : AppCompatActivity() {
    private var classList: ArrayList<lopHoc> = ArrayList()
    private lateinit var binding: ActivityClassBinding
    private var lopHocDao: lopHocDao? = null
    private var idUser: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lopHocDao = lopHocDao(this)

        idUser = intent.getIntExtra("idUser", 0)
        getData()
        binding.btnManagerAdd.setOnClickListener { view ->
            onClickAdd()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isNotEmpty()) {
                    classList.clear()
                    classList.addAll(lopHocDao!!.searchLopHoc(charSequence.toString(), idUser!!))
                    binding.rvClass.adapter = ClassAdapter(classList, this@ClassActivity)
                } else {
                    getData()
                }

            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    fun onClickAdd(
        key: Int? = null,
        id: Int? = null,
        name: String? = null,
        ma: String? = null
    ) {//key = null thi la them moi, key != null thi la sua
        val dialog = Dialog(this)//tao dialog
        dialog.setContentView(R.layout.dialog_custom)//set layout cho dialog
        dialog.setCanceledOnTouchOutside(false)//khong cho dialog bi an ngoai
        val title = dialog.findViewById<View>(R.id.textView3) as TextView
        val tenlop = dialog.findViewById<View>(R.id.edttenlop) as EditText
        val malop = dialog.findViewById<View>(R.id.edtmalophoc) as EditText
        val btnthem = dialog.findViewById<View>(R.id.luulh) as Button
        if (key != null) {//neu key != null thi la sua
            title.text = "Sửa lớp học"
            tenlop.setText(name)
            malop.setText(ma)
            btnthem.text = "Sửa"
        }
        val btnhuy = dialog.findViewById<View>(R.id.huylh) as Button
        btnthem.setOnClickListener {//khi nhan vao nut them
            val ten = tenlop.text.toString().trim { it <= ' ' }
            val ma = malop.text.toString().trim { it <= ' ' }
            val check: Boolean = lopHocDao?.getLopHocByMaLop(ma) ?: return@setOnClickListener
            if (ten == "" && ma == "") {
                tenlop.error = "không được bỏ trống!"
                malop.error = "Không được bỏ trống!"
                tenlop.requestFocus()
            } else if (ten == "") {
                tenlop.error = "không được bỏ trống!"
                tenlop.requestFocus()
            } else if (ma == "") {
                malop.error = "Không được bỏ trống!"
                malop.requestFocus()
            } else if (check) {
                malop.error = "Mã lớp học đã tồn tại!"
                malop.requestFocus()
            } else {
                //switch case
                when (key) {//neu key != null thi la sua
                    null -> {
                        val lh = lopHoc(ten, ma, idUser)//tao lop hoc
                        lopHocDao?.insertLopHoc(lh)//them lop hoc vao sqlite
                    }
                    else -> {//neu key != null thi la sua
                        val lh = lopHoc(id, ten, ma, idUser)//tao lop hoc
                        lopHocDao?.updateLopHoc(lh)//them lop hoc vao sqlite
                    }
                }
                getData()
                dialog.dismiss()
            }
        }
        btnhuy.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun getData() {//lay du lieu tu sqlite
        //gọi hàm getAllLopHoc trong lopHocDao
        classList = lopHocDao?.getAllLopHoc(idUser!!)!!//lay danh sach lop hoc
        if (classList.size < 0) {//neu danh sach lop hoc rong
            Toast.makeText(this, "Không có lớp học nào", Toast.LENGTH_LONG).show()//thong bao
            return
        }
        //setlayout cho recycler view
        val recyclerView = binding.rvClass//lay recycler view
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)//set layout cho recycler view
        binding.rvClass.adapter = ClassAdapter(classList, this)//set adapter cho recycler view
    }


    var doubleBackToExitPressedOnce = false

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