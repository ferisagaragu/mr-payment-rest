package org.pechblenda.mrpaymentrest.service

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

import org.pechblenda.exception.BadRequestException
import org.pechblenda.mrpaymentrest.entity.Payment
import org.pechblenda.mrpaymentrest.enum.PaymentType
import org.pechblenda.mrpaymentrest.repository.IPaymentRepository
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IPaymentService
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
class PaymentService(
	private val paymentRepository: IPaymentRepository,
	private val periodRepository: IPeriodRepository,
	private val response: Response
): IPaymentService {

	@Transactional(readOnly = true)
	override fun findAllPaymentsByPeriodUuid(periodUuid: UUID): ResponseEntity<Any> {
		return response.toListMap(paymentRepository.findAllByPeriodUuid(periodUuid)).ok()
	}

	@Transactional
	override fun createPayment(request: Request): ResponseEntity<Any> {
		val paymentRequest = request.to<Payment>(
			Payment::class,
			Validations(
				Validation(
					"name",
					"Upss el 'paymentUuid' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
					ValidationType.TEXT
				),
				Validation(
					"type",
					"Upss el 'type' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL
				)
			),
			EntityParse("periodUuid", "period", periodRepository, IdType.UUID)
		)

		if (paymentRequest.type == PaymentType.MONTHLY.ordinal) {
			val format = SimpleDateFormat("yyyy-MM-dd")
			val starDate = format.format(paymentRequest.startDate)
			val endDate = format.format(paymentRequest.endDate)

			val dateBefore = LocalDate.parse(starDate)
			val dateAfter = LocalDate.parse(endDate)
			val daysBetween = ChronoUnit.MONTHS.between(dateBefore, dateAfter) + 1

			paymentRequest.quantity = (paymentRequest.totalQuantity?.div(daysBetween) ?: 0) as Double
			paymentRequest.monthCount = daysBetween.toInt()
		}

		paymentRepository.save(paymentRequest)

		return response.ok()
	}

	@Transactional
	override fun setPaymentPaid(request: Request): ResponseEntity<Any> {
		val paymentRequest = request.to<Payment>(
			Payment::class,
			Validations(
				Validation(
					"uuid",
					"Upss el 'paymentUuid' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
				),
				Validation(
					"pay",
					"Upss el 'paymentUuid' es requerido",
					ValidationType.EXIST,
					ValidationType.NOT_BLANK,
					ValidationType.NOT_NULL,
					ValidationType.BOOLEAN
				)
			)
		)

		val payment = paymentRepository.findById(paymentRequest.uuid).orElseThrow {
			BadRequestException("Upss no se encuentra el pago")
		}

		payment.pay = paymentRequest.pay

		return response.created()
	}

	@Transactional
	override fun deletePayment(paymentUuid: UUID): ResponseEntity<Any> {
		val payment = paymentRepository.findById(paymentUuid).orElseThrow {
			BadRequestException("Upss no se encuentra el pago")
		}

		paymentRepository.delete(payment)

		return response.ok()
	}

}