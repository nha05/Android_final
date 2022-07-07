package com.example.managerclass.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database (context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Manager.db"
    }

    //tao bang
    override fun onCreate(db: SQLiteDatabase?) {
        val lopHoc = "CREATE TABLE LopHoc(id INTEGER PRIMARY KEY AUTOINCREMENT,tenLop TEXT,maLop TEXT,idUser INTEGER)"//tao bang lop hoc
        val sinhVien = "CREATE TABLE SinhVien(id INTEGER PRIMARY KEY AUTOINCREMENT,maSV TEXT,tenSV TEXT,hinh BLOB,sdt INTEGER,email TEXT,nganh TEXT,idLop INTEGER)"
        val user = "CREATE TABLE User(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,password TEXT)"
        db?.execSQL(lopHoc)
        db?.execSQL(sinhVien)
        db?.execSQL(user)

    }

    //nang cap database hien tai(them bang, them/xoa cot cua 1 bang,..)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
        db!!.execSQL("DROP TABLE IF EXISTS LopHoc")
        db!!.execSQL("DROP TABLE IF EXISTS SinhVien")
        db!!.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }
}