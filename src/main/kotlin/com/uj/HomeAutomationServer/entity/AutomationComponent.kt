package com.uj.HomeAutomationServer.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class AutomationComponent(
        @Column(unique = true)
        var deviceId: Long = 0,

        var name: String = "",
        var description: String = "",
        var endpointRoot: String = "",

        @ManyToOne
        var componentType: ComponentType = ComponentType(),

        @JsonManagedReference
        @OneToMany(mappedBy = "automationComponent", cascade = arrayOf(CascadeType.ALL))
        var capabilities: MutableSet<Capability> = mutableSetOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @Version
        var version: Int = 0
) {
    fun addCapability(capability: Capability) {
        capabilities.add(capability)
        capability.automationComponent = this
    }
}

@Repository
interface AutomationComponentRepository : JpaRepository<AutomationComponent, Long> {
    fun findByComponentTypeId(id: Long): List<AutomationComponent>
}
