package com.example.managerclass.Model

import java.io.Serializable

class sinhVien : Serializable{
    private var id: Int? = null
    private var maSV: String? = null
    private var idLop: Int? = null
    private var tenSV: String? = null
    private var hinh: ByteArray = byteArrayOf()
    private var sdt: String? = null
    private var email: String? = null
    private var nganh: String? = null

    constructor()

    constructor(id: Int?, maSV: String, idLop: Int, tenSV: String, hinh: ByteArray, sdt: String, email: String, nganh: String) {
        this.id = id
        this.maSV = maSV
        this.idLop = idLop
        this.tenSV = tenSV
        this.hinh = hinh
        this.sdt = sdt
        this.email = email
        this.nganh = nganh
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }
    fun getMaSV(): String {
        return maSV!!
    }

    fun setMaSV(maSV: String) {
        this.maSV = maSV
    }

    fun getIdLop(): Int {
        return idLop!!
    }

    fun setIdLop(idLop: Int) {
        this.idLop = idLop
    }

    fun getTenSV(): String? {
        return tenSV
    }

    fun setTenSV(tenSV: String) {
        this.tenSV = tenSV
    }

    fun getHinh(): ByteArray {
        return hinh
    }

    fun setHinh(hinh: ByteArray) {
        this.hinh = hinh
    }

    fun getSdt(): String {
        return sdt!!
    }

    fun setSdt(sdt: String) {
        this.sdt = sdt
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getNganh(): String? {
        return nganh
    }

    fun setNganh(nganh: String) {
        this.nganh = nganh
    }
}