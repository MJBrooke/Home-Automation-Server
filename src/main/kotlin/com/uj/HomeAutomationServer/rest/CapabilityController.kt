package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.CapabilityDto
import com.uj.HomeAutomationServer.entity.CapabilityRepository
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CapabilityController(val modelMapper: ModelMapper, val capabilityRepository: CapabilityRepository) {

    @GetMapping("/component/{id}/capability")
    fun getCapabilitiesByComponent(@PathVariable id: Long) =
            capabilityRepository.findByAutomationComponentId(id)
                    .map { modelMapper.map(it, CapabilityDto::class.java) }
}