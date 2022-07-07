package com.example.managerclass.Model

import java.io.Serializable

class User : Serializable {
    private var id =0
    private var userName: String? = null
    private var passWorld: String? = null

    constructor() {

    }

    constructor(id: Int, userName: String, passWorld: String) {
        this.id = id
        this.userName = userName
        this.passWorld = passWorld
    }

    constructor(userName: String, passWorld: String) {
        this.userName = userName
        this.passWorld = passWorld
    }


    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }


    fun getPassWorld(): String? {
        return passWorld
    }

    fun setPassWorld(passWorld: String) {
        this.passWorld = passWorld
    }
}