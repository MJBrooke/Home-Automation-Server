package com.uj.HomeAutomationServer.repository

import com.uj.HomeAutomationServer.entity.AutomationComponent
import org.springframework.data.repository.CrudRepository

interface AutomationComponentRepository : CrudRepository<AutomationComponent, Long>
