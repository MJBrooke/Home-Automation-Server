package com.uj.HomeAutomationServer.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.uj.HomeAutomationServer.entity.AutomationComponent
import com.uj.HomeAutomationServer.entity.AutomationComponentDto
import com.uj.HomeAutomationServer.entity.AutomationComponentRepository
import org.modelmapper.ModelMapper
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/component")
class ComponentController(val modelMapper: ModelMapper, val rest: RestTemplate, val automationComponentRepository: AutomationComponentRepository) {

    private val ID_SENSOR = 1L
    private val ID_ACTUATOR = 2L

    @GetMapping("/{id}")
    fun getComponentById(@PathVariable id: Long): AutomationComponentDto =
            automationComponentRepository.findOne(id)
                    ?.let { return modelMapper.map(it, AutomationComponentDto::class.java) }
                    ?: throw ComponentNotFoundException("No component found for id: $id")

    @GetMapping("/sensor")
    fun getSensors() = getAutomationComponentDtoByComponentType(ID_SENSOR)

    @GetMapping("/actuator")
    fun getActuators() = getAutomationComponentDtoByComponentType(ID_ACTUATOR)

    @PostMapping("/add/{componentUrl}")
    fun addComponent(@PathVariable componentUrl: String): AutomationComponent? {
        val component = rest.getForObject("http://$componentUrl.local/arduino/deviceInformation", AutomationComponent::class.java)

        return automationComponentRepository.save(component)
    }

    @PostMapping("/testAdd")
    fun addTestComponent() {
        val actuatorDeviceInformationResponse = "{\"deviceId\":\"1562\",\"name\":\"Aircon\",\"description\":\"Cools or heats up the room\",\"endpointRoot\":\"actuator\",\"componentType\":{\"name\":\"Actuator\",\"id\":\"2\"},\"capabilities\":[{\"name\":\"Switch On\",\"description\":\"Switches on the aircon unit\",\"endpointUrl\":\"switchOn\"},{\"name\":\"Switch Off\",\"description\":\"Switches off the aircon unit\",\"endpointUrl\":\"switchOff\"}]}"

        val sensorDeviceInformationResponse = "{\n" +
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

        val component = ObjectMapper().readValue(sensorDeviceInformationResponse, AutomationComponent::class.java)
        automationComponentRepository.save(component)
    }

    @ExceptionHandler(ComponentNotFoundException::class)
    fun handleNotFoundException(e: Exception) = BusinessExceptionResponse(e.message!!)

    //    TODO - add this to a global controller advice class
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityException(e: Exception) = BusinessExceptionResponse("Attempted to add a duplicate automation component")

    private fun getAutomationComponentDtoByComponentType(componentTypeId: Long) =
            automationComponentRepository.findByComponentTypeId(componentTypeId)
                    .map { modelMapper.map(it, AutomationComponentDto::class.java) }
}