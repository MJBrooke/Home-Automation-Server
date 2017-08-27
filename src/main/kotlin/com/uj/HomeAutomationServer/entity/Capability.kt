package com.uj.HomeAutomationServer.entity

import javax.persistence.*

@Entity
class Capability(
        var name: String = "",
        var description: String = "",
        var endpointUrl: String = "",

        @ManyToOne
        var automationComponent: AutomationComponent = AutomationComponent(),

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Version
        var version: Int = 0
)