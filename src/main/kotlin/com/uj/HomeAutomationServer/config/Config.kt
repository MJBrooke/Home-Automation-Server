package com.uj.HomeAutomationServer.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.AsyncRestTemplate
import org.springframework.web.client.RestTemplate

@Configuration
class Config {

    @Bean
    fun modelMapper() = ModelMapper()

    @Bean
    fun asyncRest() = AsyncRestTemplate()

    @Bean
    fun rest() = RestTemplate()
}