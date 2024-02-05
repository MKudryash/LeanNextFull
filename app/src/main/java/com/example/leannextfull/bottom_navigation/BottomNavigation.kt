package com.example.leannextfull.bottom_navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.leannextfull.R
import com.example.leannextfull.utlis.Constants
/*Класс отвечающий за построение нижнего бара и листа*/
@Composable
fun BottomNavigation(navHostController: NavHostController) {
    val listItems = listOf(
        Constants.TestScreen,
        Constants.DiagramScreen
    )
    NavigationBar(
        Modifier.background(Color.White).height(90.dp).border(2.dp,Color(0xFFB1B1B1),
            RoundedCornerShape(13.dp,13.dp,0.dp,0.dp),
        ),
        containerColor =MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        val backStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navHostController.navigate(item.route)
                    {
                        if (currentRoute != null) {
                            popUpTo(currentRoute) {inclusive = true}
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "Icon",
                        modifier = Modifier.size(35.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title, fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 4.em
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color(0xFFB8C1CC),
                    unselectedTextColor = Color(0xFFB8C1CC),
                    indicatorColor =MaterialTheme.colorScheme.background
                )
            )
        }
    }

}