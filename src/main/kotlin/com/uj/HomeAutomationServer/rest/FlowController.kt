package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.FlowDto
import com.uj.HomeAutomationServer.entity.FlowRepository
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flow")
class FlowController(val modelMapper: ModelMapper, val flowRepository: FlowRepository) {

    @GetMapping
    fun getFlows() = flowRepository.findAll().map { modelMapper.map(it, FlowDto::class.java) }
}