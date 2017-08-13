package com.uj.HomeAutomationServer.rest

import com.uj.HomeAutomationServer.entity.ComponentEntity
import com.uj.HomeAutomationServer.repository.ComponentRepository
import com.uj.HomeAutomationServer.rest.response.ConnectedComponents
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/component")
class ComponentController(val componentRepository : ComponentRepository) {

    @GetMapping("/test")
    fun getComponentJsonExample() = ConnectedComponents(listOf(
        ComponentEntity("Temperature Sensor", "Reads the temperature of the surrounding environment", 1),
        ComponentEntity("Light Sensor", "Reads the light levels of the surrounding environment", 2)
    ))

    @GetMapping("/add/{componentName}")
    fun addComponent(@PathVariable componentName : String) {
        val newComponent = RestTemplate().getForObject("http://$componentName.local/arduino/deviceInformation", ComponentEntity::class.java)

        //TODO - check if the component already exists before adding it
        componentRepository.save(newComponent)
    }
}