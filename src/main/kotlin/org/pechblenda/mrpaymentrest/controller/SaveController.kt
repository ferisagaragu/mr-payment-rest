package org.pechblenda.mrpaymentrest.controller

import org.pechblenda.exception.HttpExceptionResponse
import org.pechblenda.mrpaymentrest.service.`interface`.ISaveService
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(methods = [
	RequestMethod.GET
])
@RestController
@RequestMapping(name = "Save", value = ["/rest/saves"])
class SaveController(
	private val saveService: ISaveService,
	private val httpExceptionResponse: HttpExceptionResponse,
) {

	@GetMapping
	fun findAllSaves(): ResponseEntity<Any> {
		return try {
			saveService.findAllSaves()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

	@GetMapping("/sum-total-saves")
	fun sumTotalSaves(): ResponseEntity<Any> {
		return try {
			saveService.sumTotalSaves()
		} catch (e: ResponseStatusException) {
			httpExceptionResponse.error(e)
		}
	}

}