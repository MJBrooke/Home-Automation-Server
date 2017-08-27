package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class AutomationComponent(
        var name: String = "",
        var description: String = "",
        var endpointRoot: String = "",

        @ManyToOne
        var componentType: ComponentType = ComponentType(),

        @OneToMany(mappedBy = "automationComponent", cascade = arrayOf(CascadeType.ALL))
        var componentCapabilities: MutableSet<Capability> = HashSet(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version
        var version: Int = 0
) {
    fun addCapability(capability: Capability) {
        componentCapabilities.add(capability)
        capability.automationComponent = this
    }
}
