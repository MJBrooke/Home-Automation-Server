package com.uj.HomeAutomationServer.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested component does not exist")
class ComponentNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested capability does not exist")
class CapabilityNotFoundException(message: String) : RuntimeException(message)

data class BusinessExceptionResponse(val message: String)