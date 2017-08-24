package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class ComponentType(
        val name: String = "",

        @OneToMany(mappedBy = "componentType", cascade = arrayOf(CascadeType.PERSIST))
        val components: Set<Component> = setOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Version
        val version: Int = 0
)