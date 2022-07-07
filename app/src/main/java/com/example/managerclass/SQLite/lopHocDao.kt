package com.example.managerclass.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.managerclass.Model.lopHoc

class lopHocDao {
    private var TABLE_NAME = "LopHoc"//tên bảng
    private var COL_ID = "id"//tên cột
    private var COL_TENLOP = "tenLop"
    private var COL_MALOP = "maLop"
    private var COL_IDUSER = "idUser"

    private var helper: Database? = null//tạo đối tượng helper
    private var db: SQLiteDatabase? = null//tạo đối tượng db

    constructor(context: Context) {
        this.helper = Database(context)//gọi hỗ trợ
        this.db = helper!!.writableDatabase//gọi đối tượng db
    }

    //insert lop hoc vao database
    fun insertLopHoc(lopHoc: lopHoc): Boolean {
        val values = ContentValues()//tao 1 doi tuong chua cac gia tri can them vao
        values.put(COL_TENLOP, lopHoc.getTenLop())//them gia tri vao doi tuong values
        values.put(COL_MALOP, lopHoc.getMaLop())//them gia tri vao doi tuong values
        values.put(COL_IDUSER, lopHoc.getIdUser())
        return db!!.insert(TABLE_NAME, null, values) > 0//neu them thanh cong tra ve gia tri >0
    }

    //select all lop hoc from database where iduser = iduser
    @SuppressLint("Range")
    fun getAllLopHoc(idUser: Int): ArrayList<lopHoc> {//iduser la id cua user
        val list = ArrayList<lopHoc>()//tao 1 mang lop hoc
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_IDUSER = $idUser"//cau lenh select
        val cursor = db!!.rawQuery(selectQuery, null)//lay du lieu tu database
        if (cursor.moveToFirst()) {//neu co du lieu
            do {//duyet tung
                val lopHoc = lopHoc()//tao 1 doi tuong lop hoc
                lopHoc.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)))//set id
                lopHoc.setTenLop(cursor.getString(cursor.getColumnIndex(COL_TENLOP)))
                lopHoc.setMaLop(cursor.getString(cursor.getColumnIndex(COL_MALOP)))
                lopHoc.setIdUser(cursor.getInt(cursor.getColumnIndex(COL_IDUSER)))
                list.add(lopHoc)//them vao mang
            } while (cursor.moveToNext())//neu co du lieu tiep theo
        }
        return list//tra ve mang
    }

    //get lop hoc by id
    @SuppressLint("Range")
    fun getLopHocById(id: Int): lopHoc {//id la id cua lop hoc
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"//cau lenh select
        val cursor = db!!.rawQuery(selectQuery, null)//lay du lieu tu database
        var lopHoc = lopHoc()//tao 1 doi tuong lop hoc
        if (cursor.moveToFirst()) {//neu co du lieu
            lopHoc.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)))//set id
            lopHoc.setTenLop(cursor.getString(cursor.getColumnIndex(COL_TENLOP)))
            lopHoc.setMaLop(cursor.getString(cursor.getColumnIndex(COL_MALOP)))
            lopHoc.setIdUser(cursor.getInt(cursor.getColumnIndex(COL_IDUSER)))
        }
        return lopHoc//tra ve doi tuong lop hoc
    }

    //update lop hoc
    fun updateLopHoc(lopHoc: lopHoc): Boolean {//lop hoc la doi tuong lop hoc can update
        val values = ContentValues()//tao 1 doi tuong chua cac gia tri can them vao
        values.put(COL_TENLOP, lopHoc.getTenLop())
        values.put(COL_MALOP, lopHoc.getMaLop())
        values.put(COL_IDUSER, lopHoc.getIdUser())
        return db!!.update(
            TABLE_NAME,
            values,
            "$COL_ID = ?",
            arrayOf(lopHoc.getId().toString())
        ) > 0//neu update thanh cong tra ve gia tri >0
    }

    //delete lop hoc
    fun deleteLopHoc(id: Int): Boolean {//id la id cua lop hoc can xoa
        return db!!.delete(
            TABLE_NAME,
            "$COL_ID = ?",
            arrayOf(id.toString())
        ) > 0//neu xoa thanh cong tra ve gia tri >0
    }
    //get lophoc by malop
    @SuppressLint("Range")
    fun getLopHocByMaLop(maLop: String): Boolean {//malop la ma lop hoc can tim
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_MALOP = '$maLop'"//cau lenh select
        val cursor = db!!.rawQuery(selectQuery, null)//lay du lieu tu database
        if (cursor.moveToFirst()) {//neu co du lieu
            return true
        }
        return false
    }
    //search
    @SuppressLint("Range")
    fun searchLopHoc(keyword: String, userID: Int): ArrayList<lopHoc> {//keyword la keyword can tim
        val list = ArrayList<lopHoc>()//tao 1 mang lop hoc
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COL_TENLOP LIKE '%$keyword%'AND $COL_IDUSER = $userID"//cau lenh select
        val cursor = db!!.rawQuery(selectQuery, null)//lay du lieu tu database
        if (cursor.moveToFirst()) {//neu co du lieu
            do {//duyet tung
                val lopHoc = lopHoc()//tao 1 doi tuong lop hoc
                lopHoc.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)))//set id
                lopHoc.setTenLop(cursor.getString(cursor.getColumnIndex(COL_TENLOP)))
                lopHoc.setMaLop(cursor.getString(cursor.getColumnIndex(COL_MALOP)))
                lopHoc.setIdUser(cursor.getInt(cursor.getColumnIndex(COL_IDUSER)))
                list.add(lopHoc)//them vao mang
            } while (cursor.moveToNext())//neu co du lieu tiep theo
        }
        return list//tra ve mang
    }

}