package com.example.managerclass.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.managerclass.Model.sinhVien

class sinhVienDao {
    private var db: SQLiteDatabase? = null
    private var dbHelper: Database? = null
    private val TABLE_NAME = "SinhVien"
    private val COLUMN_ID = "id"
    private val COLUMN_MASV = "maSV"
    private val COLUMN_TENSV = "tenSV"
    private val COLUMN_HINH = "hinh"
    private val COLUMN_SDT = "sdt"
    private val COLUMN_EMAIL = "email"
    private val COLUMN_NGANH = "nganh"
    private val COLUMN_IDLOP = "idLop"

    constructor(context: Context) {
        dbHelper = Database(context)
        db = dbHelper!!.writableDatabase;
    }

    //insert sinh vien vao database
    fun insert(sinhVien: sinhVien): Boolean {//insert sinh vien vao database
        val values = ContentValues()//tao 1 doi tuong chua cac gia tri can them vao
        values.put(COLUMN_MASV, sinhVien.getMaSV())
        values.put(COLUMN_TENSV, sinhVien.getTenSV())
        values.put(COLUMN_HINH, sinhVien.getHinh())
        values.put(COLUMN_SDT, sinhVien.getSdt())
        values.put(COLUMN_EMAIL, sinhVien.getEmail())
        values.put(COLUMN_NGANH, sinhVien.getNganh())
        values.put(COLUMN_IDLOP, sinhVien.getIdLop())
        val result = db!!.insert(TABLE_NAME, null, values)//insert vao database
        return result > 0
    }

    //select all sinh vien from database where idLop = idLop
    @SuppressLint("Range")
    fun getAllSinhVien(idLop: Int): ArrayList<sinhVien> {//get all sinh vien from database where idLop = idLop
        val list = ArrayList<sinhVien>()//create list
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_IDLOP = $idLop"//get all sinh vien from database where idLop = idLop
        val cursor =
            db!!.rawQuery(selectQuery, null)//get all sinh vien from database where idLop = idLop
        if (cursor.moveToFirst()) {//if cursor move to first
            do {
                val sinhVien = sinhVien()
                sinhVien.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
                sinhVien.setMaSV(cursor.getString(cursor.getColumnIndex(COLUMN_MASV)))
                sinhVien.setTenSV(cursor.getString(cursor.getColumnIndex(COLUMN_TENSV)))
                sinhVien.setHinh(cursor.getBlob(cursor.getColumnIndex(COLUMN_HINH)))
                sinhVien.setSdt(cursor.getString(cursor.getColumnIndex(COLUMN_SDT)))
                sinhVien.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
                sinhVien.setNganh(cursor.getString(cursor.getColumnIndex(COLUMN_NGANH)))
                sinhVien.setIdLop(cursor.getInt(cursor.getColumnIndex(COLUMN_IDLOP)))
                list.add(sinhVien)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    //update sinh vien
    fun update(sinhVien: sinhVien, id: Int? = null): Boolean {//id = null -> update all
        val values = ContentValues()//content value
        values.put(COLUMN_MASV, sinhVien.getMaSV())
        values.put(COLUMN_TENSV, sinhVien.getTenSV())
        values.put(COLUMN_HINH, sinhVien.getHinh())
        values.put(COLUMN_SDT, sinhVien.getSdt())
        values.put(COLUMN_EMAIL, sinhVien.getEmail())// update sinh vien
        values.put(COLUMN_NGANH, sinhVien.getNganh())//update sinh vien
        values.put(COLUMN_IDLOP, sinhVien.getIdLop())//idLop
        val result = db!!.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )//update sinh vien theo id
        return result > 0//neu result > 0 thi update thanh cong
    }

    //delete sinh vien
    fun delete(id: Int): Boolean {//delete sinh vien theo id
        val result = db!!.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )//delete sinh vien theo id
        return result > 0//return true neu delete thanh cong
    }

    //search
    @SuppressLint("Range")
    fun search(keyword: String, idLop: Int): ArrayList<sinhVien> {//search sinh vien theo ten
        val list = ArrayList<sinhVien>()//create list
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TENSV LIKE '%$keyword%' AND $COLUMN_IDLOP = $idLop"//search sinh vien theo ten
        val cursor = db!!.rawQuery(selectQuery, null)//search sinh vien theo ten
        if (cursor.moveToFirst()) {//if cursor move to first
            do {
                val sinhVien = sinhVien()
                sinhVien.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
                sinhVien.setMaSV(cursor.getString(cursor.getColumnIndex(COLUMN_MASV)))
                sinhVien.setTenSV(cursor.getString(cursor.getColumnIndex(COLUMN_TENSV)))
                sinhVien.setHinh(cursor.getBlob(cursor.getColumnIndex(COLUMN_HINH)))
                sinhVien.setSdt(cursor.getString(cursor.getColumnIndex(COLUMN_SDT)))
                sinhVien.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
                sinhVien.setNganh(cursor.getString(cursor.getColumnIndex(COLUMN_NGANH)))
                sinhVien.setIdLop(cursor.getInt(cursor.getColumnIndex(COLUMN_IDLOP)))
                list.add(sinhVien)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}