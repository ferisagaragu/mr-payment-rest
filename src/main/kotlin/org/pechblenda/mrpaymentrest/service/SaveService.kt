package org.pechblenda.mrpaymentrest.service

import org.pechblenda.mrpaymentrest.repository.ISaveRepository
import org.pechblenda.mrpaymentrest.service.`interface`.ISaveService
import org.pechblenda.service.Response

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SaveService(
	private val saveRepository: ISaveRepository,
	private val response: Response
): ISaveService {

	override fun findAllSaves(): ResponseEntity<Any> {
		return response.ok(saveRepository.findAll())
	}

	override fun sumTotalSaves(): ResponseEntity<Any> {
		return response.ok(saveRepository.sumTotalSave())
	}

}