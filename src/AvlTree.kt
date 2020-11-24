package avlTree

import java.lang.Math.abs
import java.util.*

class AvlTree<T : Comparable<T>> : AbstractMutableSet<T>(), SortedSet<T> {

    class Node<T>(
            var value: T,
            var parent: Node<T>?
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
    }

    private var root: Node<T>? = null

    override var size = 0

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + maxOf(height(node.left), height(node.right))
    }


    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        size++
        when {
            closest == null -> {
                root = Node(element, null)
                return true
            }
            comparison < 0 -> closest.left = Node(element, closest)
            else -> closest.right = Node(element, closest)
        }
        var currentNode = closest!!
        balance(currentNode)
        while (currentNode != root) {
            currentNode = currentNode.parent!!
            balance(currentNode)
        }
        return true
    }

    private fun balance(currentNode: Node<T>) {
        if (currentNode.right != null) {
            if (height(currentNode.right) - height(currentNode.left) == 2) {
                if (height(currentNode.right!!.left) <= height(currentNode.right!!.right))
                    smallLeftRotation(currentNode)
                else
                    largeLeftRotation(currentNode)
            }
        }
        if (currentNode.left != null) {
            if (height(currentNode.left) - height(currentNode.right) == 2) {
                if (height(currentNode.left!!.right) <= height(currentNode.right!!.left))
                    smallLeftRotation(currentNode)
                else
                    largeLeftRotation(currentNode)
            }
        }
    }

    /*
    было:
            a
          /   \
         L     b
             /   \
            C     R
     стало:
               b
            /    \
           a      R
         /   \
        L     C
    */
    private fun smallLeftRotation(node: Node<T>) {
        val newBranchNode: Node<T>
        val a = node
        val b = a.right!!
        val c = b.left
        newBranchNode = b
        newBranchNode.left = a
        newBranchNode.left?.right = c
        //проверка на корень
        if (node.parent != null) {
            //правый или левый ребёнок исходный нод
            if (node.parent!!.right == node) {
                node.parent!!.right = newBranchNode
            } else {
                node.parent!!.left = newBranchNode
            }
        } else {
            root = newBranchNode
        }
        b.parent = a.parent
        a.parent = b
    }

    /*
    было:
            a
          /   \
         L     b
             /   \
            c     R
          /   \
         M     N
     стало:
                с
            /      \
           a        b
         /   \    /   \
        L    M   N     R
    */
    private fun largeLeftRotation(node: Node<T>) {
        val newBranchNode: Node<T>
        val a = node
        val b = a.right!!
        val c = b.left
        val m = c?.left
        val n = c?.right
        newBranchNode = c!!
        a.right = m
        b.left = n
        newBranchNode.left = a
        newBranchNode.right = b
        //проверка на корень
        if (node.parent != null) {
            //правый или левый ребёнок исходный нод
            if (node.parent!!.right == node) {
                node.parent!!.right = newBranchNode
            } else {
                node.parent!!.left = newBranchNode
            }
        } else {
            root = newBranchNode
        }
        c.parent = a.parent
        a.parent = c
        b.parent = c
    }

    /*
    было:
            a
          /   \
         b     R
       /   \
      L     C
     стало:
            b
          /   \
         L     a
             /   \
            C     R
    */
    private fun smallRightRotation(node: Node<T>) {
        val newBranchNode: Node<T>
        val b = node.left!!
        val c = b.right
        newBranchNode = b
        newBranchNode.right = node
        newBranchNode.right?.left = c
        //проверка на корень
        if (node.parent != null) {
            //правый или левый ребёнок исходный нод
            if (node.parent!!.right == node) {
                node.parent!!.right = newBranchNode
            } else {
                node.parent!!.left = newBranchNode
            }
        } else {
            root = newBranchNode
        }
    }

    /*
    было:
            a
          /   \
         b     R
       /   \
      L     c
          /   \
         M     N
     стало:
                с
            /      \
           b        a
         /   \    /   \
        L    M   N     R
    */
    private fun largeRightRotation(node: Node<T>) {
        val newBranchNode: Node<T>
        val b = node.left!!
        val c = b.right
        val m = c?.left
        val n = c?.right
        newBranchNode = c!!
        newBranchNode.right= node
        newBranchNode.left = b
        newBranchNode.left?.right = m
        newBranchNode.right?.left = n
        //проверка на корень
        if (node.parent != null) {
            //правый или левый ребёнок исходный нод
            if (node.parent!!.right == node) {
                node.parent!!.right = newBranchNode
            } else {
                node.parent!!.left = newBranchNode
            }
        } else {
            root = newBranchNode
        }
    }

    override fun iterator(): MutableIterator<T> =
            BinarySearchTreeIterator()

    inner class BinarySearchTreeIterator internal constructor() : MutableIterator<T> {
        private var currentNode: Node<T>? = null
        private val stack = Stack<Node<T>>()

        init {
            addLeftBranch(root)
        }

        private fun addLeftBranch(node: Node<T>?) {
            if (node != null) {
                stack.push(node)
                addLeftBranch(node.left)
            }
        }

        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

        override fun next(): T {
            if (stack.isEmpty()) throw NoSuchElementException()
            currentNode = stack.pop()
            addLeftBranch(currentNode?.right)
            //для тестов
            println("${currentNode!!.value} " + height(currentNode))
            return currentNode!!.value
        }


        override fun remove() {
            TODO("Not yet implemented")
        }


    }

    override fun comparator(): Comparator<in T>? =
        null

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO("Not yet implemented")
    }

    override fun headSet(toElement: T): SortedSet<T> {
        TODO("Not yet implemented")
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO("Not yet implemented")
    }

    override fun first(): T {
        TODO("Not yet implemented")
    }

    override fun last(): T {
        TODO("Not yet implemented")
    }
}