package machine

import java.util.*
import kotlin.math.max

/**
 * Single coffee beverage representation.
 */
data class Coffee(val title: String,
                  val mlOfWater: Int,
                  val mlOfMilk: Int,
                  val grOfCoffeeBeans: Int,
                  val price: Int)

/**
 * Coffee machine available resources representation
 */
data class CoffeeMachineResources(val mlOfWaterAvailable: Int = 0,
                                  val mlOfMilkAvailable: Int = 0,
                                  val grOfCoffeeBeansAvailable: Int = 0,
                                  val disposableCupsAvailable: Int = 0,
                                  val moneyCollected: Int = 0) {

    operator fun plus(increment: CoffeeMachineResources): CoffeeMachineResources {
        return CoffeeMachineResources(mlOfWaterAvailable + increment.mlOfWaterAvailable,
                mlOfMilkAvailable + increment.mlOfMilkAvailable,
                grOfCoffeeBeansAvailable + increment.grOfCoffeeBeansAvailable,
                disposableCupsAvailable + increment.disposableCupsAvailable,
                moneyCollected + increment.moneyCollected)
    }

    operator fun minus(coffee: Coffee): CoffeeMachineResources {
        return CoffeeMachineResources(mlOfWaterAvailable - coffee.mlOfWater,
                mlOfMilkAvailable - coffee.mlOfMilk,
                grOfCoffeeBeansAvailable - coffee.grOfCoffeeBeans,
                disposableCupsAvailable - 1,
                moneyCollected + coffee.price)
    }
}

/**
 * General commands accepted by the {@link CoffeeMachine}.
 */
enum class Command(val rawValue: String) {
    BUY("buy"),
    FILL("fill"),
    TAKE("take"),
    REMAINING("remaining"),
    EXIT("exit"),
    UNKNOWN("unknown");

    companion object {
        fun fromString(value: String) = when (value) {
            BUY.rawValue -> BUY
            FILL.rawValue -> FILL
            TAKE.rawValue -> TAKE
            REMAINING.rawValue -> REMAINING
            EXIT.rawValue -> EXIT
            else -> UNKNOWN
        }

        fun getCommandsAsString() = "(${values()
                .dropLast(1)
                .map { it.rawValue }
                .joinToString(", ")})"
    }
}

sealed class CoffeeMachineState {
    /**
     * Terminated. No option to turn it back on :)
     */
    object TERMINATED : CoffeeMachineState() {
        override fun produceState(command: String) = TERMINATED
    }

    /**
     * Waiting for any starting command to be given:
     * ("buy", "fill", "take", "remaining", "exit")
     */
    object IDLE : CoffeeMachineState()

    /**
     * Accepted "buy" command. Waiting for drink selection.
     */
    object SELECT_DRINK : CoffeeMachineState() {
        override fun produceState(command: String): CoffeeMachineState {
            if (command == "back") {
                return IDLE
            }

            val drinkCode = command.toIntOrNull() ?: 0
            if (drinkCode == 0) {
                return IDLE
            }

            return DRINK_PREPARATION(drinkCode - 1)
        }
    }

    class DRINK_PREPARATION(val selectedDrink: Int) : CoffeeMachineState() {
        override fun produceState(command: String) = IDLE
    }

    object EXTRACTING_MONEY : CoffeeMachineState()
    object PRINTING_STATE : CoffeeMachineState()

    /**
     * Waiting for the user input of added water amount in milliliters.
     */
    object FILL_RESOURCES_WATER : CoffeeMachineState() {
        override fun produceState(command: String) =
                FILL_RESOURCES_MILK(
                        CoffeeMachineResources(mlOfWaterAvailable = max(0, command.toIntOrNull() ?: 0))
                )
    }

    /**
     * Waiting for the user input of added milk amount in milliliters.
     */
    class FILL_RESOURCES_MILK(val resources: CoffeeMachineResources) : CoffeeMachineState() {
        override fun produceState(command: String) =
                FILL_RESOURCES_COFFEE_BEANS(
                        resources.copy(mlOfMilkAvailable = max(0, command.toIntOrNull() ?: 0))
                )
    }

    /**
     * Waiting for the user input of added coffee beans amount in grams.
     */
    class FILL_RESOURCES_COFFEE_BEANS(val resources: CoffeeMachineResources) : CoffeeMachineState() {
        override fun produceState(command: String) =
                FILL_RESOURCES_DISPOSABLE_CUPS(
                        resources.copy(grOfCoffeeBeansAvailable = max(0, command.toIntOrNull() ?: 0))
                )
    }

    /**
     * Waiting for the user input of added disposable cups amount in units.
     */
    class FILL_RESOURCES_DISPOSABLE_CUPS(val resources: CoffeeMachineResources) : CoffeeMachineState() {
        override fun produceState(command: String) =
                FILL_RESOURCES_APPLY(
                        resources.copy(disposableCupsAvailable = max(0, command.toIntOrNull() ?: 0))
                )
    }

    /**
     * Supplied the machine, now must recalculate total resources.
     */
    class FILL_RESOURCES_APPLY(val resources: CoffeeMachineResources) : CoffeeMachineState() {
        override fun produceState(command: String) = IDLE
    }

