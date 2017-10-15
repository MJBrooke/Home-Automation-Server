package com.uj.HomeAutomationServer

import com.uj.HomeAutomationServer.entity.CapabilityResponse
import com.uj.HomeAutomationServer.entity.Flow
import com.uj.HomeAutomationServer.entity.FlowRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.client.AsyncRestTemplate

@Component
class Main(val flowRepository: FlowRepository, val asyncRest: AsyncRestTemplate) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        while (true) {
            val flowExecutions = ArrayList<FlowExecutionHelper>()

            flowRepository.findAll().map {
                println("=== Repository ===")
                println("Flow: $it")

                val future = asyncRest.getForEntity(
                        "http://${it.sensor.endpointRoot}.local/arduino/${it.sensorCapability.endpointUrl}",
                        CapabilityResponse::class.java
                )

                flowExecutions.add(FlowExecutionHelper(it, future))
            }

            flowExecutions.map {
                val response = it.future.get().body as CapabilityResponse
                println("=== Execution ===")
                println("Execution Response: $response")

                if (response.value.toDouble() >= it.flow.sensorValue) {
                    asyncRest.getForEntity(
                            "http://${it.flow.actuator.endpointRoot}.local/arduino/${it.flow.actuatorCapability.endpointUrl}",
                            String::class.java
                    )
                }

                println()
            }
        }
    }

    private data class FlowExecutionHelper(val flow: Flow, val future: ListenableFuture<ResponseEntity<CapabilityResponse>>)
}