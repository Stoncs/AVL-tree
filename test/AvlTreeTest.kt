package avlTree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Math.abs
import java.util.*
import kotlin.test.assertFailsWith

class AvlTreeTest {
    @Test
    fun toAddTest() {
        val controlSet = mutableSetOf<Int>()
        val random = Random()
        for (iteration in 1..100) {
            for (i in 1..20) {
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
                        "The tree doesn't contain a supposedly added element. $element binarySet: $binarySet"
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
                        "The tree doesn't have the element $element from the control set. ControlSet: $controlSet"
                )
            }
        }
    }

    @Test
    fun doFirstAndLastTest() {
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = sortedSetOf<Int>()
            for (i in 1..10) {
                controlSet += random.nextInt(100)
            }
            println("Control set: $controlSet")
            val expectedFirst = controlSet.first()
            val expectedLast = controlSet.last()
            val binarySet = AvlTree<Int>()
            assertFailsWith<NoSuchElementException>("A first element was found in an empty tree.") {
                binarySet.first()
            }
            assertFailsWith<NoSuchElementException>("A last element was found in an empty tree.") {
                binarySet.last()
            }
            for (element in controlSet) {
                binarySet.add(element)
            }
            val actualFirst = binarySet.first()
            val actualLast = binarySet.last()
            assertEquals(
                    expectedFirst, actualFirst,
                    "The first element was determined incorrectly: was $actualFirst, should've been $expectedFirst."
            )
            assertEquals(
                    expectedLast, actualLast,
                    "The last element was determined incorrectly: was $actualLast, should've been $expectedLast."
            )
            println("First element: $actualFirst. Last element: $actualLast.")
        }
    }

    @Test
    fun doRemoveTest() {
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = mutableSetOf<Int>()
            val removeIndex = random.nextInt(20) + 1
            var toRemove = 0
            for (i in 1..20) {
                val newNumber = random.nextInt(100)
                controlSet.add(newNumber)
                if (i == removeIndex) {
                    toRemove = newNumber
                }
            }
            println("Initial set: $controlSet")
            val binarySet = AvlTree<Int>()
            for (element in controlSet) {
                binarySet.add(element)
            }
            controlSet.remove(toRemove)
            println("Control set: $controlSet")
            val expectedSize = binarySet.size - 1
            val maxHeight = binarySet.height()
            println("Removing element $toRemove from the tree...")
            assertTrue(
                    binarySet.remove(toRemove),
                    "An element was supposedly not removed from the tree when it should have been."
            )
            assertTrue(
                    toRemove !in binarySet,
                    "The tree contains a supposedly removed element."
            )
            assertTrue(
                    binarySet.height() <= maxHeight,
                    "The tree's height increased after BinarySearchTree.remove()."
            )
            assertFalse(
                    binarySet.remove(toRemove),
                    "An element that was already not in the tree was supposedly removed."
            )
            assertEquals(
                    expectedSize, binarySet.size,
                    "The size of the tree is incorrect: was ${binarySet.size}, should've been $expectedSize."
            )
            for (element in controlSet) {
                assertTrue(
                        binarySet.contains(element),
                        "The tree doesn't have the element $element from the control set."
                )
            }
            println("All clear!")
        }
    }

    @Test
    fun doIteratorTest() {
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = TreeSet<Int>()
            val listOfInt = mutableListOf<Int>()
            for (i in 1..10) {
                listOfInt.add(random.nextInt(100))
            }
            val binarySet = AvlTree<Int>()
            assertFalse(
                    binarySet.iterator().hasNext(),
                    "Iterator of an empty tree should not have any next elements."
            )
            for (element in listOfInt) {
                controlSet.add(element)
                binarySet.add(element)
            }
            println("Control set: $controlSet")
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
                        "BinarySearchTreeIterator doesn't traverse the tree correctly. \ncontrolSet: $controlSet" +
                                "\nbinarySet: $binarySet\nlistOfInt: $listOfInt"
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
        val listOfInt = listOf(9, 10, 14, 16)
        for (i in listOfInt) {
            binarySet.add(i)
            controlSet.add(i)
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
        val controlSet = TreeSet<Int>()
        val listOfInt = listOf(10, 2, 50, 1, 30, 100, 150, 80, 90)
        for (i in listOfInt) {
            binarySet.add(i)
            controlSet.add(i)
        }
        val iterator = binarySet.iterator()
        val iterator2 = controlSet.iterator()
        while (iterator.hasNext()) {
            assertEquals(
                    iterator2.next(), iterator.next(),
                    "BinarySearchTreeIterator doesn't traverse the tree correctly."
            )
        }
    }

    @Test
    fun toSmallRightRotation() {
        val binarySet = AvlTree<Int>()
        val controlSet = TreeSet<Int>()
        val listOfInt = listOf(100, 101, 50, 40, 1)
        for (i in listOfInt) {
            binarySet.add(i)
            controlSet.add(i)
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
    fun toLargeRightRotation() {
        val binarySet = AvlTree<Int>()
        val controlSet = TreeSet<Int>()
        val listOfInt = listOf(100, 101, 20, 19, 23, 30)
        for (i in listOfInt) {
            binarySet.add(i)
            controlSet.add(i)
        }
        val iterator = binarySet.iterator()
        val iterator2 = controlSet.iterator()
        while (iterator.hasNext()) {
            assertEquals(
                    iterator2.next(), iterator.next(),
                    "BinarySearchTreeIterator doesn't traverse the tree correctly."
            )
        }
    }

    @Test
    fun doIteratorRemoveTest() {
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = TreeSet<Int>()
            val removeIndex = random.nextInt(20) + 1
            var toRemove = 0
            for (i in 1..20) {
                val newNumber = random.nextInt(100)
                controlSet.add(newNumber)
                if (i == removeIndex) {
                    toRemove = newNumber
                }
            }
            println("Initial set: $controlSet")
            val binarySet = AvlTree<Int>()
            for (element in controlSet) {
                binarySet.add(element)
            }
            controlSet.remove(toRemove)
            println("Control set: $controlSet")
            println("Removing element $toRemove from the tree through the iterator...")
            val iterator = binarySet.iterator()
            assertFailsWith<IllegalStateException>("Something was supposedly removed before the iteration started") {
                iterator.remove()
            }
            var counter = binarySet.size
            print("Iterating: ")
            while (iterator.hasNext()) {
                val element = iterator.next()
                print("$element, ")
                counter--
                if (element == toRemove) {
                    iterator.remove()
                    assertFailsWith<IllegalStateException>("BinarySearchTreeIterator.remove() was successfully called twice in a row.") {
                        iterator.remove()
                    }
                }
            }
            assertEquals(
                    0, counter,
                    "BinarySearchTreeIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
            )
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
            for (element in binarySet) {
                assertTrue(
                        controlSet.contains(element),
                        "The tree has the element $element that is not in control set."
                )
            }
            println("All clear!")
        }
    }

//    @Test
//    fun toSomeSituation() {
//        val controlSet = mutableSetOf(83, 78, 98, 99)
//        val toRemove = 78
//        println("Initial set: $controlSet")
//        val binarySet = AvlTree<Int>()
//        for (element in controlSet) {
//            binarySet.add(element)
//        }
//        val iterator = binarySet.iterator()
//        controlSet.remove(toRemove)
//        println("controlSet: $controlSet")
//        var counter = binarySet.size
//        while (iterator.hasNext()) {
//            val element = iterator.next()
//            print("$element, ")
//            counter--
//            if (element == toRemove) {
//                iterator.remove()
//                assertFailsWith<IllegalStateException>("BinarySearchTreeIterator.remove() was successfully called twice in a row.") {
//                    iterator.remove()
//                }
//            }
//        }
//        assertEquals(
//                0, counter,
//                "BinarySearchTreeIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
//        )
//        assertTrue(
//                toRemove !in binarySet,
//                "The tree contains a supposedly removed element.")
//    }
}