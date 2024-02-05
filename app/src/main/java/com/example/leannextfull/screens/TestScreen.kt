package com.example.leannextfull.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import com.example.leannextfull.R
import com.example.leannextfull.viewModel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TestScreen(
    navHostController: NavHostController,     viewModel: MainViewModel
) {
    val allDirections by viewModel.allDirection.observeAsState(listOf())
    var searching by remember { mutableStateOf(false) }
    val itemsForDiagram by viewModel.itemsAllDiagrams.observeAsState(listOf())


    val searchResults by viewModel.searchResults.observeAsState(listOf())


    BoxWithConstraints(
    ) {
        val derivedDimension = this.maxWidth
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight()
                    .padding(0.dp,25.dp,0.dp,15.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Box(
                    Modifier
                        .weight(1f)
                        .clickable {
                            --viewModel.week.value
                            viewModel.checkday()
                            viewModel.findDevelopmentIndex()
                            searching = viewModel.week.value != 0
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowleft),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Column(
                    Modifier.weight(4f),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = "Еженедельный отчет",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                        fontSize = 4.5.em,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "${format.format(viewModel.startDate.value)} - ${
                            format.format(
                                viewModel.endDate.value
                            )
                        }",
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 3.em,
                        textAlign = TextAlign.Center,
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .clickable {
                            if (viewModel.week.value<0)  ++viewModel.week.value
                            viewModel.checkday()
                            viewModel.findDevelopmentIndex()
                            searching = viewModel.week.value != 0
                        },
                    contentAlignment = Alignment.Center

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowright),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            val list = if (searching) searchResults else itemsForDiagram
            var sum = 0.0
            if (list != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 92.dp),
                )
                {
                    items(allDirections) { item ->
                        var index = 0.0
                        list.forEach {
                            if (it.idDirection == item.id) index = it.mark
                        }
                        sum+=index
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(derivedDimension*0.04f, 10.dp)
                                .weight(1f)
                                .wrapContentHeight()
                                .clickable(onClick =
                                {
                                    viewModel.getItemsCriterias(item.id!!)
                                    viewModel.getAnswerCriteries(item.id)

                                    navHostController.navigate("directionTestScreen/" + item.id + "/" + item.title)
                                }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val context = LocalContext.current
                            val drawableId = remember(item.nameIcon) {
                                context.resources.getIdentifier(
                                    item.nameIcon,
                                    "drawable",
                                    context.packageName
                                )
                            }
                            Icon(
                                modifier = Modifier.weight(2f).height(75.dp),
                                painter = painterResource(id = drawableId),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                modifier = Modifier
                                    .weight(6f)
                                    .wrapContentHeight()
                                    .padding(10.dp, 0.dp, 0.dp, 0.dp),
                                text = item.title,
                                color = MaterialTheme.colorScheme.secondary,
                                fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                                fontSize = 4.em,
                                lineHeight = 1.5.em,
                                textAlign = TextAlign.Center,
                                softWrap = true

                                )
                            Text(
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(10.dp, 0.dp, 0.dp, 0.dp),
                                text = String.format("%.1f", index),
                                color = MaterialTheme.colorScheme.secondary,
                                fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                                fontSize = 4.em,
                                textAlign = TextAlign.Center,

                                )

                        }
                        if (item.id!! < allDirections.size) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 2.dp,
                                modifier = Modifier.padding(15.dp, 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


