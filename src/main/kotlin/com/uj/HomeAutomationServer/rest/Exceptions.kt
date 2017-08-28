package com.uj.HomeAutomationServer.rest

class ComponentNotFoundException(message: String) : RuntimeException(message)

data class BusinessExceptionResponse(val message: String)