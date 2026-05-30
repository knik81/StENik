package best.mobile.data.repositoty.file_excel

import best.mobile.entities.LanguageStENik
import best.mobile.entities.ResultStENik
import best.mobile.entities.TextStENik
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.io.readExcel
import org.apache.poi.ss.usermodel.WorkbookFactory

import java.io.File


class ReadExcelFile {


    fun getTextStENikList(
        absolutePath: String,
    ): ResultStENik<MutableMap< String, MutableList<TextStENik>>> {//: MutableList<TextStENik> {

        val textTextStENikMap = mutableMapOf<String, MutableList<TextStENik>>()

        //получаем временный файл
        val tempFile = File(absolutePath)

        if (tempFile.exists()) {
            try {
                //Чтение названия листов
                val sheetNames = WorkbookFactory.create(tempFile).use { workbook ->
                    (0 until workbook.numberOfSheets).map { workbook.getSheetName(it) }
                }

                //Читаем каждый найденный лист
                val dataExcel: Map<String, DataFrame<*>> = sheetNames.associateWith { name ->
                    DataFrame.readExcel(tempFile, sheetName = name)
                }

                //Цикл по каждому листу
                dataExcel.forEach { (name, sheet) ->
                    val textTextStENikList = mutableListOf<TextStENik>()

                    //Обработка каждой строки
                    sheet.forEach { row ->
                        textTextStENikList.add(
                            //Мэппинг из файла Excel в TextStENik
                            TextStENik(
                                id = row["id"].toString().toDouble().toInt(),
                                language = LanguageStENik.EMPTY.stringToLanguageStENik(row["language"].toString()),
                                vocabulary = row["vocabulary"].toString(),
                                text = row["text"].toString(),
                                level = row["level"].toString().toDouble().toInt(),
                                idInit = row["vocabulary"].toString() + "_" + row["id"].toString()
                                    .toDouble().toInt()
                            )
                        )
                    }
                    textTextStENikMap[name] = textTextStENikList
                }

            } catch (e: Exception) {
                if (tempFile.exists()) {
                    tempFile.delete()
                }
                return ResultStENik.Error(e.message ?: "Неизвестная ошибка")
            }
        }
        return ResultStENik.Success(textTextStENikMap)
    }
}