    /**
     * Each individual state can produce new state.
     *
     * Example 1:
     * FILL_RESOURCES_WATER produces FILL_RESOURCES_MILK as the next state,
     * FILL_RESOURCES_MILK produces FILL_RESOURCES_COFFEE_BEANS, etc. until
     * FILL_RESOURCES_APPLY which indicates ending of filling process and requires
     * recalculating total resources.
     *
     * Example 2:
     * SELECT_DRINK produces DRINK_PREPARATION state when a drink was correctly selected.
     */
    open fun produceState(command: String = "") = when (Command.fromString(command)) {
        Command.BUY -> SELECT_DRINK
        Command.FILL -> FILL_RESOURCES_WATER
        Command.TAKE -> EXTRACTING_MONEY
        Command.REMAINING -> PRINTING_STATE
        Command.EXIT -> TERMINATED
        Command.UNKNOWN -> IDLE
    }
}

class IntelliJCoffeeMachine(resources: CoffeeMachineResources, coffeeArray: Array<Coffee>) {

    /**
     * A list of beverages this coffee machine was programmed to make.
     * ☕ Smells good!
     */
    private val coffeeBeverages: Array<Coffee>
    private var resources: CoffeeMachineResources
    private var state: CoffeeMachineState = CoffeeMachineState.IDLE
        set(value) {
            field = value
            messageOf(value).also { if (!it.isBlank()) println(it) }
        }

    init {
        this.resources = resources
        coffeeBeverages = coffeeArray

        // To produce first output message of CoffeeMachineState.IDLE state.
        messageOf(state).also { if (!it.isBlank()) println(it) }
    }

    private fun messageOf(state: CoffeeMachineState): String {
        return when (state) {
            CoffeeMachineState.IDLE -> "Write action ${Command.getCommandsAsString()}:"
            CoffeeMachineState.SELECT_DRINK -> "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu"
            CoffeeMachineState.FILL_RESOURCES_WATER -> "Write how many ml of water do you want to add:"
            is CoffeeMachineState.FILL_RESOURCES_MILK -> "Write how many ml of milk do you want to add:"
            is CoffeeMachineState.FILL_RESOURCES_COFFEE_BEANS -> "Write how many grams of coffee beans do you want to add:"
            is CoffeeMachineState.FILL_RESOURCES_DISPOSABLE_CUPS -> "Write how many disposable cups of coffee do you want to add:"
            else -> ""
        }
    }

    /**
     * returns `true` is machine is terminated, `false` otherwise
     */
    fun accept(command: String): Boolean {
        var newState = state.produceState(command)

        when (newState) {
            is CoffeeMachineState.DRINK_PREPARATION -> {
                val coffee = coffeeBeverages[newState.selectedDrink]
                if (canPrepare(coffee)) {
                    prepareBeverage(coffee)
                }
                newState = newState.produceState()
            }
            CoffeeMachineState.EXTRACTING_MONEY -> {
                giveTheMoney()
                newState = newState.produceState()
            }
            CoffeeMachineState.PRINTING_STATE -> {
                printState()
                newState = newState.produceState()
            }
            is CoffeeMachineState.FILL_RESOURCES_APPLY -> {
                applyResources(newState.resources)
                newState = newState.produceState()
            }
        }

        state = newState

        return newState == CoffeeMachineState.TERMINATED
    }

    private fun applyResources(resources: CoffeeMachineResources) {
        this.resources = this.resources + resources
    }

    /**
     * SHOW HIM THE MONEY!
     */
    private fun giveTheMoney() {
        println("I gave you $${resources.moneyCollected}")
        resources = resources.copy(moneyCollected = 0)
    }

    private fun printState() {
        println("""
        The coffee machine has:
        ${resources.mlOfWaterAvailable} of water
        ${resources.mlOfMilkAvailable} of milk
        ${resources.grOfCoffeeBeansAvailable} of coffee beans
        ${resources.disposableCupsAvailable} of disposable cups
        ${resources.moneyCollected} of money
    """.trimIndent())
    }

    private fun canPrepare(selectedBeverage: Coffee): Boolean {
        //water, milk, coffee, disposable
        with(resources) {
            return when {
                mlOfWaterAvailable < selectedBeverage.mlOfWater -> {
                    println("Sorry, not enough water!")
                    false
                }
                mlOfMilkAvailable < selectedBeverage.mlOfMilk -> {
                    println("Sorry, not enough milk!")
                    false
                }
                grOfCoffeeBeansAvailable < selectedBeverage.grOfCoffeeBeans -> {
                    println("Sorry, not enough coffee beans!")
                    false
                }
                disposableCupsAvailable < 1 -> {
                    println("Sorry, not enough disposable cups!")
                    false
                }
                else -> true
            }
        }
    }

    private fun prepareBeverage(coffee: Coffee) {
        println("I have enough resources, making you a coffee!")
        resources = resources - coffee
    }
}

fun main() {
    val scanner = Scanner(System.`in`)

    val theCoffeeMachine = IntelliJCoffeeMachine(
            CoffeeMachineResources(400, 540,
                    120, 9, 550),
            arrayOf(Coffee("espresso", 250, 0, 16, 4),
                    Coffee("latte", 350, 75, 20, 7),
                    Coffee("cappuccino", 200, 100, 12, 6)))

    var isTerminated = false
    while (!isTerminated) {
        isTerminated = theCoffeeMachine.accept(scanner.next())
    }
}