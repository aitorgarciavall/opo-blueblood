package com.example.opoblueblood

class DoublyLinkedList<E> {
    private var head: Node<E>? = null
    private var tail: Node<E>? = null
    var size: Int = 0
        private set

    private class Node<E>(var item: E) {
        var prev: Node<E>? = null
        var next: Node<E>? = null
    }

    fun isEmpty(): Boolean = size == 0

    fun addFirst(element: E) {
        val newNode = Node(element)
        if (isEmpty()) {
            head = newNode
            tail = newNode
        } else {
            newNode.next = head
            head?.prev = newNode
            head = newNode
        }
        size++
    }

    fun addLast(element: E) {
        if (isEmpty()) {
            addFirst(element)
        } else {
            val newNode = Node(element)
            newNode.prev = tail
            tail?.next = newNode
            tail = newNode
            size++
        }
    }

    fun getFirst(): E? = head?.item

    fun removeFirst(): E? {
        if (isEmpty()) return null

        val removedItem = head?.item
        head = head?.next
        if (head == null) {
            tail = null
        } else {
            head?.prev = null
        }
        size--
        return removedItem
    }

    fun removeLast(): E? {
        if (isEmpty()) return null
        if (size == 1) return removeFirst()

        val removedItem = tail?.item
        tail = tail?.prev
        tail?.next = null
        size--
        return removedItem
    }

    fun getCurrent(): E? = head?.item

    fun getPrevious(): E? {
        if (head?.prev != null) {
            head = head?.prev
            return head?.item
        }
        return null
    }

    fun getNext(): E? {
        if (head?.next != null) {
            head = head?.next
            return head?.item
        }
        return null
    }


}
