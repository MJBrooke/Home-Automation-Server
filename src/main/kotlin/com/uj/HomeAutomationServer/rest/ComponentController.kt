package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.AutomationComponent
import com.uj.HomeAutomationServer.repository.AutomationComponentRepository
import com.uj.HomeAutomationServer.rest.response.ConnectedComponents
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/component")
class ComponentController(val automationComponentRepository: AutomationComponentRepository) {

    @PostMapping("/add/{componentName}")
    fun addComponent(@PathVariable componentName : String) {
        val newComponent = RestTemplate().getForObject("http://$componentName.local/arduino/deviceInformation", AutomationComponent::class.java)

        //TODO - check if the component already exists before adding it
        automationComponentRepository.save(newComponent)
    }

    @GetMapping("/sensors")
    fun getSensors() = ConnectedComponents(automationComponentRepository.findAll() as List<AutomationComponent>)
}