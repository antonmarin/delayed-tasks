package ru.antonmarin.delayedtasks.api.key

import java.io.Serializable

class HashCodeResolver : KeyResolver<Any> {
    override fun resolveKey(request: Any): Serializable = request.hashCode()
}
