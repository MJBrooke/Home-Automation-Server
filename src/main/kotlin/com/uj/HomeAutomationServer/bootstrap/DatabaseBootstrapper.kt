package com.uj.HomeAutomationServer.bootstrap

import com.uj.HomeAutomationServer.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class DatabaseBootstrapper(val componentTypeRepository: ComponentTypeRepository,
                           val automationComponentRepository: AutomationComponentRepository,
                           val flowRepository: FlowRepository) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {

        //SENSOR TYPES
        val sensorType = ComponentType("Sensor")
        val actuatorType = ComponentType("Actuator")

        componentTypeRepository.save(sensorType)
        componentTypeRepository.save(actuatorType)

        //MOVEMENT SENSOR
//        val movementSensor = AutomationComponent(11354, "Movement Sensor", "Detects movement around the sensor", "movementSensor", sensorType)
//
//        val detectMovement = Capability("Detect movement", "Detect movement", "detect", movementSensor)
//        movementSensor.addCapability(detectMovement)
//
//        automationComponentRepository.save(movementSensor)

        //DOOR LOCK
//        val doorLockActuator = AutomationComponent(84527, "Door Lock", "Locks a door mechanism", "doorLock", actuatorType)
//
//        val lock = Capability("Lock door", "Lock the door connected to this component", "lock", doorLockActuator)
//        doorLockActuator.addCapability(lock)
//
//        val unlock = Capability("Unlock door", "Unlock the door connected to this component", "unlock", doorLockActuator)
//        doorLockActuator.addCapability(unlock)
//
//        automationComponentRepository.save(doorLockActuator)

        //HEATER CONTROLLER
//        val heaterActuator = AutomationComponent(44596, "Heater", "Controls a heater and its temperature", "heater", actuatorType)
//
//        val switchOn = Capability("Switch On", "Switches the heater on with the current temperature setting", "on", heaterActuator)
//        heaterActuator.addCapability(switchOn)
//
//        val switchOff = Capability("Switch Off", "Switches the heater off", "off", heaterActuator)
//        heaterActuator.addCapability(switchOff)
//
//        val setTemperature = Capability("Set Temperature", "Sets the temperature of the heater", "temperature", heaterActuator)
//        heaterActuator.addCapability(setTemperature)
//
//        automationComponentRepository.save(heaterActuator)

        //TEMPERATURE|HEATER FLOW
//        val tempHeaterFlow = Flow("Automatic Doorlock", "Unlocks the door if movement is detected", movementSensor, detectMovement, actuator = heaterActuator, actuatorCapability = switchOn)
//        flowRepository.save(tempHeaterFlow)

    }
}