package ru.antonmarin.delayedtasks.api

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.antonmarin.delayedtasks.api.key.KeyResolver
import ru.antonmarin.delayedtasks.api.storage.Storage
import ru.antonmarin.delayedtasks.api.storage.TaskRequestRecord
import java.io.Serializable
import java.util.*

class TasksScheduleTest {
    private val capturedRequestRecord = slot<TaskRequestRecord>()
    private val storage: Storage = mockk {
        every { save(capture(capturedRequestRecord)) } returns Unit
    }
    private val schedule = TasksSchedule(storage, mapOf(TestTask.Request::class to TestTask.TestKeyResolver()))

    @BeforeEach
    fun resetCaptures() {
        capturedRequestRecord.clear()
    }

    @Nested
    inner class ScheduleTest {
        @Test
        fun `should store request with hashCode key when no key resolver registered for request`() {
            val request = TestTask.Request(UUID.randomUUID())
            val schedule = TasksSchedule(storage)

            schedule.schedule(request)

            assertEquals(
                TaskRequestRecord(
                    key = request.hashCode(),
                    request = request,
                ),
                capturedRequestRecord.captured
            )
        }

        @Test
        fun `should store request with key from resolver when custom key resolver registered`() {
            val request = TestTask.Request(UUID.randomUUID())

            schedule.schedule(request)

            assertEquals(
                TaskRequestRecord(
                    key = request.entityId,
                    request = request,
                ),
                capturedRequestRecord.captured
            )
        }
    }

    class TestTask {
        data class Request(
            val entityId: UUID
        )

        class TestKeyResolver : KeyResolver<Request> {
            override fun resolveKey(request: Request): Serializable = request.entityId
        }
    }
}
