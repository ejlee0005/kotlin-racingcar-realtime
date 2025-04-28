import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Car
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    val carNames = listOf("car1", "car2", "car3")
    val goal = 5
    val cars = carNames.map(::Car)
    val race = Race(cars, goal)
    race.start()
}

