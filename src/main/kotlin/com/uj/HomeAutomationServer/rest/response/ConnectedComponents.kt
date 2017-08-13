package com.uj.HomeAutomationServer.rest.response

import com.uj.HomeAutomationServer.entity.ComponentEntity

data class ConnectedComponents(val components: List<ComponentEntity>)