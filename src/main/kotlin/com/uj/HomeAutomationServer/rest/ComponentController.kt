package com.uj.HomeAutomationServer.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.uj.HomeAutomationServer.entity.AutomationComponent
import com.uj.HomeAutomationServer.entity.AutomationComponentRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/component")
class ComponentController(val automationComponentRepository: AutomationComponentRepository) {

    @PostMapping("/add/{componentName}")
    fun addComponent(@PathVariable componentName: String) {
        //val newComponent = RestTemplate().getForObject("http://$componentName.local/arduino/deviceInformation", AutomationComponent::class.java)

        val jsonDeviceInformationResponse = "{\n" +
                "    \"deviceId\": \"12345\",\n" +
                "    \"name\": \"Test\",\n" +
                "    \"description\": \"Reads the ambient temperature of the surrounding atmosphere\",\n" +
                "    \"endpointRoot\": \"Test\",\n" +
                "    \"componentType\": {\n" +
                "        \"name\": \"Sensor\",\n" +
                "        \"id\": \"1\"\n" +
                "    },\n" +
                "    \"capabilities\": [\n" +
                "        {\n" +
                "            \"name\": \"Test\",\n" +
                "            \"description\": \"Test\",\n" +
                "            \"endpointUrl\": \"Test\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Test 2\",\n" +
                "            \"description\": \"Test 2\",\n" +
                "            \"endpointUrl\": \"Test 2\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"

        val mapper = ObjectMapper()
        val component = mapper.readValue(jsonDeviceInformationResponse, AutomationComponent::class.java)

        automationComponentRepository.save(component)
    }

    @GetMapping("/sensors")
    fun getSensors() = ConnectedSensorsResponse(automationComponentRepository.findByComponentTypeId(1))

    @GetMapping("/actuators")
    fun getActuators() = ConnectedActuatorsResponse(automationComponentRepository.findByComponentTypeId(2))

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityException(e: Exception) = BusinessExceptionResponse("Attempted to add a duplicate automation component")
}

data class ConnectedSensorsResponse(val sensors: List<AutomationComponent>)
data class ConnectedActuatorsResponse(val actuators: List<AutomationComponent>)

data class BusinessExceptionResponse(val message: String)