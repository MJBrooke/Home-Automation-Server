package com.uj.HomeAutomationServer.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class Flow(
        var name: String = "",
        var description: String = "",

//        TODO - move the below component/capability/value to an embeddable object

        @ManyToOne
        var sensor: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var sensorCapability: Capability = Capability(),

        var sensorValue: Double = 0.0,

        @ManyToOne
        var actuator: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var actuatorCapability: Capability = Capability(),

        var actuationValue: Double = 0.0,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version
        var version: Int = 0


) {
    override fun toString(): String {
        return "Flow(name='$name', description='$description', sensor=$sensor, sensorCapability=$sensorCapability, sensorValue=$sensorValue, actuator=$actuator, actuatorCapability=$actuatorCapability, actuationValue=$actuationValue, id=$id, version=$version)"
    }
}

@Repository
interface FlowRepository : JpaRepository<Flow, Long>

data class FlowDto (
        var id: Long = 0,
        var name: String = "",
        var description: String = "",

        var sensorId: Long = 0,
        var sensorCapabilityId: Long = 0,
        var sensorValue: Double = 0.0,

        var actuatorId: Long = 0,
        var actuatorCapabilityId: Long = 0,
        var actuationValue: Double = 0.0
)