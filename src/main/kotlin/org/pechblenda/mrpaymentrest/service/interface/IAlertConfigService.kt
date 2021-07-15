package org.pechblenda.mrpaymentrest.service.`interface`

import org.pechblenda.service.Request

import org.springframework.http.ResponseEntity

interface IAlertConfigService {
	fun findAllAlertConfig(): ResponseEntity<Any>
	fun createAlertConfig(request: Request): ResponseEntity<Any>
}