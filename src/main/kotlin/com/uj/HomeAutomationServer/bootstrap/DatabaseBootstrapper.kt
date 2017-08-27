package com.uj.HomeAutomationServer.bootstrap

import com.uj.HomeAutomationServer.entity.AutomationComponent
import com.uj.HomeAutomationServer.entity.Capability
import com.uj.HomeAutomationServer.entity.ComponentType
import com.uj.HomeAutomationServer.repository.AutomationComponentRepository
import com.uj.HomeAutomationServer.repository.ComponentTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class DatabaseBootstrapper(@Autowired val componentTypeRepository: ComponentTypeRepository,
                           @Autowired val automationComponentRepository: AutomationComponentRepository) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        loadComponentTypes()
        loadComponents()
    }

    private fun loadComponentTypes() {
        val sensorType = ComponentType("Sensor")
        val actuatorType = ComponentType("Actuator")

        componentTypeRepository.save(sensorType)
        componentTypeRepository.save(actuatorType)
    }

    private fun loadComponents() {
        val sensorType = componentTypeRepository.findOne(1)
        val actuatorType = componentTypeRepository.findOne(2)

        //TEMP SENSOR
        val tempSensor = AutomationComponent("Temperature Sensor", "Reads the ambient temperature of the surrounding atmosphere", "tempSensor", sensorType)

        val readTemp = Capability("Measure temperature", "Read ambient temperature in Celsius", "temperature", tempSensor)
        tempSensor.addCapability(readTemp)

        automationComponentRepository.save(tempSensor)

        //DOOR LOCK
        val doorLockActuator = AutomationComponent("Door Lock", "Locks a door mechanism", "doorLock", actuatorType)

        val lock = Capability("Lock door", "Lock the door connected to this component", "lock", doorLockActuator)
        doorLockActuator.addCapability(lock)

        val unlock = Capability("Unlock door", "Unlock the door connected to this component", "unlock", doorLockActuator)
        doorLockActuator.addCapability(unlock)

        automationComponentRepository.save(doorLockActuator)

        //HEATER CONTROLLER
        val heaterActuator = AutomationComponent("Heater", "Controls a heater and its temperature", "heater", actuatorType)

        val switchOn = Capability("Switch On", "Switches the heater on with the current temperature setting", "on", heaterActuator)
        heaterActuator.addCapability(switchOn)

        val switchOff = Capability("Switch Off", "Switches the heater off", "off", heaterActuator)
        heaterActuator.addCapability(switchOff)

        val setTemperature = Capability("Set Temperature", "Sets the temperature of the heater", "temperature", heaterActuator)
        heaterActuator.addCapability(setTemperature)

        automationComponentRepository.save(heaterActuator)
    }
}