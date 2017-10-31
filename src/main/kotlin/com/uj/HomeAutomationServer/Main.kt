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
            val flowsGroupedBySensorCapabilityId = getFlowsGroupedBySensorCapabilityId()

            val flowsWithSensorValueFutures = getSensorValuesForEachFlow(flowsGroupedBySensorCapabilityId)

            triggerActuations(flowsWithSensorValueFutures)
        }
    }

    private fun getFlowsGroupedBySensorCapabilityId() = flowRepository.findAll().groupBy { it.sensorCapability.id }

    private fun getSensorValuesForEachFlow(flowsGroupedBySensorCapabilityId: Map<Long, List<Flow>>): ArrayList<FlowExecutionHelper> {
        val flowExecutions = ArrayList<FlowExecutionHelper>()

        flowsGroupedBySensorCapabilityId.map { flowMapEntry ->

            //Get the first flow
            val firstFlow = flowMapEntry.value.first()

            println("Reading sensor value from ${firstFlow.sensor.name}'s '${firstFlow.sensorCapability.name}' capability")

            //Async request the value from the sensor
            val future = asyncRest.getForEntity(
                    buildUrl(firstFlow.sensor.endpointRoot, firstFlow.sensorCapability.endpointUrl),
                    CapabilityResponse::class.java
            )

            //Associate each of the flows in the group with the same single future instance
            flowMapEntry.value.map { flow ->
                flowExecutions.add(FlowExecutionHelper(flow, future))
            }
        }

        return flowExecutions
    }

    private fun triggerActuations(flowExecutions: ArrayList<FlowExecutionHelper>) {
        val futures = ArrayList<ListenableFuture<ResponseEntity<String>>>()

        flowExecutions.map {

            //Get the response value from the future
            val sensorResponse = it.future.get().body as CapabilityResponse

            //Check if flow conditions were met
            val sensorConditionMet = when (it.flow.sensorMoreThan) {
                true -> sensorResponse.value.toDouble() >= it.flow.sensorValue
                false -> sensorResponse.value.toDouble() < it.flow.sensorValue
            }

            when {
                sensorConditionMet && !it.flow.wasMet -> futures.add(triggerActuation(it.flow, true))
                !sensorConditionMet && it.flow.wasMet -> futures.add(triggerActuation(it.flow, false))
                else -> {}
            }
        }

        //Force all actuations to complete before checking further sensor values
        futures.map { it.get() }

        if (flowExecutions.isNotEmpty())
            println()
    }

    private fun triggerActuation(flow: Flow, flowMet: Boolean): ListenableFuture<ResponseEntity<String>> {
        val capabilityToTrigger = if (flowMet) flow.actuatorCapabilityIfSensorValMet else flow.actuatorCapabilityIfSensorValNotMet

        println("\tTriggering the '${capabilityToTrigger.name}' capability on the ${flow.actuator.name} actuator")

        flow.wasMet = flowMet
        flowRepository.save(flow)

        return asyncRest.getForEntity(
                buildUrl(flow.actuator.endpointRoot, capabilityToTrigger.endpointUrl),
                String::class.java //For an actuation, we don't care about the response (at this point in the system's development)
        )
    }

    private fun buildUrl(rootUrl: String, capabilityUrl: String) = "http://$rootUrl.local/arduino/$capabilityUrl"

    private data class FlowExecutionHelper(val flow: Flow, val future: ListenableFuture<ResponseEntity<CapabilityResponse>>)
}