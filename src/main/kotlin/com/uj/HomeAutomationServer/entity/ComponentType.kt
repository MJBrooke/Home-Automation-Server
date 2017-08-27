package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class ComponentType(
        var name: String = "",

        @OneToMany(mappedBy = "componentType")
        var automationComponents: Set<AutomationComponent> = HashSet(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version
        var version: Int = 0
)