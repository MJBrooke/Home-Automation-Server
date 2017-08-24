package com.uj.HomeAutomationServer.repository

import com.uj.HomeAutomationServer.entity.Component
import org.springframework.data.repository.CrudRepository

interface ComponentRepository : CrudRepository<Component, Long>
