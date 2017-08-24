package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.Component
import com.uj.HomeAutomationServer.repository.ComponentRepository
import com.uj.HomeAutomationServer.rest.response.ConnectedComponents
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/component")
class ComponentController(val componentRepository : ComponentRepository) {

    @PostMapping("/add/{componentName}")
    fun addComponent(@PathVariable componentName : String) {
        val newComponent = RestTemplate().getForObject("http://$componentName.local/arduino/deviceInformation", Component::class.java)

        //TODO - check if the component already exists before adding it
        componentRepository.save(newComponent)
    }

    @GetMapping("/sensors")
    fun getSensors() = ConnectedComponents(componentRepository.findAll() as List<Component>)
}