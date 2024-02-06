package com.example.leannextfull.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.leannextfull.db.modelsDb.AnswerCriterias
import com.example.leannextfull.db.modelsDb.Criterias
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.db.modelsDb.Directions
import com.example.leannextfull.db.modelsDb.Users
import com.example.leannextfull.utlis.Converters
/**Создание базы данных
 * Таблицы:
 * Направления
 * Критерии (критерий принадлежит одному напрвалению)
 * Ответы на критерии
 * Индекс развития - хранит данные ответов пользователя (подсчитанный критерий за определенную дату)
 * */
@Database(
    entities = [
        AnswerCriterias::class,
        Criterias::class,
        DevelopmentIndex::class,
        Directions::class,
        Users::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {

        private var INSTANCE: MainDb? = null

        fun createDataBase(context: Context): MainDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDb::class.java,
                        "leannext_full"
                    )
                        .createFromAsset("leannext_default_full.db") //Направления и критерия заполнены по умолчанию
                        .build()
                    INSTANCE = instance

                }
                return instance
            }
        }
    }
}