package org.pechblenda.mrpaymentrest.service.`interface`

import org.springframework.http.ResponseEntity

interface ISaveService {
	fun findAllSaves(): ResponseEntity<Any>
	fun sumTotalSaves(): ResponseEntity<Any>
}