package org.pechblenda.mrpaymentrest.controller

import org.pechblenda.exception.HttpExceptionResponse
import org.pechblenda.mrpaymentrest.service.`interface`.IAlertConfigService
import org.pechblenda.service.Request
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(methods = [
	RequestMethod.GET,
	RequestMethod.POST
])
@RestController
@RequestMapping(name = "Alert Config", value = ["/rest/alert-config"])
class AlertConfigController(
	private val alertConfigService: IAlertConfigService,
	private val httpExceptionResponse: HttpExceptionResponse
) {

	@GetMapping
	fun findAllAlertConfig(): ResponseEntity<Any> {
		return try {
			alertConfigService.findAllAlertConfig()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@PostMapping
	fun sumTotalSaves(
		@RequestBody request: Request
	): ResponseEntity<Any> {
		return try {
			alertConfigService.createAlertConfig(request)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

}