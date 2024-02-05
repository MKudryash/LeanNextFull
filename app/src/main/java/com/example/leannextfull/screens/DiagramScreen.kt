package com.example.leannextfull.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.leannextfull.R
import com.example.leannextfull.dataStore.StoreData
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.radarChart.RadarChart
import com.example.leannextfull.radarChart.modelRadarChart.NetLinesStyle
import com.example.leannextfull.radarChart.modelRadarChart.Polygon
import com.example.leannextfull.radarChart.modelRadarChart.PolygonStyle
import com.example.leannextfull.roundCheckBox.RoundCheckBox
import com.example.leannextfull.roundCheckBox.RoundCheckBoxDefaults
import com.example.leannextfull.screens.constantsUI.ConstantsUI
import com.example.leannextfull.utlis.Constants
import com.example.leannextfull.utlis.ExportDataToCsv
import com.example.leannextfull.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


val format = SimpleDateFormat("dd MMMM", Locale("ru"))


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiagramScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel,
) {


    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val allDirections by viewModel.allDirection.observeAsState(listOf())
    val itemsForDiagram by viewModel.itemsAllDiagrams.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())
    val searchResultsLastMonth by viewModel.searchResultsLastMonth.observeAsState(listOf())
    val itemsForDiagramLastMonth by viewModel.itemsAllDiagramsLastMonth.observeAsState(listOf())
    var searching by remember { mutableStateOf(false) }
    var roundCheckBoxState by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var show by remember {
        mutableStateOf(false)
    }
    var showInformation by remember {
        mutableStateOf(false)
    }
    BoxWithConstraints(
        Modifier.padding(0.dp, 0.dp, 0.dp, 95.dp)
    ) {
        val derivedDimension = this.maxWidth

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(this@BoxWithConstraints.maxHeight)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(derivedDimension * 0.01f, derivedDimension * 0.04f, 0.dp, 0.dp)
                        .weight(7f)
                        .clickable {
                            show = true
                        },
                    text = "Добро пожаловать ${Constants.userName}!",
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                    fontSize = 5.em,
                    lineHeight = 1.em,
                    textAlign = TextAlign.Center,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(derivedDimension * 0.03f, derivedDimension * 0.09f)
                        .weight(7f),
                    text = "Индекс развития технологий и инструментов Кайдзен (КDI)",
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                    fontSize = 4.5.em,
                    lineHeight = 1.em,
                    textAlign = TextAlign.Center,
                )
                Box(
                )
                {

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.menuicon),
                            contentDescription = "Показать меню",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(20.dp)
                            )
                            .background(MaterialTheme.colorScheme.background),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 0.dp, derivedDimension * 0.03f, 0.dp)
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0f))
                                .clickable {
                                    ExportDataToCsv.createXlFile(
                                        itemsForDiagram,
                                        allDirections,
                                        format.format(viewModel.startDate.value),
                                        true,
                                        context
                                    )
                                }
                        )
                        {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.saveicon),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                "Скачать",
                                fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                                fontSize = 4.em,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 2.dp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 1f))
                                .clickable {
                                    ExportDataToCsv.createXlFile(
                                        itemsForDiagram,
                                        allDirections,
                                        format.format(viewModel.startDate.value),
                                        false,
                                        context
                                    )

                                }
                        )
                        {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.shareicon),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                "Поделиться",
                                fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 4.em,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(),
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
                            viewModel.findDevelopmentIndexLastMonth()
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
                        text = "Ежемесячный отчет",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                        fontSize = 4.em,
                        textAlign = TextAlign.Center,
                    )
                    // notificationPermissionState.launchPermissionRequest()
                    Text(
                        text = "${format.format(viewModel.startDate.value)} - ${
                            format.format(
                                viewModel.endDate.value
                            )
                        }",
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 3.5.em,
                        textAlign = TextAlign.Center,
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .clickable {
                            if (viewModel.week.value < 0) ++viewModel.week.value
                            viewModel.checkday()
                            viewModel.findDevelopmentIndex()
                            viewModel.findDevelopmentIndexLastMonth()
                            searching = viewModel.week.value != 0
                        },
                    contentAlignment = Alignment.Center,


                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowright),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    "Отчет за прошлый месяц", color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                    fontSize = 3.em,
                    textAlign = TextAlign.Center,
                )
                RoundCheckBox(
                    modifier = Modifier.width(45.dp),
                    color = RoundCheckBoxDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.background,
                        tickColor = MaterialTheme.colorScheme.secondary,
                        borderColor = MaterialTheme.colorScheme.primary
                    ),
                    isChecked = roundCheckBoxState,
                    onClick = { roundCheckBoxState = !roundCheckBoxState
                        viewModel.findDevelopmentIndexLastMonth()},
                    enabled = true
                )

            }
            val list = if (searching) searchResults else itemsForDiagram
            val listLastMonth = if (roundCheckBoxState) searchResultsLastMonth else itemsForDiagramLastMonth
            RadarChartSample(list, listLastMonth, derivedDimension, navHostController, viewModel)
            var sum = 0.0
            list.forEach {
                sum += it.mark
            }
            try {
                sum /= allDirections.size
            } catch (_: Exception) {
            }
            val colorItem = if (sum != 0.0) MaterialTheme.colorScheme.primary
            else Color(0xFFFF6864)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(derivedDimension * 0.01f, derivedDimension * 0.04f)
                    .clickable {
                        showInformation = true
                    },
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Box(
                    Modifier
                        .weight(12f)
                        .wrapContentHeight()
                        .padding(derivedDimension * 0.02f, 0.dp)
                        .border(
                            2.dp,
                            colorItem,
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        modifier = Modifier.padding(
                            derivedDimension * 0.01f,
                            derivedDimension * 0.04f
                        ),
                        text = "Итог",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 4.em,
                        textAlign = TextAlign.Center,
                    )
                }
                Box(
                    Modifier
                        .weight(2.7f)
                        .wrapContentHeight()
                        .padding(derivedDimension * 0.02f, 0.dp)
                        .border(
                            2.dp,
                            colorItem,
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        modifier = Modifier.padding(0.dp, derivedDimension * 0.04f),
                        text = String.format("%.1f", sum),
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 4.em,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(derivedDimension * 0.08f, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(derivedDimension * 0.02f, 0.dp)
                        .border(
                            2.dp,
                            colorItem,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            isSheetOpen = true
                        },
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        modifier = Modifier.padding(
                            derivedDimension * 0.01f,
                            derivedDimension * 0.04f
                        ),
                        text = "Свяжитесь с нами!",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                        fontSize = 4.em,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { isSheetOpen = false },
                ) {
                    BottomConnect()
                }
            }

            if (Constants.userName == "") {
                InputDialogView {
                }
            }
            if (show) {
                InputDialogView {
                    show = !show
                }
            }
            if (showInformation) {
                InformationView {
                    showInformation = !showInformation
                }
            }

        }
    }

}

