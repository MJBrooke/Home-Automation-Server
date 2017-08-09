package com.uj.HomeAutomationServer.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Component(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val name: String,
        val description: String
)