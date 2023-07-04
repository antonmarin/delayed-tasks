# Delayed tasks

Library to run tasks(or jobs if you prefer)

### Terms

* Task - endpoint starting business logic, similar to controller
* Request - task input
* Schedule - strategy of scheduling requested tasks (Instance/Distributed)

## Usage

The simplest case:

```kotlin
// create task class
class ScheduleCalculations : Task<ScheduleCalculations.Request> {
  override fun execute(request: Request) {/* some code here*/}
  class Request : HashCodeIdempotent() // or implement TaskRequest to support own request idempotency key
}
// select task executing request  
val tasksRegistry = TasksRegistry(mapOf(ScheduleCalculations.Request::class to ScheduleCalculations()))
// select schedule. Instance or Distributed
val tasksSchedule = InstanceSchedule(tasksRegistry = tasksRegistry)
// run at once task execution with request using api
tasksSchedule.run(ScheduleCalculations.Request()) // also runAt() and runAfter() available

// Optional: to change idempotency key when expecting scheduling multiple tasks same time
// implement TaskRequest interface instead extending HashCodeIdempotent 
class Request(val id: UUID) : TaskRequest {
  fun getIdempotencyKey(): String = id.toString()
}
```

### Components

- platform to sync components version `implementation(platform("ru.antonmarin.delayed-tasks:platform:1.5.8"))`
  when multiple required. [docs](https://docs.gradle.org/current/userguide/dependency_version_alignment.html#aligning_versions_natively_with_gradle)
- api(ru.antonmarin.delayed-tasks:api): to schedule programmatically
- periodic(ru.antonmarin.delayed-tasks:periodic): to run tasks periodically
- control-plane(ru.antonmarin.delayed-tasks:control-plane): to observe registered tasks
- spring(ru.antonmarin.delayed-tasks:spring): to integrate with spring
- test(ru.antonmarin.delayed-tasks:test): utilities to test integration with library
- annotations: supports configuration using annotations

### Retries and configurable delay

```kotlin
import java.time.Duration
import java.time.temporal.ChronoUnit

class TasksConfiguration {
    fun config(registry: TasksRegistry) {
        val scheduleCalculations = ScheduleCalculations()
        registry.add(scheduleCalculations, TaskConfig(
            RetryConfig(
                maxRetriesCount = 10,
                delayOnFailStrategy = fixedDelay,
            ),
        ))
    }
    val fixedDelay = Fixed(Duration.parse("PT1M"))
    val linearIncreasingDelay = Linear(Duration.parse("PT1M"))
    val fibonacciIncreasingSeconds = Fibonacci(ChronoUnit.SECONDS)
}
```

### Run at exact time

```kotlin
// at exact time
tasksSchedule.schedule(request, Delay.at(Instant.parse("2023-02-13T14:03:12Z")))
// after some duration
tasksSchedule.schedule(request, Delay.duration(Duration.ofHours(1)))
// right now
tasksSchedule.schedule(request, Delay.now)
```

### Run periodically

Configure distributive locks
```kotlin
    val lockProvider = MemoryLockProvider()
    TasksSchedule(storage, metrics, tracing, lockProvider)
```

```kotlin
    // with cron syntax
    TaskConfig(
        RetryConfig(
          maxRetriesCount = 10,
          delayOnFailStrategy = fixedDelay,
        ),
        Cron("* * * * *")
    )
    // with duration
    TaskConfig(
      RetryConfig(
        maxRetriesCount = 10,
        delayOnFailStrategy = fixedDelay,
      ),
      Duration("PT24H")
    )
```
_Period counts from previous start_

### Cancel task

```kotlin
taskSchedule.cancel(Request::class, "taskKey")
```

### Replace task

```kotlin
taskSchedule.replace(request, Delay.at(Instant.now))
```

### Get scheduled tasks

```kotlin
taskSchedule.findAll()
taskSchedule.findAll(Request::class)
```

### Control plane

- forthcoming tasks
- registered tasks
- ability to execute task manually

### File Configuration

```kotlin
class FileConfiguration (
    val enabled: Boolean, // enables delayed tasks mechanism
)
class TasksSchedule(
    config: TasksConfiguration,
    val storage: TasksStorage, // selects storage for tasks
    val lockProvider: LockProvider, // selects distributive locks provider
    val tracing: Tracer, // selects tracing lib opentracing supported
    val metrics: MetricsProvider, // selects metrics lib opentracing supported
)
//todo decide class or values here
```
- количество потоков выполнения? (чтобы масштабировать когда планируется много задач)

### Annotations config

```kotlin
@DelayedTask
class ScheduleCalculations : Task<Request> {
  fun execute(request: Request) {}
}
@ScheduledTask(Cron("* * * * *"))
class ScheduleCalculations : Task<Request> {
    fun execute(request: Request) {}
}
@ScheduledTask(Duration("PT24H"))
class ScheduleCalculations : Task<Request> {
  fun execute(request: Request) {}
}
```

### Supported storages

- postgresql PostgreSQLStorage
- memory MemoryStorage

### Metrics

- successful finish count
- failed finish count
- mean tasks execution duration
- завершенные за retentionTime/retentionCount с результатом завершения (и ошибкой?)

### Tracing

[OpenTelemetry](https://opentelemetry.io/) used. 
Visit [Getting started](https://opentelemetry.io/docs/instrumentation/java/) to start collecting

```kotlin
SomeTracingProvider()
```

### Storage

Configure tasks storage
```kotlin
PostgreSQLStorage()
```
Available
- MemoryStorage
- PostgreSQLStorage (sql to create table included)

or implement new with TasksStorage

### Spring support

- DataSource as TasksStorage
- Autoconfiguration and configuration using file
- ```yaml
tasks:
  enabled: true
```

### Suspendable tasks

```kotlin
class ScheduleCalculations : SuspendableTask<Request> {
    suspend fun execute(request: Request) {}
}
```

Notes:
Uses internally java.util.Timer and java.util.TimerTask 
