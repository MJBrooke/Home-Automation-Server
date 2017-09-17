package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.ComponentTypeDto
import com.uj.HomeAutomationServer.entity.ComponentTypeRepository
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ComponentTypeController(val modelMapper: ModelMapper, val componentTypeRepository: ComponentTypeRepository) {

    @GetMapping("component/{id}/componentType")
    fun getComponentType(@PathVariable id: Long) = modelMapper.map(componentTypeRepository.findByAutomationComponentsId(id), ComponentTypeDto::class.java)
}