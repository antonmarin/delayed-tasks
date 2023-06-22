package ru.antonmarin.delayedtasks.api.storage

import java.io.Serializable

interface Storage {
    fun save(taskRequestRecord: TaskRequestRecord)
}

data class TaskRequestRecord(
    val key: Serializable,
    val request: Any
)
