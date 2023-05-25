import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.antonmarin.delayedtasks.core.Storage
import ru.antonmarin.delayedtasks.core.TasksSchedule

class TasksScheduleTest {
    private val capturedRequest = slot<Any>()
    private val storage: Storage = mockk {

    }
    private val schedule = TasksSchedule(storage)

    @Test
    fun `should store request`() {

    }
}
