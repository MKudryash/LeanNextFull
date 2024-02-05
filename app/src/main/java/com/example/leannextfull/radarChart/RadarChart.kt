package com.example.leannextfull.radarChart

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.navigation.NavHostController
import com.example.leannextfull.radarChart.modelRadarChart.AngleTitle
import com.example.leannextfull.radarChart.modelRadarChart.NetLinesStyle
import com.example.leannextfull.radarChart.modelRadarChart.Polygon
import com.example.leannextfull.viewModel.MainViewModel
import kotlin.math.atan2



@OptIn(ExperimentalTextApi::class)
@Composable
fun RadarChart(
    radarLabels: List<String>,
    labelsStyle: TextStyle,
    netLinesStyle: NetLinesStyle,
    scalarSteps: Int,
    scalarValue: Double,
    scalarValuesStyle: TextStyle,
    polygons: List<Polygon>,
    polygonsLast: List<Polygon>,
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: MainViewModel
) {

    val textMeasurer = rememberTextMeasurer()
    var centre by remember {
        mutableStateOf(Offset.Zero)
    }
    var labelsEndPoints by remember {
        mutableStateOf(listOf<Offset>())
    }

    Canvas(modifier = modifier.pointerInput(true) {
        detectTapGestures(
            onTap = { offset ->
                var index = 1
                val listAngle = mutableListOf<AngleTitle>()

                labelsEndPoints.forEach {
                    listAngle += AngleTitle(atan2(it.y - centre.y, it.x - centre.x), index)
                    index++
                }

                listAngle += AngleTitle(3.0f, listAngle.maxBy { it.angle }.id)
                listAngle += AngleTitle(-3.0f, listAngle.maxBy { it.angle }.id)
                listAngle.sortBy { it.angle }

                index = 0

                val angle = atan2(offset.y - centre.y, offset.x - centre.x)
                Log.d("TapX", offset.x.toString())
                Log.d("TapY", offset.y.toString())
                listAngle.forEach lit@{
                    val i = if (index + 1 != listAngle.size) index + 1
                    else return@lit
                    if (angle in it.angle..listAngle[i].angle)
                    {
                        viewModel.getItemsCriterias(it.id)
                        viewModel.getAnswerCriteries(it.id)
                        navHostController.navigate("directionTestScreen/${it.id}/" + radarLabels[it.id-1])
                    }
                    index++
                }
            }
        )
    }) {

        centre = Offset(size.width / 2, size.height / 2)
        val labelWidth = measureMaxLabelWidth(radarLabels, labelsStyle, textMeasurer)

        val radius = (size.minDimension / 2.7f) - (10.toDp().toPx())
        val labelRadius = (size.minDimension / 2) - (labelWidth / 5)
        val numLines = radarLabels.size
        val radarChartConfig =
            calculateRadarConfig(labelRadius, radius, size, numLines, scalarSteps)
        labelsEndPoints = radarChartConfig.labelsPoints

        drawRadarNet(netLinesStyle, radarChartConfig)


        polygons.forEach {
            drawPolygonShape(
                this,
                it,
                radius,
                scalarValue ,
                Offset(size.width / 2, size.height / 2),
                scalarSteps
            )
        }
        polygonsLast.forEach {
            drawPolygonShape(
                this,
                it,
                radius,
                scalarValue ,
                Offset(size.width / 2, size.height / 2),
                scalarSteps
            )
        }

        drawAxisData(
            labelsStyle,
            scalarValuesStyle,
            textMeasurer,
            radarChartConfig,
            radarLabels,
            scalarValue,
            scalarSteps,
            polygons[0].unit
        )
    }

}

private fun validateRadarChartConfiguration(
    radarLabels: List<String>,
    scalarValue: Double,
    polygons: List<Polygon>,
    scalarSteps: Int
) {
    require(scalarSteps > 0) { "Scalar steps must be a positive value" }
    require(scalarValue > 0.0) { "Scalar value must be greater than 0" }
    require(radarLabels.size >= 3) { "At least 3 radar labels are required" }

    for (polygon in polygons) {
        require(polygon.values.size == radarLabels.size) {
            "Number of polygon values must match the number of radar labels"
        }
        polygon.values.forEachIndexed { index, value ->
            require(value in 0.0..scalarValue) {
                "Polygon value at index $index must be between 0 and scalar value ($scalarValue)"
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.measureMaxLabelWidth(
    radarLabels: List<String>,
    labelsStyle: TextStyle,
    textMeasurer: TextMeasurer
): Float {
    return textMeasurer.measure(
        AnnotatedString(
            text = radarLabels.maxByOrNull { it.length } ?: "",
        ), style = labelsStyle
    ).size.width.toDp().toPx()
}