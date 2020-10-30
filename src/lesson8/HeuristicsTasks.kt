@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson8

import lesson6.Graph
import lesson6.Path
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import kotlin.random.Random

// Примечание: в этом уроке достаточно решить одну задачу

/**
 * Решить задачу о ранце (см. урок 6) любым эвристическим методом
 *
 * Очень сложная
 *
 * load - общая вместимость ранца, items - список предметов
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */
fun fillKnapsackHeuristics(load: Int, items: List<Item>, vararg parameters: Any): Fill {
    TODO()
}

/**
 * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
 * или любым другим эвристическим методом, кроме генетического и имитации отжига
 * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
 *
 * Очень сложная
 *
 * Граф передаётся через получатель метода
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */

// Трудоемкость: O(iterations * antsNumber * edges)
// Ресурсоемкость: S(antsNumber * edges)
fun Graph.findVoyagingPathHeuristics(antsNumber: Int, iterations: Int): Path {

    val pheromoneNumber = this.edges.size.toDouble() * 5
    val pheromone = mutableMapOf<Graph.Edge, Double>()
    this.edges.forEach {
        pheromone[it] = 0.0
    }

    var bestPath: Path? = null

    class Ant(private var currentVertex: Graph.Vertex) {
        val graph = this@findVoyagingPathHeuristics
        var path: Path = Path(currentVertex)
        val visited = mutableSetOf(currentVertex)
        val start = currentVertex
        var done = false
        var finishedInStart = false

        fun countPher() {
            val pherForEveryEdge = pheromoneNumber / path.length
            val iter = path.vertices.iterator()
            var current: Graph.Vertex
            var next = if (iter.hasNext()) iter.next() else null
            while (iter.hasNext()) {
                current = next!!
                next = iter.next()
                val edge = graph.getConnection(current, next)
                pheromone[edge!!] = pheromone[edge]!! + pherForEveryEdge
            }
        }

        fun compareWithBestPath() {
            if (bestPath == null) {
                bestPath = path
            } else {
                if (path.length < bestPath!!.length) {
                    bestPath = path
                } else if (path.length == bestPath!!.length && path.vertices.size < bestPath!!.vertices.size) {
                    bestPath = path
                }
            }
        }

        fun reset() {
            done = false
            visited.clear()
            visited.add(start)
            path = Path(start)
        }

        fun move() {
            val neighbors = graph.getNeighbors(currentVertex)
            val unvisitedNeighbors = neighbors.filter { it !in visited }
            val toIterate = if (unvisitedNeighbors.isEmpty()) neighbors else unvisitedNeighbors
            var next: Graph.Vertex? = null
            val coefficient = mutableMapOf<Graph.Vertex, Pair<Double, Double>>()
            var previousCoef = 0.0
            toIterate.forEach {
                val edge = graph.getConnection(currentVertex, it)!!
                val thisPheromone = pheromone[edge]!!
                val kf = 50.0 / edge.weight
                coefficient[it] = Pair(previousCoef, previousCoef + thisPheromone + kf)
                previousCoef += thisPheromone + kf
            }

            val r = Random.nextDouble(0.0, previousCoef)
            coefficient.forEach {
                val interval = it.value
                val leftBorder = interval.first
                val rightBorder = interval.second
                if (leftBorder < r && r <= rightBorder) {
                    next = it.key
                }
            }

            visited.add(next!!)
            currentVertex = next!!
            path = Path(path, graph, currentVertex)
            if (visited.size == graph.vertices.size) {
                done = true
                currentVertex = start
                if (start in graph.getNeighbors(path.vertices.last())) {
                    path = Path(path, graph, currentVertex)
                    finishedInStart = true
                } else {
                    finishedInStart = false
                }
            }
        }
    }

    val needAnts = this.vertices.shuffled().take(antsNumber)
    val ants = List(minOf(antsNumber, this.vertices.size)) { Ant(needAnts[it]) }

    for (i in 0 until iterations) {
        ants.forEach {
            it.reset()
            while (!it.done) {
                it.move()
            }
        }
        ants.forEach {
            if (it.finishedInStart) {
                it.countPher()
                it.compareWithBestPath()
            }
        }
    }

    return bestPath ?: Path()
}

