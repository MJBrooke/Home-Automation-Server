package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.CapabilityDto
import com.uj.HomeAutomationServer.entity.CapabilityRepository
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CapabilityController(val modelMapper: ModelMapper, val capabilityRepository: CapabilityRepository) {

    @GetMapping("/capability/{id}")
    fun getCapabilityById(@PathVariable id: Long) = modelMapper.map(capabilityRepository.findOne(id), CapabilityDto::class.java)

    @GetMapping("/component/{id}/capability")
    fun getCapabilitiesByComponentId(@PathVariable id: Long) =
            capabilityRepository.findByAutomationComponentId(id)
                    .map { modelMapper.map(it, CapabilityDto::class.java) }
}