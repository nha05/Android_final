package com.example.managerclass.Model

 class lopHoc {
    private var id: Int? = null
    private var tenLop: String? = null
    private var maLop: String? = null
    private var idUser : Int? = null


    constructor(id: Int?, tenLop: String?, maLop: String?, idUser: Int?) {
        this.id = id
        this.tenLop = tenLop
        this.maLop = maLop
        this.idUser = idUser
    }
     constructor(tenLop: String?, maLop: String?, idUser: Int?) {
         this.id = id
         this.tenLop = tenLop
         this.maLop = maLop
         this.idUser = idUser
     }
     constructor()

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getTenLop(): String? {
        return tenLop
    }

    fun setTenLop(tenLop: String?) {
        this.tenLop = tenLop
    }

    fun getMaLop(): String? {
        return maLop
    }

    fun setMaLop(maLop: String?) {
        this.maLop = maLop
    }

    fun getIdUser(): Int? {
        return idUser
    }

    fun setIdUser(idUser: Int?) {
        this.idUser = idUser
    }
}