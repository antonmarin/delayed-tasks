package ru.antonmarin.delayedtasks.api.schedule

import ru.antonmarin.delayedtasks.api.TaskRequest
import ru.antonmarin.delayedtasks.api.TasksRegistry
import kotlin.concurrent.thread

/**
 * Schedule tasks only inside application instance
 */
class InstanceSchedule(
    private val tasksRegistry: TasksRegistry,
) {
    /**
     * Runs corresponding task at once.
     *
     * Executes in separated thread. No idea what happens when no thread available now
     */
    fun run(request: TaskRequest) {
        val kClass = request::class
        val task = tasksRegistry.getRequestTask(kClass)

        thread(name = "task-${task::class.qualifiedName}-${request.getIdempotencyKey()}") {
            task.execute(request)
        }
    }
}
