package com.thehecklers.brpprocessorkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.SendTo
import reactor.core.publisher.Flux
import java.util.function.Function
import kotlin.random.Random

@SpringBootApplication
class BrpProcessorKotlinApplication

fun main(args: Array<String>) {
    runApplication<BrpProcessorKotlinApplication>(*args)
}

@Configuration
//@EnableBinding(Processor::class)
class GateAgent {
    val rnd = Random

    @Bean
//    MH: Not yet available, but coming soon
//    fun greetPassenger(): (Flux<Passenger>) -> Flux<FlyingPassenger> =
//        {
    fun greetPassenger(): Function<Flux<Passenger>, Flux<FlyingPassenger>> =
        Function {
            it.map {
                val fPax = FlyingPassenger(
                    it.id,
                    it.name,
                    if (rnd.nextInt(2) == 0)
                        FlyingPassenger.State.VALUED_PASSENGER
                    else
                        FlyingPassenger.State.PREMIUM_PASSENGER
                )

                println(fPax)

                fPax
            }
        }

/*  // MH: Example without Publishers...
    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    fun greetPassenger(passenger: Passenger): FlyingPassenger {
        val flyingPassenger = FlyingPassenger(
            passenger.id,
            passenger.name,
            if (Random.nextInt(2) == 0)
                FlyingPassenger.State.VALUED_PASSENGER
            else
                FlyingPassenger.State.PREMIUM_PASSENGER
        )

        println(flyingPassenger)

        return flyingPassenger
    }
*/
}

data class FlyingPassenger(val id: String, val name: String, val state: State) {
    enum class State {
        VALUED_PASSENGER,
        PREMIUM_PASSENGER
    }
}

data class Passenger(val id: String, val name: String)