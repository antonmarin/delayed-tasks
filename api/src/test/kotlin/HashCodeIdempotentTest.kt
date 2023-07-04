package ru.antonmarin.delayedtasks.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HashCodeResolverTest {
    @Test
    fun `should use hashCode as key`() {
        val request = object : HashCodeIdempotent() {}

        val key = request.getIdempotencyKey()

        Assertions.assertEquals(request.hashCode().toString(), key)
    }

    @Test
    fun `should same key when several calls`() {
        val request = object : HashCodeIdempotent(){}

        Assertions.assertEquals(request.getIdempotencyKey(), request.getIdempotencyKey())
    }
}
