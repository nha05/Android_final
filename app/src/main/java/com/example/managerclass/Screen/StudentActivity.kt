package com.example.managerclass.Screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.managerclass.Adapter.StudentAdapter
import com.example.managerclass.Model.sinhVien
import com.example.managerclass.R
import com.example.managerclass.SQLite.sinhVienDao
import com.example.managerclass.databinding.ActivityStudentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.IOException


class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding
    private var sinhvienDao: sinhVienDao? = null
    private var listSv: ArrayList<sinhVien>? = null
    private var sv: sinhVien? = null
    private var img: android.widget.ImageView? = null
    private var id: Int? = null
    private var idUser: String? = null
    private var checked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sinhvienDao = sinhVienDao(this)
        listSv = ArrayList<sinhVien>()

        sv = sinhVien()
        id = intent.getIntExtra("malop", 0)
        idUser = intent.getStringExtra("idUser")
        binding.btnManagerAdd.setOnClickListener {
            onClickAdd()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isNotEmpty()) {
                    listSv!!.clear()
                    listSv!!.addAll(sinhvienDao!!.search(charSequence.toString(), id!!))
                    binding.rvStudent.adapter = StudentAdapter(listSv, this@StudentActivity)
                } else {
                    getData()
                }

            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    fun onClickAdd(
        key: Int? = null,
        sv2: sinhVien? = null,
        idupdate: Int? = null
    ) {//key = 1 : update, key = 0 : add
        val dialog = BottomSheetDialog(this@StudentActivity)
        dialog.setContentView(R.layout.custom_themsv)
        val ten = dialog.findViewById<View>(R.id.edttensv) as EditText?
        val ma = dialog.findViewById<View>(R.id.edtmasv) as EditText?
        val nganh = dialog.findViewById<View>(R.id.edtnganhsv) as EditText?
        val email = dialog.findViewById<View>(R.id.edtemailsv) as EditText?
        val sdt = dialog.findViewById<View>(R.id.edtsdtsv) as EditText?
        val them = dialog.findViewById<View>(R.id.themsv) as Button?
        val huy = dialog.findViewById<View>(R.id.huysv) as Button?
        img = dialog.findViewById<View>(R.id.img2) as ImageView?
        val imgcam = dialog.findViewById<View>(R.id.cam) as ImageButton?
        val imgfol = dialog.findViewById<View>(R.id.folde) as ImageButton?
        if (key != null) {//update
            ten?.setText(sv2?.getTenSV())
            ma?.setText(sv2?.getMaSV())
            nganh?.setText(sv2?.getNganh())
            email?.setText(sv2?.getEmail())
            sdt?.setText(sv2?.getSdt())
            val check: Boolean = sv2?.getHinh() != null
            if (check) {
                img?.setImageBitmap(convertCompressedByteArrayToBitmap(sv2?.getHinh()!!))
            } else {
                img?.setImageResource(R.drawable.img)
            }

        }
        imgcam?.setOnClickListener {
            checked = true
            onClickCamera()

        }
        imgfol?.setOnClickListener {
            checked = true
            onClickFol()
        }
        them?.setOnClickListener {
            var masv = ma?.text.toString()
            if (ten?.text.toString().isEmpty() || nganh?.text.toString()
                    .isEmpty() || email?.text.toString().isEmpty() || sdt?.text.toString()
                    .isEmpty() || img == null || ma?.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this@StudentActivity,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                sv?.setTenSV(ten?.text.toString())
                sv?.setMaSV(ma?.text.toString())
                sv?.setNganh(nganh?.text.toString())
                sv?.setEmail(email?.text.toString())
                sv?.setSdt(sdt?.text.toString())
                sv?.setIdLop(id!!)
                when (key) {//key = 1 : update, key = 0 : add
                    null -> {
                        if (checkSv(masv)) {
                            Toast.makeText(
                                this@StudentActivity,
                                "Mã sinh viên đã tồn tại",
                                Toast.LENGTH_LONG
                            ).show()
                            return@setOnClickListener
                        }
                        sinhvienDao?.insert(sv!!)//add
                        Toast.makeText(
                            this@StudentActivity,
                            "Thêm thành công",
                            Toast.LENGTH_LONG
                        ).show()
                        sv = sinhVien()
                        listSv = sinhvienDao?.getAllSinhVien(id!!)
                    }
                    else -> {
                        if (!checked) {
                            sv?.setHinh(sv2!!.getHinh())
                        }
                        sinhvienDao?.update(sv!!, idupdate)
                        Toast.makeText(
                            this@StudentActivity,
                            "Sửa thành công",
                            Toast.LENGTH_LONG
                        ).show()
                        checked = false
                        sv = sinhVien()
                        listSv = sinhvienDao?.getAllSinhVien(id!!)
                    }
                }
                dialog.dismiss()
                binding.rvStudent.adapter = StudentAdapter(listSv!!, this@StudentActivity)
            }
        }
        huy?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onClickFol() {
        Permisson()
    }

    private fun onClickCamera() {
        PermissonCamera()
    }

    fun onClickBack(view: View) {
        finish()
    }

    fun checkSv(maSv: String): Boolean {
        var check = false
        var listSV = sinhvienDao?.getAllSinhVien(id!!)
        for (i in 0 until listSV!!.size) {
            if (listSV!![i].getMaSV() == maSv) {
                check = true
            }
        }
        return check
    }

    //xin quyền truy cập thư mục
    private fun Permisson() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)//xin quyền truy cập thư mục
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val intent =
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )//chọn ảnh từ thư mục
                    intent.type = "image/*"//chọn kiểu file
                    startActivityForResult(intent, 999)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(this@StudentActivity, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                    checked = false
                    return
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    //xin quyền truy cập camera
    private fun PermissonCamera() {//xin quyền truy cập camera
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)//xin quyền truy cập camera
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//xin quyền truy cập camera
                    try {
                        activityResultLaunch.launch(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(this@StudentActivity, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                    checked = false
                    return
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun getData() {//lay du lieu tu sqlite
        //gọi hàm getAllLopHoc trong lopHocDao
        listSv = sinhvienDao?.getAllSinhVien(id!!)!!//lay danh sach lop hoc
        if (listSv!!.size < 0) {//neu danh sach lop hoc rong
            Toast.makeText(this, "Không có lớp học nào", Toast.LENGTH_LONG).show()//thong bao
            return
        }
        //setlayout cho recycler view
        val recyclerView = binding.rvStudent//lay recycler view
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)//set layout cho recycler view
        binding.rvStudent.adapter = StudentAdapter(listSv, this)//set adapter cho recycler view
    }

    var activityResultLaunch = registerForActivityResult(
        StartActivityForResult(),//xử lý kết quả trả về từ camera và thư mục
        ActivityResultCallback { result ->//xử lý kết quả trả về từ camera và thư mục
            //lấy ảnh từ camera
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data!!.extras?.get("data") as Bitmap
                img?.setImageBitmap(bitmap)
                sv!!.setHinh(imageViewToByte(bitmap)!!)
            } else {

            }
        })


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity.RESULT_OK && data != null) {
            //đo kích thước ảnh nếu lớn hơn thì resize
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
            val bitmapResize = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
            img?.setImageBitmap(bitmapResize)
            sv!!.setHinh(imageViewToByte(bitmapResize)!!)
        } else {

        }
    }


    //Convert Bitmap To Byte
    fun imageViewToByte(image: Bitmap): ByteArray? {//convert image to byte
        val os = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, os)
        val byteArray = os.toByteArray()
        return byteArray
    }

    //Convert uri to bitmap
    fun getBitmapFromUri(uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    //Convert Byte To BitMap
    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap? {//convert byte to bitmap
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

    //get data from database
    override fun onStart() {//get data from database
        super.onStart()
        listSv = sinhvienDao?.getAllSinhVien(id!!)!!//get data from database
        if (listSv!!.size == 0) {
            return
        }
        binding.rvStudent.adapter = StudentAdapter(listSv!!, this@StudentActivity)
    }
}