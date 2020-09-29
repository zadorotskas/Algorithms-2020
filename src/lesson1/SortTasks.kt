@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.min

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

// Трудоёмкость: O(N * logN)
// Ресурсоемкость: S(N)
fun sortAddresses(inputName: String, outputName: String) {
    val map = sortedMapOf<String, MutableList<String>>(compareBy<String> {
        it.split(" ")[0]
    }.thenBy { it.split(" ")[1].toInt() })

    for (line in File(inputName).readLines()) {
        if (!Regex("""[а-яА-ЯёЁ-]+\s[а-яА-ЯёЁ-]+\s-\s[а-яА-ЯёЁ-]+\s\d+""").containsMatchIn(line)) {
            throw IllegalArgumentException()
        }
        val list = line.split(" - ")
        val name = list[0]
        val address = list[1]

        val listOfNames = map.getOrDefault(address, mutableListOf())
        listOfNames.add(name)
        map[address] = listOfNames
    }

    map.map { it.value.sort() }

    File(outputName).bufferedWriter().use {
        map.forEach { (address, listOfNames) ->
            it.write(address + " - " + listOfNames.joinToString(", "))
            it.newLine()
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */

// Трудоёмкость: O(N)
// Ресурсоемкость: S(N)
fun sortTemperatures(inputName: String, outputName: String) {
    val minTemp = 2730
    val maxTemp = 5000
    val list = mutableListOf<Int>()

    for (line in File(inputName).readLines()) {
        val indexOfPoint = line.length - 2
        val number = line.removeRange(indexOfPoint, indexOfPoint + 1)
        list.add(number.toInt() + minTemp)
    }


    val sorted = Sorts.countingSort(list.toIntArray(), maxTemp + minTemp)

    File(outputName).bufferedWriter().use {
        for (number in sorted) {
            val string = (number - minTemp).toString()
            val length = string.length

            val res = if (string.first() == '-' && length == 2) {
                string.subSequence(0, length - 1).toString() + "0." + string.last()
            } else if (length == 1) {
                "0." + string.last()
            } else string.subSequence(0, length - 1).toString() + "." + string.last()

            it.write(res)
            it.newLine()
        }
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */

// Трудоёмкость: O(N)
// Ресурсоемкость: S(N)
fun sortSequence(inputName: String, outputName: String) {
    val mapForCount = mutableMapOf<Int, Int>()
    val list = mutableListOf<Int>()
    for (line in File(inputName).readLines()) {
        val currentNumber = line.toInt()
        val currentCount = mapForCount[currentNumber] ?: 1
        mapForCount[currentNumber] = currentCount + 1
        list.add(currentNumber)
    }
    var maxCount = 0
    var numberWithMaxCount = 0
    mapForCount.forEach {
        val currentCount = it.value
        if (maxCount == currentCount) numberWithMaxCount = min(it.key, numberWithMaxCount)
        if (maxCount < currentCount) {
            numberWithMaxCount = it.key
            maxCount = currentCount
        }
    }

    val res = list.filterNot { it == numberWithMaxCount }
    File(outputName).bufferedWriter().use {
        for (element in res) {
            it.write(element.toString())
            it.newLine()
        }
        for (i in 0 until maxCount - 1) {
            it.write(numberWithMaxCount.toString())
            it.newLine()
        }
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

