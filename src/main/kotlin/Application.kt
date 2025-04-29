import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel
import model.Car

fun main() {
    runBlocking {
        val carNames = listOf("car1", "car2", "car3")
        val goal = 5
        val cars = carNames.map(::Car)
        val channel = Channel<Car>(Channel.UNLIMITED)
        val race = Race(cars, goal, channel)
        race.start()
    }
}
