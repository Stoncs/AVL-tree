package avlTree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFailsWith

class AvlTreeTest {
    @Test
    fun toAddTest() {
        val controlSet = mutableSetOf<Int>()
        val random = Random()
        for (iteration in 1..100) {
            for (i in 1..10) {
                controlSet += random.nextInt(100)
            }
            val binarySet = AvlTree<Int>()
            for (element in controlSet) {
                assertTrue(
                        binarySet.add(element),
                        "An element was supposedly not added to the tree when it should have been."
                )
                assertTrue(
                        element in binarySet,
                        "The tree doesn't contain a supposedly added element."
                )
                assertFalse(
                        binarySet.add(element),
                        "An element was supposedly added to the tree twice."
                )
            }
            assertEquals(
                    controlSet.size, binarySet.size,
                    "The size of the tree is incorrect: was ${binarySet.size}, should've been ${controlSet.size}."
            )
            for (element in controlSet) {
                assertTrue(
                        binarySet.contains(element),
                        "The tree doesn't have the element $element from the control set."
                )
            }
        }
    }

    @Test
    fun doIteratorTest() {
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = TreeSet<Int>()
            for (i in 1..10) {
                controlSet.add(random.nextInt(100))
            }
            println("Control set: $controlSet")
            val binarySet = AvlTree<Int>()
            assertFalse(
                    binarySet.iterator().hasNext(),
                    "Iterator of an empty tree should not have any next elements."
            )
            for (element in controlSet) {
                binarySet += element
            }
            val iterator1 = binarySet.iterator()
            val iterator2 = binarySet.iterator()
            println("Checking if calling hasNext() changes the state of the iterator...")
            while (iterator1.hasNext()) {
                assertEquals(
                        iterator2.next(), iterator1.next(),
                        "Calling BinarySearchTreeIterator.hasNext() changes the state of the iterator."
                )
            }
            val controlIter = controlSet.iterator()
            val binaryIter = binarySet.iterator()
            println("Checking if the iterator traverses the tree correctly...")
            while (controlIter.hasNext()) {
                assertEquals(
                        controlIter.next(), binaryIter.next(),
                        "BinarySearchTreeIterator doesn't traverse the tree correctly."
                )
            }
            assertFailsWith<NoSuchElementException>("Something was supposedly returned after the elements ended") {
                binaryIter.next()
            }
            println("All clear!")
        }
    }

    @Test
    fun toSmallLeftRotation() {
        val binarySet = AvlTree<Int>()
        val controlSet = TreeSet<Int>()
        controlSet.add(9)
        controlSet.add(10)
        controlSet.add(14)
        controlSet.add(16)
        for (i in controlSet) {
            binarySet.add(i)
        }
        val iterator = binarySet.iterator()
        val iterator2 = controlSet.iterator()
        while (iterator.hasNext()) {
            assertEquals(
                    iterator.next(), iterator2.next(),
                    "BinarySearchTreeIterator doesn't traverse the tree correctly."
            )
        }

    }

    @Test
    fun toLargeLeftRotation() {
        val binarySet = AvlTree<Int>()
        val controlSet = mutableSetOf(10, 2, 50, 1, 30, 100, 150, 80, 90)
        for (i in controlSet) {
            binarySet.add(i)
        }
        val iterator = binarySet.iterator()
        val iterator2 = controlSet.iterator()
        while (iterator.hasNext()) {
            assertEquals(
                    iterator.next(), iterator2.next(),
                    "BinarySearchTreeIterator doesn't traverse the tree correctly."
            )
        }
    }
}