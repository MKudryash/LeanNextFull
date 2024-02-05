package com.example.leannextfull.db.modelsDb

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "Criterias", foreignKeys = arrayOf(
        ForeignKey(
            entity = Directions::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idDirection"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Criterias(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    val idDirection: Int,
    val recommendations: String
)
