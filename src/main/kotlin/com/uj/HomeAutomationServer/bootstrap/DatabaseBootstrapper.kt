package com.uj.HomeAutomationServer.bootstrap

import com.uj.HomeAutomationServer.entity.AutomationComponentRepository
import com.uj.HomeAutomationServer.entity.ComponentType
import com.uj.HomeAutomationServer.entity.ComponentTypeRepository
import com.uj.HomeAutomationServer.entity.FlowRepository
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



//        val lightSensor = AutomationComponent(11354, "Light Sensor", "Measures light", "arduino", sensorType)
//
//        val measureLight = Capability("Measure light", "Measure light", "light", lightSensor)
//        lightSensor.addCapability(measureLight)
//
//        automationComponentRepository.save(lightSensor)
//
//
//
//
//        val lockAndLightActuator = AutomationComponent(123456, "Light and lock", "Measures light", "lockandlight", actuatorType)
//
//        val switchOn = Capability("Switch On", "On", "lightOn", lockAndLightActuator)
//        lockAndLightActuator.addCapability(switchOn)
//
//        val switchOff = Capability("Switch Off", "On", "lightOff", lockAndLightActuator)
//        lockAndLightActuator.addCapability(switchOff)
//
//        val lock = Capability("Lock", "Lock", "lock", lockAndLightActuator)
//        lockAndLightActuator.addCapability(lock)
//
//        val unlock = Capability("Unlock", "Unlock", "unlock", lockAndLightActuator)
//        lockAndLightActuator.addCapability(unlock)
//
//        automationComponentRepository.save(lockAndLightActuator)
    }
}