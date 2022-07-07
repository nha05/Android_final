package com.example.managerclass.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.managerclass.Model.User

class userDao {
    private var TABLE_NAME = "User"
    private var COLUMN_ID = "id"
    private var COLUMN_USERNAME = "username"
    private var COLUMN_PASSWORD = "password"
    private var db: SQLiteDatabase? = null
    private var dbHelper: Database? = null

    constructor(context: Context) {
        dbHelper = Database(context)
        db = dbHelper!!.writableDatabase
    }

    //Insert user to database
    fun insertUser(user: User): Boolean {
        if(checkUser(user)){
            return false
        }
        val values = ContentValues()
        values.put(COLUMN_USERNAME, user.getUserName())
        values.put(COLUMN_PASSWORD, user.getPassWorld())
        val success = db!!.insert(TABLE_NAME, null, values)
        return success > 0
    }

    //Check user is exist
    fun checkUser(user: User): Boolean {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = '${user.getUserName()}'"
        val cursor = db!!.rawQuery(query, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    //Get user by username
    @SuppressLint("Range")
    fun getUser(user: User): User {
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = '${user.getUserName()}' AND $COLUMN_PASSWORD = '${user.getPassWorld()}'"
        val cursor = db!!.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val user = User(id, username, password)
            cursor.close()
            return user
        }
        cursor.close()
        return User()
    }
}