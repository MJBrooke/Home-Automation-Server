package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.*
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/flow")
class FlowController(
        val modelMapper: ModelMapper,
        val flowRepository: FlowRepository,
        val componentRepository: AutomationComponentRepository,
        val capabilityRepository: CapabilityRepository) {

    @GetMapping
    fun getFlows() = flowRepository.findAll().map { modelMapper.map(it, FlowDto::class.java) }

    @PostMapping
    fun postFlow(@RequestBody flowDto: FlowDto): Flow? =
        with(flowDto) {
            val newFlow = Flow(name = name, description = description, sensorValue = sensorValue, actuationValue = actuationValue)

            newFlow.sensor = componentRepository.findOne(sensorId)
            newFlow.actuator = componentRepository.findOne(actuatorId)

            newFlow.sensorCapability = capabilityRepository.findOne(sensorCapabilityId)
            newFlow.actuatorCapability = capabilityRepository.findOne(actuatorCapabilityId)

            newFlow.sensorMoreThan = sensorMoreThan == "true"

            return flowRepository.save(newFlow)
        }
}