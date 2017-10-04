package com.uj.HomeAutomationServer

import com.uj.HomeAutomationServer.rest.FlowController
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Main(val flowController: FlowController) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {

        while(true) {
            flowController.getFlows()
                    .map { println(it.name) }
        }
    }
}