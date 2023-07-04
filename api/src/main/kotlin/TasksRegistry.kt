package ru.antonmarin.delayedtasks.api

import kotlin.reflect.KClass

class TasksRegistry(private val requestsTasks: Map<KClass<*>, Task<*>>) {
    @Suppress("UNCHECKED_CAST")
    fun <T: TaskRequest> getRequestTask(kClass: KClass<out T>): Task<T> = (requestsTasks[kClass] ?: UnexpectedRequestTask) as Task<T>
}
