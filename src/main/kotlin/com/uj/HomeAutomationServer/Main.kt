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

            //Get all flows, grouped by the sensor capability (so that one REST call will satisfy them all)
            val flowsGroupedBySensorCapabilityId = flowRepository.findAll().groupBy { it.sensorCapability.id }

            //For each group of flows with the same sensor capability trigger...
            flowsGroupedBySensorCapabilityId.map { list ->

                //Get the first flow
                val firstFlow = list.value.first()

                println("Reading sensor value")

                //Async request the value from the sensor
                val future = asyncRest.getForEntity(
                        buildUrl(firstFlow.sensor.endpointRoot, firstFlow.sensorCapability.endpointUrl),
                        CapabilityResponse::class.java
                )

                //Associate each of the flows in the group with the same single future instance
                list.value.map { flow ->
                    flowExecutions.add(FlowExecutionHelper(flow, future))
                }
            }

            //For each flow/future association...
            flowExecutions.map {

                //Get the response value from the future
                val response = it.future.get().body as CapabilityResponse
//                println("=== Execution ===")
//                println("Execution Response: $response")

                //Check if flow conditions were met
                val sensorConditionMet = when (it.flow.sensorMoreThan) {
                    true -> response.value.toDouble() >= it.flow.sensorValue
                    false -> response.value.toDouble() < it.flow.sensorValue
                }

                //Trigger positive actuation
                if (sensorConditionMet && !it.flow.wasMet) {
                    triggerActuation(it.flow, true)
                } else if (!sensorConditionMet && it.flow.wasMet) {
                    triggerActuation(it.flow, false)
                }

                println()
            }
        }
    }

//    private fun checkSensorValues(flowsGroupedBySensorCapabilityId: Map<Long, List<Flow>>): ArrayList<FlowExecutionHelper> {
//        val flowExecutions = ArrayList<FlowExecutionHelper>()
//
//        flowsGroupedBySensorCapabilityId.map { list ->
//
//            //Get the first flow
//            val firstFlow = list.value.first()
//
//            println("Reading sensor value")
//
//            //Async request the value from the sensor
//            val future = asyncRest.getForEntity(
//                    buildUrl(firstFlow.sensor.endpointRoot, firstFlow.sensorCapability.endpointUrl),
//                    CapabilityResponse::class.java
//            )
//
//            //Associate each of the flows in the group with the same single future instance
//            list.value.map { flow ->
//                flowExecutions.add(FlowExecutionHelper(flow, future))
//            }
//        }
//
//        return flowExecutions
//    }

    private fun triggerActuation(flow: Flow, flowMet: Boolean) {

        val capabilityToTrigger = if(flowMet) flow.actuatorCapabilityIfSensorValMet else flow.actuatorCapabilityIfSensorValNotMet

        asyncRest.getForEntity(
                buildUrl(flow.actuator.endpointRoot, capabilityToTrigger.endpointUrl),
                String::class.java //For an actuation, we don't care about the response (at this point in the system's development)
        )

        flow.wasMet = flowMet
        flowRepository.save(flow)
    }

    private fun buildUrl(rootUrl: String, capabilityUrl: String) = "http://$rootUrl.local/arduino/$capabilityUrl"

    private data class FlowExecutionHelper(val flow: Flow, val future: ListenableFuture<ResponseEntity<CapabilityResponse>>)
}