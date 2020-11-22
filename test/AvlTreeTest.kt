package avlTree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AvlTreeTest {
    @Test
    fun toAddTest() {
        val controlSet1 = mutableSetOf(10, 9, 11, 14, 12)
        var binarySet = AvlTree<Int>()
        for (element in controlSet1) {
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
                controlSet1.size, binarySet.size,
                "The size of the tree is incorrect: was ${binarySet.size}, should've been ${controlSet1.size}."
        )
        for (element in controlSet1) {
            assertTrue(
                    binarySet.contains(element),
                    "The tree doesn't have the element $element from the control set."
            )
        }
        val controlSet2 = mutableSetOf(10, 9, 11, 14, 15, 16, 18)
        binarySet = AvlTree()
        for (element in controlSet2) {
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
                controlSet2.size, binarySet.size,
                "The size of the tree is incorrect: was ${binarySet.size}, should've been ${controlSet2.size}."
        )
        for (element in controlSet2) {
            assertTrue(
                    binarySet.contains(element),
                    "The tree doesn't have the element $element from the control set."
            )
        }
    }
}