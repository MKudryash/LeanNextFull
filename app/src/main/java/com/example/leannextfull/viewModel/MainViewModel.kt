package com.example.leannextfull.viewModel


import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leannextfull.db.MainDb
import com.example.leannextfull.db.Repository
import com.example.leannextfull.db.modelsDb.AnswerCriterias
import com.example.leannextfull.db.modelsDb.Criterias
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.db.modelsDb.Directions
import com.example.leannextfull.utlis.CheckMonth
import com.example.leannextfull.utlis.CheckWeek
import java.util.Date

/**Модель данных между UI и DB*/
class MainViewModel(application: Application) : ViewModel() {

    val allDirection: LiveData<List<Directions>> //Лист всех направлений
    val itemsAllDiagrams: LiveData<List<DevelopmentIndex>>// Результаты тестов текущей недели, для построения диаграммы
    val itemsAllDiagramsLastMonth: LiveData<List<DevelopmentIndex>>// Результаты тестов текущей недели, для построения диаграммы
    val itemsCriterias: LiveData<List<Criterias>> //Вопросы по определенному навправлению

    val searchResults: MutableLiveData<List<DevelopmentIndex>>//Поиск результатов тестов, для определенной недели
    val searchResultsLastMonth: MutableLiveData<List<DevelopmentIndex>>//Поиск результатов тестов, для определенной недели
    val answerResult: MutableLiveData<List<AnswerCriterias>> //Ответы на вопросы определенного напрваления


    private val repository: Repository //Репозиторий


    var week = mutableStateOf(0) //Переменная для перемещения по неделям


    @RequiresApi(Build.VERSION_CODES.O)
    val startDate = mutableStateOf(CheckMonth.getCurrentMonthStartDate(week.value)) //Дата начала месяца относительно сдвига
    @RequiresApi(Build.VERSION_CODES.O)
    val endDate = mutableStateOf(CheckMonth.getCurrentMonthEndDate(week.value)) //Дата конца месяца относительно сдвига


    init {
        val db = MainDb.createDataBase(application)
        val dao = db.dao()
        repository = Repository(dao)

        allDirection = repository.allItemDirection
        itemsAllDiagrams = repository.itemsForDiagram
        itemsAllDiagramsLastMonth = repository.itemsForDiagramLastMonth
        searchResults = repository.searchResults
        searchResultsLastMonth = repository.searchResultsLastMonth
        itemsCriterias = repository.allItemCriterias
        answerResult = repository.answerCriteries
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkday() {
        startDate.value = CheckMonth.getCurrentMonthStartDate(week.value)
        endDate.value = CheckMonth.getCurrentMonthEndDate(week.value)
    }

    fun getAnswerCriteries(idDirections: Int) {
        repository.changeListAnswerCriterias(Date(), idDirections)
    }

    fun getItemsCriterias(id: Int) {
        repository.changeListCriterias(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertAnswerCriteries(idCriteries: Int, mark: Double, idDirections: Int) {
        repository.insertAnswerCriteries(
            AnswerCriterias(null, idCriteries, mark, Date()),
            idDirections,
            startDate.value, endDate.value
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun findDevelopmentIndex() {
        repository.changeListDevelopmentIndex(startDate.value, endDate.value)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun findDevelopmentIndexLastMonth() {
        repository.changeListDevelopmentIndexLastMonth(CheckMonth.getCurrentMonthStartDate(week.value-1),
            CheckMonth.getCurrentMonthEndDate(week.value-1))
    }

}