package com.uj.HomeAutomationServer.repository

import com.uj.HomeAutomationServer.entity.Capability
import org.springframework.data.repository.CrudRepository

interface CapabilityRepository : CrudRepository<Capability, Long>