package ru.antonmarin.delayedtasks.api

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TasksRegistryTest {
    @Test
    fun `should return UnexpectedRequestTask when no task registered for request type`() {
        val registry = TasksRegistry(emptyMap())

        val task = registry.getRequestTask(SUTTask.Request::class)

        Assertions.assertEquals(UnexpectedRequestTask, task)
    }

    @Test
    fun `should return matched task`() {
        val expectedTask = SUTTask()
        val registry = TasksRegistry(mapOf(SUTTask.Request::class to expectedTask))

        val task = registry.getRequestTask(SUTTask.Request::class)

        Assertions.assertEquals(expectedTask, task)
    }

    private class SUTTask : Task<SUTTask.Request> {
        override fun execute(request: Request) {
            /* some code */
        }

        class Request : HashCodeIdempotent()
    }
}
