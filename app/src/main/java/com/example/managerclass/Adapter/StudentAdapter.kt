package com.example.managerclass.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.managerclass.Adapter.StudentAdapter.StudentViewHolder
import com.example.managerclass.Model.lopHoc
import com.example.managerclass.Model.sinhVien
import com.example.managerclass.SQLite.lopHocDao
import com.example.managerclass.SQLite.sinhVienDao
import com.example.managerclass.Screen.DetailActivity
import com.example.managerclass.Screen.StudentActivity
import com.example.managerclass.databinding.ItemSinhvienBinding

class StudentAdapter() : RecyclerView.Adapter<StudentViewHolder>() {
    var sv: ArrayList<sinhVien>? = null
    var context: StudentActivity? = null
    var lh: lopHoc? = null
    var lopDao: lopHocDao? = null


    constructor(lh: ArrayList<sinhVien>?, context: StudentActivity?) : this() {
        this.sv = lh
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemSinhvienBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        lopDao = lopHocDao(context!!)//khởi tạo đối tượng lopHocDao
        var id = sv!![position].getIdLop()//lấy id lớp
        lh = lopDao!!.getLopHocById(id)//lấy lớp theo id
        holder.binding!!.edtmssv.text = sv!![position].getMaSV()
        holder.binding!!.edttensv.text = sv!![position].getTenSV()
        holder.binding!!.edtnganh.text = sv!![position].getNganh()
        holder.binding!!.edtemail.text = sv!![position].getEmail()
        holder.binding!!.edtsdt.text = sv!![position].getSdt()
        holder.binding!!.edtmalop.text = lh!!.getMaLop()
        holder.binding!!.imageView.setImageBitmap(convertCompressedByteArrayToBitmap(sv!![position].getHinh()))

        holder.binding!!.imgbtxoa.setOnClickListener {//xóa sinh viên
            val dialogxoa = AlertDialog.Builder(context)//tạo dialog xóa
            dialogxoa.setMessage("Bạn có muốn xóa thông tin sinh viên : ${sv!![position].getTenSV()} này không?")
            dialogxoa.setPositiveButton(
                "Có"
            ) { dialog, which ->
                sinhVienDao(context!!).delete(sv!![position].getId()!!)//xóa sinh viên theo id
                setData(sinhVienDao(context!!).getAllSinhVien(id))//cập nhật lại danh sách sinh viên
                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show()
            }
            dialogxoa.setNegativeButton(
                "Không"
            ) { dialog, which -> }
            dialogxoa.show()
        }

        holder.binding!!.imgbtsua.setOnClickListener {//sửa sinh viên
            val dialogsua = AlertDialog.Builder(context)
            dialogsua.setMessage("Bạn có muốn sửa thông tin sinh viên : ${sv!![position].getTenSV()} này không?")//tạo dialog sửa
            dialogsua.setPositiveButton(
                "Có"
            ) { dialog, which ->
                context!!.onClickAdd(
                    position,
                    sv!![position],
                    sv!![position].getId()!!
                )//gọi hàm onClickAdd trong StudentActivity
            }
            dialogsua.setNegativeButton(
                "Không"
            ) { dialog, which -> }
            dialogsua.show()
        }

        holder.binding!!.layoutStudent.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("student", sv!![position])
            intent.putExtra("tenlop", lh!!.getMaLop())
            context!!.startActivity(intent)
        }
    }   //Convert Byte To BitMap
    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap? {//chuyển byteArray thành bitmap
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }



    override fun getItemCount(): Int {
        return sv!!.size
    }

    fun setData(svs: ArrayList<sinhVien>?) {//cập nhật lại danh sách sinh viên
        sv!!.clear()
        sv!!.addAll(svs!!)
        notifyDataSetChanged()
    }

    class StudentViewHolder : RecyclerView.ViewHolder {
        var binding: ItemSinhvienBinding? = null

        constructor(itemView: ItemSinhvienBinding) : super(itemView.root) {
            binding = itemView
        }
    }
}