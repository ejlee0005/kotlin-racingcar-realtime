package racingcar

import kotlinx.coroutines.runBlocking
import racingcar.controller.RacingController
import racingcar.model.Car
import racingcar.view.RacingView

/**
 * pair programming
 */
fun main() =
    runBlocking {
        val racingView = RacingView()
        val cars = racingView.nameInputView().map { Car(it) }.toMutableList()
        val goal = racingView.distanceInputView()
        val racingController = RacingController(cars, goal, racingView)
        racingController.start()
    }
