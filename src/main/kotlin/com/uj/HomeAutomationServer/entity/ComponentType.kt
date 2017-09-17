package com.uj.HomeAutomationServer.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class ComponentType(
        var name: String = "",

        @JsonIgnore
        @OneToMany(mappedBy = "componentType")
        var automationComponents: Set<AutomationComponent> = HashSet(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @Version
        var version: Int = 0
)

@Repository
interface ComponentTypeRepository : JpaRepository<ComponentType, Long> {
    fun findByAutomationComponentsId(id: Long) : ComponentType
}

data class ComponentTypeDto (
        var id: Long = 0,
        var name: String = ""
)