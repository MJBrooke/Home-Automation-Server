package com.uj.HomeAutomationServer.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class Capability(
        var name: String = "",
        var description: String = "",
        var endpointUrl: String = "",

        @ManyToOne @JsonBackReference
        var automationComponent: AutomationComponent = AutomationComponent(),

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version @JsonIgnore
        var version: Int = 0
)

@Repository
interface CapabilityRepository : JpaRepository<Capability, Long>