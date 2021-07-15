package org.pechblenda.mrpaymentrest.config

import org.pechblenda.doc.Documentation
import org.pechblenda.doc.entity.ApiInfo
import org.pechblenda.mrpaymentrest.controller.AlertConfigController
import org.pechblenda.mrpaymentrest.controller.PaymentController
import org.pechblenda.mrpaymentrest.controller.PeriodController
import org.pechblenda.mrpaymentrest.controller.SaveController

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ComponentScan("org.pechblenda.bean")
class BeanConfig {

	@Bean
	fun documentation(): Documentation {
		return Documentation(
			ApiInfo(
				title = "Don Pagador",
				description = "Mr Payment rest api",
				iconUrl = "",
				version = "0.0.1",
				credentials = listOf()
			),
			PeriodController::class,
			PaymentController::class,
			SaveController::class,
			AlertConfigController::class
		)
	}

}