@Composable
fun RadarChartSample(
    itemsListDirectionIndex: List<DevelopmentIndex>?,
    itemsListDirectionIndexLastMonth: List<DevelopmentIndex>?,
    w: Dp,
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    val labelDirection =
        listOf(
            "5S и Визуальный менеджмент",
            "Cистема ТРМ",
            "Быстрая переналадка SMED",
            "Стандартизированная работа",
            "Картирование",
            "Выстраивание\nпотока",
            "Вовлечение\nперсонала",
            "Обучение\nперсонала",
            "Логистика"
        )
    //Сделать в одном цикле!
    var valuesDirection = arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    var valuesDirectionLastMonth = arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    var indexDir = 1

    // iterate it using a mutable iterator and modify values
    val iterate = valuesDirection.listIterator()
    while (iterate.hasNext()) {
        iterate.next()
        itemsListDirectionIndex?.forEach {
            if (it.idDirection == indexDir) {
                iterate.set(it.mark)
            }
        }
        indexDir++
    }
    indexDir = 1
    val iterates = valuesDirectionLastMonth.listIterator()
    while (iterates.hasNext()) {
        iterates.next()
        itemsListDirectionIndexLastMonth?.forEach {
            if (it.idDirection == indexDir) {
                iterates.set(it.mark)
            }
        }
        indexDir++
    }

    val labelsStyle = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
        fontSize = 2.5.em,
        textAlign = TextAlign.Center,
        hyphens = Hyphens.Auto
    )

    val scalarValuesStyle = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
        fontSize = 2.5.em
    )

    RadarChart(
        modifier = Modifier.size(w),
        radarLabels = labelDirection,
        labelsStyle = labelsStyle,
        netLinesStyle = NetLinesStyle(
            netLineColor = Color(0x90ffD3CFD3),
            netLinesStrokeWidth = 3f,
            netLinesStrokeCap = StrokeCap.Butt
        ),
        scalarSteps = 4,
        scalarValue = 100.0,
        scalarValuesStyle = scalarValuesStyle,
        polygons = listOf(
            Polygon(
                values = valuesDirection,
                unit = "",
                style = PolygonStyle(
                    fillColor = Color(0x594CBBBF),
                    fillColorAlpha = 0.5f,
                    borderColor = MaterialTheme.colorScheme.primary,
                    borderColorAlpha = 0.5f,
                    borderStrokeWidth = 6f,
                    borderStrokeCap = StrokeCap.Butt,
                )
            )
        ),
        polygonsLast = listOf(
            Polygon(
                values = valuesDirectionLastMonth,
                unit = "",
                style = PolygonStyle(
                    fillColor = Color(0x80FF6864),
                    fillColorAlpha = 0.5f,
                    borderColor = Color(0xFFFF6864),
                    borderColorAlpha = 0.5f,
                    borderStrokeWidth = 6f,
                    borderStrokeCap = StrokeCap.Butt,
                )
            )
        ),
        navHostController = navHostController,
        viewModel = viewModel
    )
}

