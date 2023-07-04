package ru.antonmarin.delayedtasks.api.schedule

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.delayedtasks.api.HashCodeIdempotent
import ru.antonmarin.delayedtasks.api.Task
import ru.antonmarin.delayedtasks.api.TasksRegistry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class InstanceScheduleTest {
    private val sutTask = mockk<Task<SUTRequest>> {
        every { execute(any()) } just runs
    }
    class SUTRequest : HashCodeIdempotent()

    private val tasksRegistry: TasksRegistry = mockk{
        every { getRequestTask(any<KClass<SUTRequest>>()) } returns sutTask
    }
    private val schedule = InstanceSchedule(tasksRegistry)

    @Test
    fun `should execute task at once when requested without delay`() {
        var executedWithRequest: SUTRequest? = null
        val isExecuted = CountDownLatch(1)
        every { sutTask.execute(any()) } answers {
            executedWithRequest = firstArg()
            isExecuted.countDown()
        }

        val scheduledRequest = SUTRequest()
        schedule.run(scheduledRequest)
        isExecuted.await(10, TimeUnit.MILLISECONDS)

        Assertions.assertEquals(scheduledRequest, executedWithRequest)
    }

    @Test
    fun `should not await for task execution finish`() {
        val initialCounterValue = 1
        val isNotExecuted = CountDownLatch(initialCounterValue)
        val taskExecutionDuration: Long = 1000
        every { sutTask.execute(any()) } answers {
            Thread.sleep(taskExecutionDuration-100) // -100 to execute lt await
            isNotExecuted.countDown()
        }

        schedule.run(SUTRequest())

        Assertions.assertEquals(initialCounterValue.toLong(), isNotExecuted.count)
        Assertions.assertTrue(isNotExecuted.await(taskExecutionDuration, TimeUnit.MILLISECONDS))
    }

    // what if no more threads available?
}
