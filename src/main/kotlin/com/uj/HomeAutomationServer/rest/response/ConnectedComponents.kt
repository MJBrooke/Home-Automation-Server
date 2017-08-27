package com.uj.HomeAutomationServer.rest.response

import com.uj.HomeAutomationServer.entity.AutomationComponent

data class ConnectedComponents(val sensors: List<AutomationComponent>)