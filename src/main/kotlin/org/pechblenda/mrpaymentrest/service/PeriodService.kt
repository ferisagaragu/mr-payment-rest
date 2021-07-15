package org.pechblenda.mrpaymentrest.service

import org.pechblenda.mrpaymentrest.enum.PaymentType
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService
import org.pechblenda.service.Response
import org.pechblenda.exception.BadRequestException

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

import java.util.UUID

@Service
class PeriodService(
	private val periodRepository: IPeriodRepository,
	private val response: Response
): IPeriodService {

	override fun findAllPeriods(): ResponseEntity<Any> {
		return response.toListMap(periodRepository.findAll()).ok()
	}

	override fun findPeriodDetail(periodUuid: UUID): ResponseEntity<Any> {
		val period = periodRepository.findById(periodUuid).orElseThrow {
			BadRequestException("Upps no se encontro el perido")
		}
		val out = mutableMapOf<String, Double>()
		var unique = 0.0
		var monthly = 0.0
		var recurrent = 0.0

		period.payments.forEach { payment ->
			if (payment.type === PaymentType.UNIQUE.ordinal) {
				unique += payment.quantity
			}

			if (payment.type === PaymentType.MONTHLY.ordinal) {
				monthly += payment.quantity
			}

			if (payment.type === PaymentType.RECURRENT.ordinal) {
				recurrent += payment.quantity
			}
		}

		out["unique"] = unique
		out["monthly"] = monthly
		out["recurrent"] = recurrent

		return response.ok(out)
	}

}