@Composable
fun InputDialogView(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val dataStore = StoreData(context)
    Log.d("Dialog", "Here")

    var nameUser by remember {
        mutableStateOf(Constants.userName)
    }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),

            ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Text(
                    text = "Введите имя пользователя",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 4.em,
                    fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                    color = MaterialTheme.colorScheme.secondary
                )

                OutlinedTextField(
                    value = nameUser,
                    singleLine = true,
                    onValueChange = { nameUser = it },
                    modifier = Modifier.padding(8.dp),
                    label = {
                        Text(
                            "Имя пользователя"
                        )
                    },
                )
                Row()
                {
                    Button(
                        onClick = {
                            onDismiss()
                            scope.launch {
                                dataStore.saveData(nameUser)
                            }

                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(20.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                    ) {
                        Text(
                            text = "Сохранить",
                            fontSize = 3.em,
                            fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun InformationView(onDismiss: () -> Unit) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),

            ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Text(
                    text = "Точки роста есть! Напишите нам в Telegram! Возьмите наш опыт себе на развитие",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 5.em,
                    fontFamily = FontFamily(Font(R.font.neosanspro_medium)),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )


                Button(
                    onClick = {

                        onDismiss()
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(20.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background)

                ) {
                    Text(
                        text = "Спасибо!",
                        fontSize = 3.em,
                        fontFamily = FontFamily(Font(R.font.neosanspro_bold)),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}


@Composable
fun BottomConnect() {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(0.dp, 25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(ConstantsUI.connectionWithUs) { it ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(0.dp, 1.dp)
                    .clickable {
                        uriHandler.openUri(it.link)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    painter = painterResource(id = it.idIcon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,

                    )
                Text(
                    it.title,
                    fontFamily = FontFamily(Font(R.font.neosanspro_regular)),
                    fontSize = 5.em,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrowoutward),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        }
    }
}
