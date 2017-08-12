package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.Component
import com.uj.HomeAutomationServer.repository.ComponentRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/component")
class ComponentController(val componentRepository : ComponentRepository) {

    @GetMapping("/test")
    fun getComponentJsonExample() = Component("Temperature Sensor", "Reads the temperature of the surrounding environment")

    @GetMapping("/add/{componentName}")
    fun addComponent(@PathVariable componentName : String) {
        val newComponent = RestTemplate().getForObject("http://$componentName.local/arduino/deviceInformation", Component::class.java)

        //TODO - check if the component already exists before adding it
        componentRepository.save(newComponent)
    }
}