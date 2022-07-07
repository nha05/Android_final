package com.example.managerclass.Screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.managerclass.Model.sinhVien
import com.example.managerclass.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var student: sinhVien? = null
    private var tenLop = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        student = intent.getSerializableExtra("student") as sinhVien
        tenLop = intent.getStringExtra("tenlop").toString()

        binding.editTextName.text = (student?.getTenSV() ?: return).toEditable()
        binding.editTextMssv.text = (student?.getMaSV() ?: return).toEditable()
        binding.editTextEmail.text = (student?.getEmail() ?: return).toEditable()
        binding.editTextPhone.text = (student?.getSdt() ?: return).toEditable()
        binding.editTextNganh.text = (student?.getNganh() ?: return).toEditable()
        binding.editTextClass.text = (tenLop).toEditable()
        binding.profileImage.setImageBitmap(
            convertCompressedByteArrayToBitmap(
                student?.getHinh() ?: return
            )
        )
    }

    //Convert Byte To BitMap
    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap? {//chuyển byteArray thành bitmap
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

    fun onClickBack() {
        finish()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}