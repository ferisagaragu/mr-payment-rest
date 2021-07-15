package org.pechblenda.mrpaymentrest.service.`interface`

import org.springframework.http.ResponseEntity

import java.util.UUID

interface IPeriodService {
	fun findAllPeriods(): ResponseEntity<Any>
	fun findPeriodDetail(periodUuid: UUID): ResponseEntity<Any>
}