package controller

import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.isActive
import model.Car
import view.RacingView

class RacingController(
    private val racingView: RacingView,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    var cars: List<Car> = emptyList()
    var goal: Int = 0

    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    fun initRacing() {
        cars = racingView.nameInputView().map { Car(it) }
        goal = racingView.distanceInputView()
    }

    suspend fun startRacing() {
        cars
            .map {
                scope.launch { move(it) }
            }.joinAll()
    }

    private suspend fun move(car: Car) {
        while (isActive && car.position < goal) {
            yield() // while 이 종료되는 시점이 car.move 내부의 delay 에 걸리기 전에 종료
            car.move()
            racingView.positionView(car)
            if (car.position == goal) {
                racingView.resultView(car)
                scope.cancel()
            }
        }
    }
}