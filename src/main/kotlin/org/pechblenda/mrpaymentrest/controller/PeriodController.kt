package org.pechblenda.mrpaymentrest.controller

import org.pechblenda.exception.HttpExceptionResponse
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(methods = [
	RequestMethod.GET,
	RequestMethod.POST
])
@RestController
@RequestMapping(value = ["/rest/period"])
class PeriodController(
	private val periodService: IPeriodService,
	private val httpExceptionResponse: HttpExceptionResponse,
) {

	@GetMapping
	fun findAllCompaniesByUserUuid(): ResponseEntity<Any> {
		return try {
			periodService.findAllPeriods()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@PostMapping("/calculate-next")
	fun calculateNextPeriod(): ResponseEntity<Any> {
		return try {
			periodService.calculateNextPeriod()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

}