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

            //Get all currently inactive flows
            flowRepository.findByActiveFalse().map {
                println("=== Repository ===")
                println("Flow: ${it.name}")

                //Request sensor values for the flow, wrapping it in a future
                val future = asyncRest.getForEntity(
                        buildUrl(it.sensor.endpointRoot, it.sensorCapability.endpointUrl),
                        CapabilityResponse::class.java
                )

                //Associate the future with the flow that it originated from
                flowExecutions.add(FlowExecutionHelper(it, future))
            }

            //For each flow,
            flowExecutions.map {
                val response = it.future.get().body as CapabilityResponse
                println("=== Execution ===")
                println("Execution Response: $response")

                val shouldActuate = when(it.flow.sensorMoreThan) {
                    true -> response.value.toDouble() >= it.flow.sensorValue
                    false -> response.value.toDouble() < it.flow.sensorValue
                }

                if (shouldActuate) {
                    asyncRest.getForEntity(
                            buildUrl(it.flow.actuator.endpointRoot, it.flow.actuatorCapability.endpointUrl),
                            String::class.java //For an actuation, we don't care about the response (at this point in the system's development)
                    )

                    //Update all other flows that have the same actuator as inactive (so that they can be checked against)
                    flowRepository.findByActuatorId(it.flow.actuator.id).map { impactedFlow ->
                        impactedFlow.active = false
                        flowRepository.save(impactedFlow)
                    }

                    //Update this flow to reflect as active (should not be checked against any further)
                    it.flow.active = true
                    flowRepository.save(it.flow)
                }

                println()
            }
        }
    }

    private fun buildUrl(rootUrl: String, capabilityUrl: String) = "http://$rootUrl.local/arduino/$capabilityUrl"

    private data class FlowExecutionHelper(val flow: Flow, val future: ListenableFuture<ResponseEntity<CapabilityResponse>>)
}