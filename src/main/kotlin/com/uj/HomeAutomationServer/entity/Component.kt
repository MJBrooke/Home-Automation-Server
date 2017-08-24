package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class Component(
        val name: String = "",
        val description: String = "",
        val endpointRoot: String = "",

        @ManyToOne(cascade = arrayOf(CascadeType.PERSIST, CascadeType.REMOVE))
        @JoinColumn(name = "component_type_id")
        val componentType: ComponentType = ComponentType(),

        @OneToMany(cascade = arrayOf(CascadeType.PERSIST), mappedBy = "component")
        val componentCapabilities: Set<Capability> = setOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Version
        val version: Int = 0
)
