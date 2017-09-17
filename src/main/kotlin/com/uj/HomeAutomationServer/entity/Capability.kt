package com.uj.HomeAutomationServer.entity

import com.fasterxml.jackson.annotation.JsonBackReference
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

        @Version
        var version: Int = 0
)

@Repository
interface CapabilityRepository : JpaRepository<Capability, Long> {
    fun findByAutomationComponentId(id: Long) : List<Capability>
}

data class CapabilityDto(
        var id: Long = 0,
        var name: String = "",
        var description: String = "",
        var endpointUrl: String = ""
)