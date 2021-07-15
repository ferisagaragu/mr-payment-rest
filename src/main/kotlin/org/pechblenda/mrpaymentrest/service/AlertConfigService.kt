package org.pechblenda.mrpaymentrest.service

import org.pechblenda.mrpaymentrest.entity.AlertConfig
import org.pechblenda.mrpaymentrest.repository.IAlertConfigRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IAlertConfigService
import org.pechblenda.service.Request
import org.pechblenda.service.Response
import org.pechblenda.service.enum.IdType
import org.pechblenda.service.helper.EntityParse
import org.pechblenda.service.helper.Validation
import org.pechblenda.service.helper.ValidationType
import org.pechblenda.service.helper.Validations
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlertConfigService(
	private val alertConfigRepository: IAlertConfigRepository,
	private val response: Response
): IAlertConfigService {

	@Transactional(readOnly = true)
	override fun findAllAlertConfig(): ResponseEntity<Any> {
		alertConfigRepository.findAll().forEach { alertConfig ->
			return response.toMap(alertConfig).ok()
		}

		return response.toMap({ }).ok()
	}

	@Transactional
	override fun createAlertConfig(request: Request): ResponseEntity<Any> {
		val alertConfig = request.to<AlertConfig>(
			AlertConfig::class,
			Validations(
				Validation(
					"lowQuantity",
					"Upss el 'lowQuantity' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
					ValidationType.NUMBER
				),
				Validation(
					"mediumQuantity",
					"Upss el 'mediumQuantity' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
					ValidationType.NUMBER
				),
				Validation(
					"hardQuantity",
					"Upss el 'hardQuantity' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
					ValidationType.NUMBER
				)
			)
		)

		if (!alertConfigRepository.existsById(alertConfig.uuid)) {
			alertConfigRepository.save(
				AlertConfig(
					alertConfig.lowQuantity,
					alertConfig.mediumQuantity,
					alertConfig.hardQuantity
				)
			)

			return response.created()
		} else {
			request.merge<AlertConfig>(
				EntityParse(
					"uuid",
					alertConfigRepository,
					IdType.UUID
				)
			)
		}

		return response.ok()
	}

}