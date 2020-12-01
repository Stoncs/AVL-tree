package avlTree

import java.lang.IllegalArgumentException
import java.util.*
import kotlin.NoSuchElementException

open class AvlTree<T : Comparable<T>> : AbstractMutableSet<T>(), SortedSet<T> {

    class Node<T>(
            var value: T,
            var parent: Node<T>?
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
    }

    private var root: Node<T>? = null

    override var size = 0

    fun height(): Int {
        return height(root)
    }

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

    override fun isEmpty(): Boolean {
        return size == 0
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
        //println("$this")
        return true
    }

    override fun remove(element: T): Boolean {
        val currentNode = find(element)
        if (currentNode == null || currentNode.value != element) return false
        return removeNode(currentNode)
    }

    private fun removeNode(node: Node<T>): Boolean {
        var replacementNode: Node<T>
        var rightTrue = false
        if (node.left == null && node.right == null) {
            //а если корень дерева?
            if (node == root) {
                root = null
            } else {
                if (node == node.parent?.left) {
                    node.parent?.left = null
                } else {
                    node.parent?.right = null
                }
                var nodeBalance = node
                while (nodeBalance != root) {
                    nodeBalance = nodeBalance.parent!!
                    balance(nodeBalance)
                }
            }
            size--
            return true
        }
        if (node.left != null) {
            replacementNode = node.left!!
            while (replacementNode.right != null) {
                replacementNode = replacementNode.right!!
                rightTrue= true
            }
            node.value = replacementNode.value
            if (rightTrue) replacementNode.parent!!.right = replacementNode.left
            else replacementNode.parent!!.left = replacementNode.left
        } else {
            replacementNode = node.right!!
            node.value = replacementNode.value
            node.right = replacementNode.right
            node.left = replacementNode.left

        }
        size--
        var nodeBalance = replacementNode.parent
        while (nodeBalance != root) {
            nodeBalance = nodeBalance!!.parent!!
            balance(nodeBalance)
        }
        return true
    }

    private fun balance(currentNode: Node<T>){
        if (currentNode.right != null) {
            if (height(currentNode.right) - height(currentNode.left) == 2) {
                if (height(currentNode.right!!.left) <= height(currentNode.right!!.right)) {
                    smallLeftRotation(currentNode)
                }
                else {
                    largeLeftRotation(currentNode)
                }

            }
        }
        if (currentNode.left != null) {
            if (height(currentNode.left) - height(currentNode.right) == 2) {
                if (height(currentNode.left!!.right) <= height(currentNode.left!!.left)) {
                    smallRightRotation(currentNode)
                }
                else {
                    largeRightRotation(currentNode)
                }
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
        c?.parent = a
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
        m?.parent = a
        n?.parent = b
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
        val a = node
        val b = a.left!!
        val c = b.right
        newBranchNode = b
        newBranchNode.right = a
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
        b.parent = a.parent
        a.parent = b
        c?.parent = a
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
        val a = node
        val b = a.left!!
        val c = b.right
        val m = c?.left
        val n = c?.right
        newBranchNode = c!!
        newBranchNode.right= a
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
        c.parent = a.parent
        a.parent = c
        b.parent = c
        m?.parent = b
        n?.parent = a
    }

    override fun iterator(): MutableIterator<T> =
            BinarySearchTreeIterator()

    inner class BinarySearchTreeIterator internal constructor() : MutableIterator<T> {
        private var currentNode: Node<T>? = null
        private val stack = Stack<Node<T>>()
        private var count = 0

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
            count++
            addLeftBranch(currentNode?.right)
            //В некоторых редких ситуациях после удаления элемента, когда дерево балансируется с помощью малого
            //левого поворота, становится невозможно пройтись до конца дерева
            if (stack.size == 0 && count < size) stack.push(currentNode!!.parent)
            return currentNode!!.value
        }

        override fun remove() {
            check(currentNode != null)
            removeNode(currentNode!!)
            currentNode = null
        }
    }

    override fun comparator(): Comparator<in T>? =
        AvlTreeComparator()

    inner class AvlTreeComparator : Comparator<T> {

        override fun compare(o1: T, o2: T): Int {
            val x = find(o1)!!
            val y = find(o2)!!
            if (x.value != o1 || y.value != o2) throw IllegalArgumentException()
            return when {
                x.value == y.value -> 0
                x.value > y.value -> -1
                else -> 1
            }
        }

    }

    override fun first(): T {
        var currentNode: Node<T> = root ?: throw NoSuchElementException()
        while (currentNode.left != null) {
            currentNode = currentNode.left!!
        }
        return currentNode.value
    }

    override fun last(): T {
        var currentNode: Node<T> = root ?: throw NoSuchElementException()
        while (currentNode.right != null) {
            currentNode = currentNode.right!!
        }
        return currentNode.value
    }

    override fun clear() {
        root = null
        size = 0
    }

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        if (fromElement >= toElement) throw IllegalArgumentException()
        return SubTree(fromElement, toElement, this)
    }

    override fun headSet(toElement: T): SortedSet<T> {
        return SubTree(null, toElement, this)
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        return SubTree(fromElement, null, this)
    }

    inner class SubTree(private val bottom: T?, private val top: T?, private val tree: AvlTree<T>) : AvlTree<T>() {
        override var size: Int
            get() {
                var countSize = 0
                for (value in tree) {
                    if (check(value)) countSize++
                }
                return countSize
            }
            set(value) {}

        private fun check(element: T): Boolean {
            return (bottom != null && top != null && element >= bottom && element < top ||
                    bottom == null && top != null && element < top ||
                    bottom != null && top == null && element >= bottom)
        }
        override fun add(element: T): Boolean {
            if (!check(element)) throw IllegalArgumentException()
            return tree.add(element)
        }

        override fun contains(element: T): Boolean {
            return check(element) && tree.contains(element)
        }

        override fun remove(element: T): Boolean {
            if (!check(element)) throw IllegalArgumentException()
            return tree.remove(element)
        }

        override fun iterator(): MutableIterator<T> =
                SubTreeIterator()

        inner class SubTreeIterator: MutableIterator<T>{
            private var currentNode: Node<T>? = null
            private val stack = Stack<Node<T>>()

            init {
                addRightAndLeftBranch(root)
            }

            private fun addRightAndLeftBranch(node: Node<T>?) {
                if (node != null) {
                    addRightAndLeftBranch(node.right)
                    if (check(node.value))stack.push(node)
                    addRightAndLeftBranch(node.left)
                }
            }

            override fun hasNext(): Boolean {
                return stack.isNotEmpty()
            }

            override fun next(): T {
                if (stack.isEmpty()) throw NoSuchElementException()
                currentNode = stack.pop()
                return currentNode!!.value
            }

            override fun remove() {
                check(currentNode != null)
                tree.removeNode(currentNode!!)
            }
        }

        override fun first(): T {
            for (value in tree) {
                if (check(value)) return value
            }
            throw NoSuchElementException()
        }

        override fun last(): T {
            var result: T? = null
            for (value in tree) {
                if (check(value)) result = value
            }
            if (result == null) throw NoSuchElementException()
            return result
        }
    }
}