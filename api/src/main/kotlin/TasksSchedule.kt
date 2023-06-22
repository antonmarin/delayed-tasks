package ru.antonmarin.delayedtasks.api

import ru.antonmarin.delayedtasks.api.key.HashCodeResolver
import ru.antonmarin.delayedtasks.api.key.KeyResolver
import ru.antonmarin.delayedtasks.api.storage.Storage
import ru.antonmarin.delayedtasks.api.storage.TaskRequestRecord
import kotlin.reflect.KClass

/**
 * Facade to work with tasks programmatically
 */
class TasksSchedule(
    private val storage: Storage,
    private val keyResolvers: Map<KClass<*>, KeyResolver<*>> = mapOf()
) {

    fun <R : Any> schedule(request: R) {
        val keyResolver = selectKeyResolver(request)

        storage.save(
            TaskRequestRecord(keyResolver.resolveKey(request), request)
        )
    }

    private val hashCodeResolver = HashCodeResolver()

    @Suppress("UNCHECKED_CAST")
    private fun <R : Any> selectKeyResolver(request: R): KeyResolver<R> {
        return (keyResolvers[request::class] ?: hashCodeResolver) as KeyResolver<R>
    }
}
