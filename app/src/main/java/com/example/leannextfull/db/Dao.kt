package com.example.leannextfull.db


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.leannextfull.db.modelsDb.AnswerCriterias
import com.example.leannextfull.db.modelsDb.Criterias
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.db.modelsDb.Directions

import java.util.Date

@Dao
interface Dao {
    /**Запросы к таблице Ответы на критерий*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemAnswerCriterias(answerCriterias: AnswerCriterias?)
    @Query("Update AnswerCriterias set mark = :answer where date=:date and idCriterias = :idCriterias")
    suspend fun updateItemAnswerCriterias(answer:Double, date: Date, idCriterias: Int)
    @Query(value = "SELECT * FROM AnswerCriterias WHERE idCriterias =:idCriterias and date(date)>= :startWeek and date(date)<=:endWeek")
    fun foundItemAnswerCriteriasIndexForDate( idCriterias: Int,startWeek: Date,endWeek:Date): AnswerCriterias

    @Query(value = "select *\n" +
            "from AnswerCriterias as AC join Criterias as C on AC.idCriterias = C.id\n" +
            "Where C.idDirection = :idDirection and date = :date")
    fun getListAnswer( idDirection: Int,date:Date): List<AnswerCriterias>




    /**Запросы к таблице Критерии*/
    @Query(value = "SELECT * FROM Criterias WHERE idDirection = :id")
    fun foundItemCriteriasForDirection(id: Int): List<Criterias>


    /**Запросы к таблице Индекс развития*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemDevelopmentIndex(developmentIndex: DevelopmentIndex)
    @Query(value = "SELECT * FROM DevelopmentIndex WHERE date = :date and idDirection = :idDirection")
    fun foundItemDevelopmentIndexForDate(date: Date, idDirection: Int): DevelopmentIndex

    @Query(value = "SELECT * FROM DevelopmentIndex WHERE date(date)>= :startWeek and date(date)<=:endWeek")
    fun getAllDevelopmentIndexDate(startWeek: Date,endWeek:Date): LiveData<List<DevelopmentIndex>>

    @Query(value = "SELECT * FROM DevelopmentIndex WHERE date(date)>= :startWeek and date(date)<=:endWeek")
    fun getAllDevelopmentIndexDate1(startWeek: Date,endWeek:Date): List<DevelopmentIndex>
    @Query("Update DevelopmentIndex set mark = :mark where id = :id")
    fun updateDevelopmentIndex(id: Int,mark:Double)
    @Query(value = "SELECT * FROM DevelopmentIndex WHERE date(date)>= :startWeek and date(date)<=:endWeek")
    fun getDevelopmentIndexLastMonth(startWeek: Date,endWeek:Date): LiveData<List<DevelopmentIndex>>


    /**Запросы к таблице Навправления*/

    @Query("SELECT * FROM Directions")
    fun getAllItemsDirection(): LiveData<List<Directions>>
    @Query(
        value = "select sum(mark)/(select count(id) from Criterias as C where c.idDirection = :idDirection)\n" +
                "from Criterias as C\n" +
                "join AnswerCriterias as AC on C.id = AC.idCriterias\n" +
                "where c.idDirection = :idDirection and date =  :date"
    )
    fun CalculationMartForDevelopmentIndex(idDirection: Int, date: Date): Double
}
