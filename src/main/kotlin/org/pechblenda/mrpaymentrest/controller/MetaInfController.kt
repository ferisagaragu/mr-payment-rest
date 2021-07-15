package org.pechblenda.mrpaymentrest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import org.pechblenda.service.Response

@CrossOrigin(methods = [
	RequestMethod.GET
])
@RestController
@RequestMapping(name = "MetaInfo", value = ["/rest/meta-infos"])
class MetaInfController(
	private val response: Response
) {

	@GetMapping("/version")
	fun getAppVersion(): ResponseEntity<Any> {
		return response.ok("1.1.2");
	}

}