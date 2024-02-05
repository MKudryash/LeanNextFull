package com.example.leannextfull.utlis

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.leannextfull.db.modelsDb.DevelopmentIndex
import com.example.leannextfull.db.modelsDb.Directions
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**Заполнение excel с возможностью отправки или сохранения*/
object ExportDataToCsv {

    //Создание и заполнение файла
    @RequiresApi(Build.VERSION_CODES.O)
    fun createXlFile(
        devindex: List<DevelopmentIndex>,
        nameDirections: List<Directions>,
        startDate: String,
        save: Boolean,
        context: Context
    ) {

        val wb: Workbook = HSSFWorkbook()
        var cell: Cell? = null
        var sheet: Sheet? = null
        sheet = wb.createSheet(Constants.userName)
        //Now column and row
        val row: Row = sheet.createRow(0)
        cell = row.createCell(0)
        cell.setCellValue("Наименование критерия")
        cell = row.createCell(1)
        cell.setCellValue("Оценка")
        cell = row.createCell(2)
        cell.setCellValue(Constants.userName)
        for (i in nameDirections.indices) {
            val row1: Row = sheet.createRow(i + 1)
            cell = row1.createCell(0)
            cell.setCellValue(nameDirections[i].title)
            sheet.setColumnWidth(0, 20 * 200)
            var index = 0.0

            devindex.forEach {
                if (it.idDirection == nameDirections[i].id) index = it.mark
            }
            cell = row1.createCell(1)
            cell.setCellValue(String.format("%.1f", index))
        }

        val file = checkFile(save, context, startDate)

        streamFolder(file,wb)

        if (!save) {
            SendOtherApp(context,file)
        }
        else Toast.makeText(context,"Успешно сохранено в загрузки!",Toast.LENGTH_LONG).show()

    }
    //Отправка в другое приложение
    fun SendOtherApp(context: Context,file: File)
    {
        val uris = FileProvider.getUriForFile(context, "com.anni.shareimage.fileprovider", file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uris)
        intent.type = "text/*"

        context.startActivity(Intent.createChooser(intent, "Share Via"))
    }
    //Проверка файла в сохраненных
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkFile(save:Boolean, context:Context, startDate: String):File
    {
        context.cacheDir.deleteRecursively()
        val folder =
            if (save) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            else context.cacheDir

        var fileName = "Отчет от $startDate.xls"

        var path = "" + folder + File.separator + fileName
        var flag = true
        var index = 1
        while (flag) {
            if (Files.exists(Paths.get(path))) {
                fileName = "Отчет от $startDate ($index).xls"
                path = "" + folder + File.separator + fileName
                index++
            } else {
                flag = false
            }
        }
        return File(folder, fileName)
    }
    //Запись файла в нужную папку
    fun streamFolder(file:File,wb:Workbook)
    {
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file.path)
            wb.write(outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                outputStream!!.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}
