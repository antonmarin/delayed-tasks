package ru.antonmarin.delayedtasks.api.key

import java.io.Serializable

interface KeyResolver<in RequestType : Any> {
    fun resolveKey(request: RequestType): Serializable
}
