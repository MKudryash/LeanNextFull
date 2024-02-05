package com.example.leannextfull.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.leannextfull.db.Dao
import com.example.leannextfull.db.modelsDb.AnswerCriterias
import com.example.leannextfull.db.modelsDb.Criterias
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.db.modelsDb.Directions
import com.example.leannextfull.utlis.CheckWeek
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date
/**Репозиторий методов обращающихся к Dao (SQL - запросам)*/
class Repository(private val dao: Dao) {
    val allItemDirection: LiveData<List<Directions>> = dao.getAllItemsDirection() //Список всех направлений
    val itemsForDiagram: LiveData<List<DevelopmentIndex>> = dao.getAllDevelopmentIndexDate( //Получение индексов текущей недели
        CheckWeek.PreviousNextWeekModay(0),
        CheckWeek.PreviousNextWeekSunday(0)
    )
    val searchResults = MutableLiveData<List<DevelopmentIndex>>() //Поиск индексов по выбранной неделе
    val allItemCriterias = MutableLiveData<List<Criterias>>() // Список критериев
    val answerCriteries = MutableLiveData<List<AnswerCriterias>>() //Список ответов для навправления
    val itemsForDiagramLastMonth:LiveData<List<DevelopmentIndex>> = dao.getDevelopmentIndexLastMonth( //Получение индексов текущей недели
        CheckWeek.PreviousNextWeekModay(-1),
        CheckWeek.PreviousNextWeekSunday(-1)
    )
    val searchResultsLastMonth = MutableLiveData<List<DevelopmentIndex>>() //Поиск индексов по выбранной неделе
    private val coroutineScope = CoroutineScope(Dispatchers.Main) //Поток корутины

    fun changeListDevelopmentIndex(startDate: Date, endDate: Date) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFindDevelopmentIndex(startDate, endDate).await()
        }
    }
    private fun asyncFindDevelopmentIndex(
        startDate: Date,
        endDate: Date
    ): Deferred<List<DevelopmentIndex>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async dao.getAllDevelopmentIndexDate1(startDate, endDate)
        }

    fun changeListDevelopmentIndexLastMonth(startDate: Date, endDate: Date) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResultsLastMonth.value = asyncFindDevelopmentIndexLastMonth(startDate, endDate).await()
        }
    }
    private fun asyncFindDevelopmentIndexLastMonth(
        startDate: Date,
        endDate: Date
    ): Deferred<List<DevelopmentIndex>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async dao.getAllDevelopmentIndexDate1(startDate, endDate)
        }

    fun changeListCriterias(id: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            allItemCriterias.value = asyncFindAnswerCriterias(id).await()
        }
    }

    private fun asyncFindAnswerCriterias(id: Int): Deferred<List<Criterias>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async dao.foundItemCriteriasForDirection(id)
        }
    fun changeListAnswerCriterias(date: Date,idDirection:Int) {
        coroutineScope.launch(Dispatchers.Main) {
            answerCriteries.value = asyncFindAnswerCriterias(date,idDirection).await()
        }
    }

    private fun asyncFindAnswerCriterias(date: Date,idDirection: Int): Deferred<List<AnswerCriterias>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async dao.getListAnswer(idDirection,date)
        }


    fun insertAnswerCriteries(answerCriterias: AnswerCriterias, idDirection:Int,startDate: Date,endDate:Date) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                if (dao.foundItemAnswerCriteriasIndexForDate(
                        answerCriterias.idCriterias,
                        startDate, endDate
                    ) == null
                ) {
                    dao.insertItemAnswerCriterias(answerCriterias = answerCriterias)
                    var local = dao.foundItemDevelopmentIndexForDate(Date(), idDirection)
                    val mark = dao.CalculationMartForDevelopmentIndex(idDirection, Date())
                    if (local == null) {
                        dao.insertItemDevelopmentIndex(
                            DevelopmentIndex(
                                null,
                                1,
                                Date(),
                                idDirection,
                                mark
                            )
                        )
                    } else dao.updateDevelopmentIndex(local.id!!, mark)
                } else {
                    dao.updateItemAnswerCriterias(
                        answerCriterias.mark,
                        answerCriterias.date,
                        answerCriterias.idCriterias
                    )
                    var local = dao.foundItemDevelopmentIndexForDate(Date(), idDirection)
                    val mark = dao.CalculationMartForDevelopmentIndex(idDirection, Date())
                    dao.updateDevelopmentIndex(local.id!!, mark)

                }
            }
            catch (_:Exception){}
        }
    }
}