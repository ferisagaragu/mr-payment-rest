package org.pechblenda.mrpaymentrest.controller

import org.pechblenda.exception.HttpExceptionResponse
import org.pechblenda.mrpaymentrest.service.`interface`.IPaymentService
import org.pechblenda.service.Request

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping

import java.util.UUID

@CrossOrigin(methods = [
	RequestMethod.GET,
	RequestMethod.POST,
	RequestMethod.PATCH,
	RequestMethod.DELETE
])
@RestController
@RequestMapping(value = ["/rest/payment"])
class PaymentController(
	private val paymentService: IPaymentService,
	private val httpExceptionResponse: HttpExceptionResponse,
) {

	@GetMapping("/{periodUuid}")
	fun findAllPaymentsByPeriodUuid(
		@PathVariable periodUuid: UUID
	): ResponseEntity<Any> {
		return try {
			paymentService.findAllPaymentsByPeriodUuid(periodUuid)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@PostMapping
	fun createPayment(
		@RequestBody request: Request
	): ResponseEntity<Any> {
		return try {
			paymentService.createPayment(request)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@PatchMapping
	fun setPaymentPaid(
		@RequestBody request: Request
	): ResponseEntity<Any> {
		return try {
			paymentService.setPaymentPaid(request)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@DeleteMapping("/{paymentUuid}")
	fun deletePayment(
		@PathVariable paymentUuid: UUID
	): ResponseEntity<Any> {
		return try {
			paymentService.deletePayment(paymentUuid)
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

}