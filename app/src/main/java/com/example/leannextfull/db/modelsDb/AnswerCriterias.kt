package com.example.leannextfull.db.modelsDb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.leannextfull.utlis.Converters
import java.sql.Timestamp
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "AnswerCriterias", foreignKeys = arrayOf(
        ForeignKey(
            entity = Criterias::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idCriterias"),
            onDelete = CASCADE
        )
    )
)
data class AnswerCriterias(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val idCriterias: Int,
    val mark: Double,
    @TypeConverters(Converters::class)
    val date: Date
)
