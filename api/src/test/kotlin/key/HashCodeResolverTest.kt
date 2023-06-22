package ru.antonmarin.delayedtasks.api.key

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HashCodeResolverTest {
    private val resolver = HashCodeResolver()

    @Test
    fun `should use hashCode as key`() {
        val request = object {}

        val key = resolver.resolveKey(request)

        Assertions.assertEquals(request.hashCode(), key)
    }

    @Test
    fun `should same key when several calls`() {
        val request = object {}

        Assertions.assertEquals(resolver.resolveKey(request), resolver.resolveKey(request))
    }
}
