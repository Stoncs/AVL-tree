class AvlTree<T : Comparable<T>> : AbstractMutableSet<T>() {

    private class Node<T>(
            var value: T,
            val parent: Node<T>?
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
        var height = 0
    }

    private var root: Node<T>? = null

    override var size = 0

    private fun height(node: Node<T>?): Int {
        return node?.height ?: -1
    }

    private fun find(element: T): Node<T>? =
        root?.let { find(it, element)}


    private fun find(start: Node<T>, element: T): Node<T> {
        return when {
            start.value == element -> start
            element < start.value -> start.left?.let { find(it, element) } ?: start
            else -> start.right?.let { find(it, element) } ?: start
        }

    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        if (closest?.value == element) return false
        size++
        when {
            closest == null -> {
                root = Node(element, null)
                return true
            }
            closest.value < element -> closest.left = Node(element, closest)
            else -> closest.right = Node(element, closest)
        }
        closest.height++
        var currentNode = closest!!
        do {
            balance(currentNode)
            currentNode = currentNode.parent!!
        } while (currentNode != root)
        return true
    }
    private fun balance(currentNode: Node<T>) {
        currentNode.height = maxOf(height(currentNode.right), height(currentNode.left)) + 1
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

    private fun smallLeftRotation(node: Node<T>) {
        TODO("Not yet implemented")
    }

    private fun largeLeftRotation(node: Node<T>) {
        TODO("Not yet implemented")
    }

    private fun smallRightRotation(node: Node<T>) {
        TODO("Not yet implemented")
    }

    private fun largeRightRotation(node: Node<T>) {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<T> {
        TODO("Not yet implemented")
    }


}