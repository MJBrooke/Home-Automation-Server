package com.uj.HomeAutomationServer.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class Flow(
        var name: String = "",
        var description: String = "",
        var wasMet: Boolean = false,

//        TODO - move the below component/capability/value to an embeddable object

        @ManyToOne
        var sensor: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var sensorCapability: Capability = Capability(),

        var sensorValue: Double = 0.0,

        var sensorMoreThan: Boolean = false,

        @ManyToOne
        var actuator: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var actuatorCapabilityIfSensorValMet: Capability = Capability(),

        @ManyToOne
        var actuatorCapabilityIfSensorValNotMet: Capability = Capability(),

        var actuationValue: Double = 0.0,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version
        var version: Int = 0
) {
    override fun toString(): String {
        return "Flow(name='$name', description='$description', sensor=$sensor, sensorCapability=$sensorCapability, sensorValue=$sensorValue, actuator=$actuator, actuatorCapabilityIdIfSensorValMet=$actuatorCapabilityIfSensorValMet, actuatorCapabilityIdIfSensorValNotMet=$actuatorCapabilityIfSensorValNotMet, actuationValue=$actuationValue, id=$id, version=$version)"
    }
}

@Repository
interface FlowRepository : JpaRepository<Flow, Long> {

    fun findByActuatorId(id: Long): List<Flow>
}

data class FlowDto (
        var id: Long = 0,
        var name: String = "",
        var description: String = "",

        var sensorId: Long = 0,
        var sensorCapabilityId: Long = 0,
        var sensorValue: Double = 0.0,
        var sensorMoreThan: String = "",

        var actuatorId: Long = 0,
        var actuatorCapabilityIdIfSensorValMet: Long = 0,
        var actuatorCapabilityIdIfSensorValNotMet: Long = 0,
        var actuationValue: Double = 0.0
)