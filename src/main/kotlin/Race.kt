import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Car
import kotlin.collections.map

class Race(
    val cars: List<Car>,
    val goal: Int,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private lateinit var jobs: List<Job>

    suspend fun start() {   // 자동차들을 코루틴으로 만들어서 실행시킴. coroutineScope: 구조화된 동시성을 제공함.
//        coroutineScope {
//            jobs = cars.map {      // <구조화된 동시성을 위해 자동차들을 Job으로 만들기>
//                launch { move(it) }
//            }
//        }

        val jobs = cars.map {
            scope.launch { move(it) }
        }
        jobs.joinAll()
    }

    private suspend fun move(car: Car) {
        while (isActive && car.position < goal) {
            car.move()
            if (car.position == goal) {
                withContext(Dispatchers.IO) {
                    println("${car.name}가 최종 우승했습니다.")
                }
                // jobs.map { it.cancel() }    // jobs.forEach { it.cancel() }
                scope.cancel()
            }
        }
    }
}