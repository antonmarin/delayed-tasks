package ru.antonmarin.delayedtasks.api

import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UnexpectedRequestTaskTest {
    @Test
    fun `should receive any subtype request`() {
        val request = mockk<TaskRequest>()

        Assertions.assertDoesNotThrow {
            UnexpectedRequestTask.execute(request)
        }
    }
}
