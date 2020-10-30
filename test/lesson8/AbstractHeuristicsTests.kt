package lesson8

import lesson6.Graph
import lesson6.Path
import lesson6.impl.GraphBuilder
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import lesson7.knapsack.fillKnapsackGreedy
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractHeuristicsTests {

    fun fillKnapsackCompareWithGreedyTest(fillKnapsackHeuristics: (Int, List<Item>) -> Fill) {
        for (i in 0..9) {
            val items = mutableListOf<Item>()
            val random = Random()
            for (j in 0 until 10000) {
                items += Item(1 + random.nextInt(10000), 300 + random.nextInt(600))
            }
            try {
                val fillHeuristics = fillKnapsackHeuristics(1000, items)
                println("Heuristics score = " + fillHeuristics.cost)
                val fillGreedy = fillKnapsackGreedy(1000, items)
                println("Greedy score = " + fillGreedy.cost)
                assertTrue(fillHeuristics.cost >= fillGreedy.cost)
            } catch (e: StackOverflowError) {
                println("Greedy failed with Stack Overflow")
            }
        }
    }

    fun findVoyagingPathHeuristics(findVoyagingPathHeuristics: Graph.() -> Path) {

        fun check(graph: Graph, expected: List<Graph.Vertex?>, expectedLength: Int) {
            val path = graph.findVoyagingPathHeuristics()
            assertEquals(expectedLength, path.length)
            val vertices = path.vertices
            assertEquals(vertices.first(), vertices.last(), "Voyaging path $vertices must be loop!")
            val withoutLast = vertices.dropLast(1)
            assertEquals(expected.size, withoutLast.size, "Voyaging path $vertices must travel through all vertices!")
            expected.forEach {
                assertTrue(it in vertices, "Voyaging path $vertices must travel through all vertices!")
            }
        }

        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            addConnection(a, b, 10)
            addConnection(b, c, 15)
            addConnection(c, f, 30)
            addConnection(a, d, 20)
            addConnection(d, e, 25)
            addConnection(e, f, 15)
            addConnection(a, f, 40)
            addConnection(b, d, 10)
            addConnection(c, e, 5)
        }.build()

        val expected = listOf(graph["A"], graph["D"], graph["B"], graph["C"], graph["E"], graph["F"])
        check(graph, expected, 105)

        val secondGraph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, c, 20)
            addConnection(a, d, 35)
            addConnection(a, b, 30)
            addConnection(b, d, 40)
            addConnection(c, d, 10)
            addConnection(d, g, 25)
            addConnection(d, f, 50)
            addConnection(g, h, 15)
            addConnection(g, f, 35)
            addConnection(f, h, 20)
            addConnection(f, j, 5)
            addConnection(f, e, 30)
            addConnection(j, i, 20)
            addConnection(j, h, 10)
            addConnection(i, e, 60)
            addConnection(e, b, 20)
        }.build()

        val expectedInSecondGraph = listOf(
            secondGraph["A"],
            secondGraph["B"],
            secondGraph["C"],
            secondGraph["D"],
            secondGraph["E"],
            secondGraph["G"],
            secondGraph["F"],
            secondGraph["H"],
            secondGraph["I"],
            secondGraph["J"],
            secondGraph["J"]
        )

        check(secondGraph, expectedInSecondGraph, 205)

        val emptyGraph = GraphBuilder().apply {}.build()
        val emptyPath = emptyGraph.findVoyagingPathHeuristics()
        assertEquals(emptyPath.length, Path().length)
        assertEquals(emptyPath.vertices, Path().vertices)
    }
}