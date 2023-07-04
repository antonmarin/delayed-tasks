package ru.antonmarin.delayedtasks.api

import org.slf4j.LoggerFactory

interface Task<in R : TaskRequest> {
    fun execute(request: R)
}

object UnexpectedRequestTask : Task<TaskRequest> {
    override fun execute(request: TaskRequest) {
        logger.error(
            "Received unexpected task request of type {}. Register executing task in TasksRegistry",
            request::class.java
        )
    }

    private val logger = LoggerFactory.getLogger(UnexpectedRequestTask::class.java)
}

interface TaskRequest {
    fun getIdempotencyKey(): String
}

abstract class HashCodeIdempotent : TaskRequest {
    override fun getIdempotencyKey(): String = hashCode().toString()
}
