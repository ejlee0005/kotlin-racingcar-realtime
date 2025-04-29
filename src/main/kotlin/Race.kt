import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import model.Car
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.coroutineContext

class Race(
    cars: List<Car>,
    val goal: Int,
    private val channel: Channel<Car> = Channel(Channel.UNLIMITED),
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val _cars: MutableList<Car> = cars.toMutableList()
    val cars: List<Car>
        get() = _cars.toList()

    private val scope: CoroutineScope = CoroutineScope(dispatcher + SupervisorJob())
    val isPaused: AtomicBoolean = AtomicBoolean(false)

    suspend fun start() {
        launchRace()
        launchInput()
        monitorRace()
    }

    private fun launchRace() {
        _cars.forEach { car ->
            scope.launch { move(car) }
        }
    }

    private fun launchInput() {
        scope.launch {
            while (isActive) {
                val input = readlnOrNull()
                if (input != null) {
                    pauseRace()
                    val command = "add car4"
                    val carName = command.split(" ")[1]
                    channel.send(Car(carName))
                    resumeRace()
                }
            }
        }
    }

    private suspend fun monitorRace() {
        while (coroutineContext.isActive) {
            while (!channel.isEmpty) {
                val newCar = channel.receive()
                println("${newCar.name} 참가 완료!")
                _cars.add(newCar)
                scope.launch { move(newCar) }
            }
        }
    }

    private fun pauseRace() {
        isPaused.set(true)
    }

    private fun resumeRace() {
        isPaused.set(false)
    }

    private suspend fun move(car: Car) {
        while (coroutineContext.isActive && car.position < goal) {
            if (!isPaused.get()) {
                car.move()
                car.checkWinner()
            }
        }
    }

    private fun Car.checkWinner() {
        if (position == goal) {
            println("${name}가 최종 우승했습니다.")
            scope.cancel()
        }
    }
}