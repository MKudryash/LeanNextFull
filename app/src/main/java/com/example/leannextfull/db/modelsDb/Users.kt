package com.example.leannextfull.db.modelsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Users"
)
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val name: String,
    val login:String,
    val password: String
)