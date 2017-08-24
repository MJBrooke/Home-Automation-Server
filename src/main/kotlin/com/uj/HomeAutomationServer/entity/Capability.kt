package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class Capability(
        val name: String = "",
        val description: String = "",
        val endpointUrl: String = "",

        @ManyToOne(cascade = arrayOf(CascadeType.PERSIST, CascadeType.REMOVE))
        @JoinColumn(name = "component_id")
        val component: Component,

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Version
        val version: Int = 0
)