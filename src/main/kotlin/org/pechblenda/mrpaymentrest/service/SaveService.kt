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
		/*val restTemplate = RestTemplate()
		val data = mutableMapOf<String, Any>()
		data["username"] = "Don Pagador"
		data["text"] = "Se obtuvo tu dinero guardado"
		data["icon_url"] = "https://firebasestorage.googleapis.com/v0/b/mr-payment.appspot.com/o/mr-payment-icon.png?alt=media&token=bb73ce2f-c4fe-4234-8839-24bf69fa79b7"

		val request: HttpEntity<Map<String, Any>> = HttpEntity(data)
		val response2: ResponseEntity<String> = restTemplate
				.exchange(
					"https://hooks.slack.com/services/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",
					HttpMethod.POST,
					request,
					String::class.java
				)

		 */
		return response.ok(saveRepository.findAll())
	}

	override fun sumTotalSaves(): ResponseEntity<Any> {
		return response.ok(saveRepository.sumTotalSave())
	}

}