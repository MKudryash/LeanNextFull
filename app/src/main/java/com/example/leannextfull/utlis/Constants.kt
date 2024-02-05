package com.example.leannextfull.utlis


import com.example.leannextfull.R
import com.example.leannextfull.bottom_navigation.BottomItem

/**Модель простого экрана*/
sealed class  Screen(val route:String)
/**Константы страниц*/
object Constants {
    object DiagramScreen : BottomItem("Диаграмма", R.drawable.diagram,"diagram")
    object ProfileScreen : BottomItem("Профиль", R.drawable.profile,"profile")
    object TestScreen : BottomItem("Тесты", R.drawable.test,"test")
    object DirectionTestScreen:Screen("directionTestScreen/{id}/{name}")
    object MainScreen:Screen("mainScreen")
    var userName:String = ""

}