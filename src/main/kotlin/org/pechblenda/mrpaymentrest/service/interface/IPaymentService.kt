package org.pechblenda.mrpaymentrest.service.`interface`

import org.springframework.http.ResponseEntity

import java.util.UUID

import org.pechblenda.service.Request

interface IPaymentService {
	fun findAllPaymentsByPeriodUuid(periodUuid: UUID): ResponseEntity<Any>
	fun createPayment(request: Request): ResponseEntity<Any>
	fun setPaymentPaid(request: Request): ResponseEntity<Any>
	fun deletePayment(paymentUuid: UUID): ResponseEntity<Any>
}