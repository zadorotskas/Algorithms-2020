@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.lang.StringBuilder
import kotlin.math.sqrt

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    TODO()
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */

//Трудоёмкость: O(N * M)
//Ресурсоемкость: S(N * M)
fun longestCommonSubstring(first: String, second: String): String {
    if (first.isEmpty() || second.isEmpty()) {
        return ""
    }

    val matrix = Array(first.length) { Array(second.length) { 0 } }

    var maxLength = 0
    var maxIndexOfFirst = 0

    matrix.forEachIndexed { indexFirst, array ->
        array.forEachIndexed { indexSecond, _ ->
            if (first[indexFirst] == second[indexSecond]) {
                if (indexFirst != 0 && indexSecond != 0) {
                    matrix[indexFirst][indexSecond] = matrix[indexFirst - 1][indexSecond - 1] + 1
                } else {
                    matrix[indexFirst][indexSecond] = 1
                }
                if (matrix[indexFirst][indexSecond] > maxLength) {
                    maxLength = matrix[indexFirst][indexSecond]
                    maxIndexOfFirst = indexFirst
                }
            }
        }
    }

    return first.substring(maxIndexOfFirst - maxLength + 1, maxIndexOfFirst + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
//Трудоёмкость: O(N)
//Ресурсоемкость: S(N)
fun calcPrimesNumber(limit: Int): Int {
    println("starting with limit $limit")
    if (limit <= 1) return 0
    val list = MutableList(limit + 1) { 0 }
    val sqrtLimit = sqrt(limit.toDouble()).toInt()
    var x2 = 0

    for (i in 1..sqrtLimit) {
        x2 += 2 * i - 1
        var y2 = 0
        for (j in 1..sqrtLimit) {
            y2 += 2 * j - 1
            var n = 4 * x2 + y2
            if (n <= limit && (n % 12 == 1 || n % 12 == 5)) list[n]++
            n -= x2
            if (n <= limit && n % 12 == 7) list[n]++
            n -= 2 * y2
            if (i > j && n <= limit && n % 12 == 11) list[n]++
        }
    }

    list[2] = 1
    if (limit > 2) list[3] = 1
    if (limit > 4) list[5] = 1
    for (i in 5..sqrtLimit) {
        if (list[i] % 2 == 1) {
            val square = i * i
            for (j in square..limit step square) {
                list[j] = 0
            }
        }
    }

    return list.count { it % 2 == 1 }
}
