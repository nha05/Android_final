package com.example.managerclass.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.managerclass.Adapter.ClassAdapter.ViewHolder
import com.example.managerclass.Model.lopHoc
import com.example.managerclass.Model.sinhVien
import com.example.managerclass.SQLite.lopHocDao
import com.example.managerclass.SQLite.sinhVienDao
import com.example.managerclass.Screen.ClassActivity
import com.example.managerclass.Screen.StudentActivity
import com.example.managerclass.databinding.ItemLophocBinding

class ClassAdapter() : RecyclerView.Adapter<ViewHolder>() {

    var lh: ArrayList<lopHoc>? = null
    var context: ClassActivity? = null
    var sinhviendao: sinhVienDao? = null
    var listSinhVien: java.util.ArrayList<sinhVien>? = null

    constructor(lh: ArrayList<lopHoc>?, context: ClassActivity?) : this() {
        this.lh = lh
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ClassAdapter.ViewHolder(
            ItemLophocBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return lh!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //lấy ra số thứ tự của lớp học
        val stt = position + 1
        //lấy ra id của user để lấy ra danh sách lớp học của user để xóa lớp học
        var idUser = lh!![position].getIdUser()
        holder.binding!!.tenlop.text = lh!![position].getTenLop()
        holder.binding!!.malop.text = lh!![position].getMaLop()
        holder.binding!!.stt.text = stt.toString()
        holder.binding!!.layoutStudent.setOnClickListener {
            val intent: Intent = Intent(context, StudentActivity::class.java)
            //gửi id lớp học để lấy ra danh sách sinh viên của lớp học
            intent.putExtra("malop", lh!![position].getId())
            intent.putExtra("idUser", idUser.toString())
            context!!.startActivity(intent)
        }

        holder.binding!!.btnxoa.setOnClickListener {
            var index = position
            var id = lh!![index].getId()
            sinhviendao = sinhVienDao(context!!)
            //lấy ra danh sách sinh viên của lớp học
            listSinhVien =
                sinhviendao!!.getAllSinhVien(id!!) as java.util.ArrayList<sinhVien>

            if (listSinhVien!!.size > 0) {//nếu có sinh viên trong lớp học
                val dialog = AlertDialog.Builder(context)//tạo dialog
                dialog.setMessage("Hiện tại đang có sinh viên trong lớp không thể xóa!")//thông báo
                dialog.setPositiveButton(
                    "OK"
                ) { dialog, which -> dialog.dismiss() }
                dialog.show()
            } else {//nếu không có sinh viên trong lớp học
                val dialogxoalh = AlertDialog.Builder(context)//tạo dialog
                dialogxoalh.setMessage("Bạn có muốn xóa lớp ${lh!![index].getTenLop()} này không?")//thông báo
                dialogxoalh.setPositiveButton(//nút xác nhận
                    "Có"//nút xác nhận
                ) { dialog, which ->
                    lopHocDao(context!!).deleteLopHoc(id)//xóa lớp học
                    setData(lopHocDao(context!!).getAllLopHoc(idUser!!))//cập nhật lại danh sách lớp học
                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                }
                dialogxoalh.setNegativeButton(
                    "Không"
                ) { dialog, which -> }
                dialogxoalh.show()
            }
        }

        holder.binding!!.btnsua.setOnClickListener {
            context!!.onClickAdd(
                1,
                lh!![position].getId(),
                lh!![position].getTenLop(),
                lh!![position].getMaLop()
            )
        }
    }

    fun setData(lha: ArrayList<lopHoc>?) {//cập nhật lại danh sách lớp học
        lh!!.clear()
        lh!!.addAll(lha!!)
        notifyDataSetChanged()
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var binding: ItemLophocBinding? = null

        constructor(itemView: ItemLophocBinding) : super(itemView.root) {
            this.binding = itemView
        }
    }


}