package org.pechblenda.mrpaymentrest.controller

import java.util.UUID
import org.pechblenda.doc.annotation.ApiDocumentation
import org.pechblenda.exception.HttpExceptionResponse
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService
import org.pechblenda.mrpaymentrest.scheduled.PeriodScheduled

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(methods = [
	RequestMethod.GET,
	RequestMethod.POST,
  RequestMethod.DELETE
])
@RestController
@RequestMapping(name = "Period", value = ["/rest/periods"])
class PeriodController(
	private val periodService: IPeriodService,
	private val periodScheduled: PeriodScheduled,
	private val httpExceptionResponse: HttpExceptionResponse,
) {

	@GetMapping
	@ApiDocumentation(path = "doc/period/find-all-periods.json")
	fun findAllPeriods(): ResponseEntity<Any> {
		return try {
			periodService.findAllPeriods()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@PostMapping("/calculate-next-period")
	@ApiDocumentation(path = "doc/period/calculate-next-period.json")
	fun calculateNextPeriod(): ResponseEntity<Any> {
		return try {
			periodScheduled.calculateNextPeriod(0, 0)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@GetMapping("/calculate-next-period/{month}/{year}")
	@ApiDocumentation(path = "doc/period/calculate-next-period-by-month-year.json")
	fun calculateNextPeriod(
		@PathVariable month: Int,
		@PathVariable year: Int
	): ResponseEntity<Any> {
		return try {
			periodScheduled.calculateNextPeriod(month, year)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@DeleteMapping("/{periodUuid}")
	fun deletePeriod(
		@PathVariable periodUuid: UUID
	): ResponseEntity<Any> {
		return try {
			periodService.deletePeriod(periodUuid)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

}