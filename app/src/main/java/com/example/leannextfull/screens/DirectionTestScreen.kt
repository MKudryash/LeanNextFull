package com.example.leannextfull.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.example.leannextfull.viewModel.MainViewModel
import com.example.leannextfull.R
import com.example.leannextfull.db.modelsDb.Directions
import kotlinx.coroutines.delay
import java.sql.Time
import java.util.Timer
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectionTestScreen(
    navHostController: NavHostController,
    id: Int,
    name: String,
    viewModel: MainViewModel
) {
    val itemsListCriterias by viewModel.itemsCriterias.observeAsState(listOf())
    val itemsListAnswerCriterias by viewModel.answerResult.observeAsState(listOf())
    val allDirections by viewModel.allDirection.observeAsState(listOf())

    val context = LocalContext.current

    BoxWithConstraints(
    ) {
        val derivedDimension = this.maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .padding(7.dp, 25.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0XFFF5F5F9))
                        .clickable {
                            if (id > 1) {
                                viewModel.getItemsCriterias(id - 1)
                                viewModel.getAnswerCriteries(id - 1)
                                navHostController.navigate("directionTestScreen/" + (id - 1) + "/" + allDirections.first { it -> it.id == id - 1 }.title)
                            } else {
                                viewModel.getItemsCriterias(allDirections.size)
                                viewModel.getAnswerCriteries(allDirections.size)
                                navHostController.navigate("directionTestScreen/" + (allDirections.size) + "/" + allDirections.first { it -> it.id == (allDirections.size) }.title)
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(7.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(6f)
                        .padding(10.dp, 0.dp, 10.dp, 0.dp),
                    text = name,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                    fontSize = 5.em,
                    textAlign = TextAlign.Center,
                )
                Box(
                    modifier = Modifier
                        .padding(7.dp, 25.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0XFFF5F5F9))
                        .clickable {
                            if (id < allDirections.size) {
                                viewModel.getItemsCriterias(id + 1)
                                viewModel.getAnswerCriteries(id + 1)

                                navHostController.navigate("directionTestScreen/" + (id + 1) + "/" + allDirections.first { it -> it.id == id + 1 }.title) {

                                    popUpTo("directionTestScreen/" + id + "/" + allDirections.first { it -> it.id == id }.title)
                                }
                            } else {
                                viewModel.getItemsCriterias(1)
                                viewModel.getAnswerCriteries(1)

                                navHostController.navigate("directionTestScreen/" + (1) + "/" + allDirections.first { it -> it.id == 1 }.title) {

                                    popUpTo("directionTestScreen/" + id + "/" + allDirections.first { it -> it.id == id }.title)
                                }
                            }
                        }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.padding(7.dp),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(derivedDimension * 0.05f, 10.dp, derivedDimension * 0.05f, 20.dp)
                    .border(2.dp, Color.Transparent, RoundedCornerShape(5.dp))
            ) {
                if (itemsListAnswerCriterias.isNotEmpty()) {
                    Divider(
                        modifier = Modifier.weight(itemsListAnswerCriterias.size.toFloat()),
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 8.dp
                    )
                }
                if ((itemsListCriterias.size - itemsListAnswerCriterias.size) != 0) {
                    Divider(
                        modifier = Modifier.weight((itemsListCriterias.size - itemsListAnswerCriterias.size).toFloat()),
                        color = Color(0xFFD9D9D9),
                        thickness = 8.dp
                    )
                }

            }

            LazyColumn(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 92.dp),
            ) {
                itemsIndexed(items = itemsListCriterias) { index,item ->
                    var count = index+1
                    if (count == 0) count = itemsListCriterias.size
                    val sheetState = rememberModalBottomSheetState()
                    var isSheetOpen by rememberSaveable {
                        mutableStateOf(false)
                    }
                    Column {
                        Row(

                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${count} вопрос",
                                fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                                fontSize = 6.em,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(7.dp, 5.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(5.dp, 0.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable {
                                        isSheetOpen = true
                                    }
                            )

                            {
                                Text(
                                    text = "i",
                                    fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                                    fontSize = 4.em,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(12.dp, 3.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 10.dp)
                                .border(
                                    2.dp, Color(0xFFB8C1CC), RoundedCornerShape(20.dp)
                                ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.title,
                                fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(7.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        var selectedIndex by remember { mutableStateOf(-1) }
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(derivedDimension * 0.02f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            itemsIndexed(
                                listOf(
                                    0, 25, 50, 75, 100
                                )

                            ) { index, items ->
                                if (itemsListAnswerCriterias.isNotEmpty()) {
                                    try {
                                        selectedIndex =
                                            itemsListAnswerCriterias.find { it.idCriterias == item.id }?.mark!!.toInt() / 25
                                    } catch (_: Exception) {
                                    }
                                } else selectedIndex = -1
                                val borderColor: Color =
                                    if (index == selectedIndex) MaterialTheme.colorScheme.primary
                                    else Color(0xFFB8C1CC)
                                Box(
                                    modifier = Modifier
                                        .border(
                                            2.5.dp, borderColor, RoundedCornerShape(20.dp)
                                        )
                                        .width(derivedDimension / 5f - derivedDimension * 0.05f)
                                        .height(derivedDimension / 5f - derivedDimension * 0.05f)
                                        .clickable(
                                            onClick = {
                                                selectedIndex =
                                                    if (selectedIndex != index) index else -1
                                                try {
                                                    item.id?.let {
                                                        viewModel.insertAnswerCriteries(
                                                            it,
                                                            items.toDouble(),
                                                            id
                                                        )
                                                    }
                                                    viewModel.getAnswerCriteries(id)
                                                    Timer().schedule(3000) {}
                                                    Log.d("Test", selectedIndex.toString())
                                                } catch (_: Exception) {
                                                }
                                            },
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(
                                                bounded = false,
                                                radius = 35.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = items.toString(),
                                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                                        fontSize = 4.em,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                        if (item.id!! < itemsListCriterias.size) {
                            Divider(
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 2.dp,
                                modifier = Modifier.padding(15.dp, 10.dp)
                            )
                        }
                        if (isSheetOpen) {
                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = { isSheetOpen = false },
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .wrapContentSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(derivedDimension*0.01f, 20.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .weight(2f)
                                                .height(65.dp),
                                            painter = painterResource(id = R.drawable.iconrecom),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            modifier = Modifier
                                                .weight(8f)
                                                .padding(13.dp, 25.dp)
                                                .fillMaxWidth(),
                                            text = "Рекомендация по критерию",
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(derivedDimension*0.04f, 0.dp,derivedDimension*0.04f,10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = item.recommendations,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Justify, softWrap = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
