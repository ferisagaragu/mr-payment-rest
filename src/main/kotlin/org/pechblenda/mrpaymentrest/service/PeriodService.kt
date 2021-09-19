package org.pechblenda.mrpaymentrest.service

import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService
import org.pechblenda.service.Response

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PeriodService(
	private val periodRepository: IPeriodRepository,
	private val response: Response
): IPeriodService {

	override fun findAllPeriods(): ResponseEntity<Any> {
		return response.toListMap(periodRepository.findAll()).ok()
	}

}