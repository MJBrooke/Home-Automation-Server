package com.uj.HomeAutomationServer.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class Flow(
        var name: String = "",

//        TODO - move the below component/capability/value to an embeddable object

        @ManyToOne
        var sensor: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var sensorCapability: Capability = Capability(),

        var thresholdValue: Double = 0.0,

        @ManyToOne
        var actuator: AutomationComponent = AutomationComponent(),

        @ManyToOne
        var actuatorCapability: Capability = Capability(),

        var actuationValue: Double = 0.0,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version @JsonIgnore
        var version: Int = 0
)

@Repository
interface FlowRepository : JpaRepository<Flow, Long>