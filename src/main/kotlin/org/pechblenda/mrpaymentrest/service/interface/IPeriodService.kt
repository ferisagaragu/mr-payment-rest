package org.pechblenda.mrpaymentrest.service.`interface`

import org.springframework.http.ResponseEntity

interface IPeriodService {
	fun findAllPeriods(): ResponseEntity<Any>
	fun calculateNextPeriod(): ResponseEntity<Any>
}