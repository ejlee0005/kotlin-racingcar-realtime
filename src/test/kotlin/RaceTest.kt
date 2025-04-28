import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import model.Car
import kotlin.test.Test

class RaceTest {

    @Test
    fun name() = runTest {
        // given
        val goal = 5

        val cars = listOf(
            Car("car1", 0),
            Car("car2", 0),
            Car("car3", goal - 1)
        )

        val dispatcher = StandardTestDispatcher(testScheduler)
        val race = Race(cars = cars, goal = goal, dispatcher)

        // when
        race.start()

//        // then 1
//        race.cars.firstOrNull { it.position == goal }.shouldNotBeNull()

        // then 2
        val winner =  race.cars.firstOrNull { it.position == goal }
        winner?.name shouldBe "car3"
    }

}