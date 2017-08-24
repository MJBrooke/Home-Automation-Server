package com.uj.HomeAutomationServer.rest.response

import com.uj.HomeAutomationServer.entity.Component

data class ConnectedComponents(val sensors: List<Component>)