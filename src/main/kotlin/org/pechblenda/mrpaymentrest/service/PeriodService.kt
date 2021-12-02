package org.pechblenda.mrpaymentrest.service

import org.pechblenda.exception.NotFoundException
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService
import org.pechblenda.service.Response

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.UUID

@Service
class PeriodService(
	private val periodRepository: IPeriodRepository,
	private val response: Response
): IPeriodService {

	override fun findAllPeriods(): ResponseEntity<Any> {
		return response.toListMap(periodRepository.findAll()).ok()
	}

	@Transactional
	override fun deletePeriod(periodUUID: UUID): ResponseEntity<Any> {
		val period = periodRepository.findById(periodUUID).orElseThrow {
			NotFoundException("No se encuentra el periodo que se quiere eliminar")
		}

		period.payments?.forEach { payment -> payment.period = null }

		periodRepository.delete(period)

		return response.ok()
	}

}