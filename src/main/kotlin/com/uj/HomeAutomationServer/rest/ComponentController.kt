package com.uj.HomeAutomationServer.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/arduino")
class ComponentController {

    @GetMapping("/test")
    fun testRestMapping(): String {
        return "This works! 1+1=${1+1}"
    }
}