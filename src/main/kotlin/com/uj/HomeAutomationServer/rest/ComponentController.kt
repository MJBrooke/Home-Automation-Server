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

    @GetMapping("/connectedComponents")
    fun getConnectedComponents() = ConnectedComponentsResponse(automationComponentRepository.findByComponentTypeId(1), automationComponentRepository.findByComponentTypeId(2))

    @GetMapping("/{id}")
    fun getComponentById(@PathVariable id: Long) = automationComponentRepository.findOne(id) ?: throw ComponentNotFoundException("No component found for id: $id")

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ComponentNotFoundException::class)
    fun handleNotFoundException(e: Exception) = BusinessExceptionResponse(e.message!!)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityException(e: Exception) = BusinessExceptionResponse("Attempted to add a duplicate automation component")
}

data class ConnectedComponentsResponse(val sensors: List<AutomationComponent>, val actuators: List<AutomationComponent>)