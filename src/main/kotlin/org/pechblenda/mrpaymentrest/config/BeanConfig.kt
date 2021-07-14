package org.pechblenda.mrpaymentrest.config

import org.pechblenda.doc.Documentation
import org.pechblenda.doc.entity.ApiInfo
import org.pechblenda.mrpaymentrest.controller.PaymentController
import org.pechblenda.mrpaymentrest.controller.PeriodController

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("org.pechblenda.bean")
class BeanConfig {

	@Bean
	fun documentation(): Documentation {
		return Documentation(
			ApiInfo(
				title = "Notarialy",
				description = "Notarialy rest api",
				iconUrl = "",
				version = "0.0.1",
				credentials = listOf()
			),
			PeriodController::class,
			PaymentController::class
		)
	}

}