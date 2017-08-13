package com.uj.HomeAutomationServer.repository

import com.uj.HomeAutomationServer.entity.ComponentEntity
import org.springframework.data.repository.CrudRepository

interface ComponentRepository : CrudRepository<ComponentEntity, Long>
