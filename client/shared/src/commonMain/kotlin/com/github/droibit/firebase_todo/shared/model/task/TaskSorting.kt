package com.github.droibit.firebase_todo.shared.model.task

data class TaskSorting(val key: Key, val order: Order) {

    companion object {
        val DEFAULT = TaskSorting(key = Key.CREATED_DATE, order = Order.ASC)
    }

    enum class Key(val id: Int) {
        TITLE(id = 0),
        CREATED_DATE(id = 1);

        companion object {
            operator fun invoke(id: Int): Key = values().first { it.id == id }
        }
    }

    enum class Order(val id: Int) {
        ASC(id = 0),
        DESC(id = 1);

        fun toggled(): Order {
            return if (this == ASC) DESC else ASC
        }

        companion object {
            operator fun invoke(id: Int): Order = values().first { it.id == id }
        }
    }
}