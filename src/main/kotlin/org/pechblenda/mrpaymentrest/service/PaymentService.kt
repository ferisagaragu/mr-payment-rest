package org.pechblenda.mrpaymentrest.service

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
import org.pechblenda.mrpaymentrest.webhook.SlackAlertMessage

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class PaymentService(
	private val paymentRepository: IPaymentRepository,
	private val periodRepository: IPeriodRepository,
	private val slackAlert: SlackAlertMessage,
	private val response: Response
): IPaymentService {

	@Transactional(readOnly = true)
	override fun findAllPaymentsByPeriodUuid(periodUuid: UUID): ResponseEntity<Any> {
		val payment = paymentRepository.findAllByPeriodUuid(periodUuid)
		val period = periodRepository.findById(periodUuid).orElseThrow {
			BadRequestException("Upss no se encuentra el periodo")
		}
		val request = Request()
		request["totalMoney"] = period.totalMoney()?: 0
		request["debt"] = period.debt()
		request["biweekly"] = period.biweekly()
		request["individual"] = period.individual()
		request["remainingDebt"] = period.remainingDebt()

		return response.toListMap(payment).ok(request)
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
				),
				Validation(
					"periodUuid",
					"Upss el 'periodUuid' es requerido",
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

		val payment = paymentRepository.save(paymentRequest)
		slackAlert.onCreatePayment(payment)

		return response.ok()
	}

	@Transactional
	override fun updatePayment(request: Request): ResponseEntity<Any> {
		val request = request.merge<Payment>(
			EntityParse(
				"uuid",
				paymentRepository,
				IdType.UUID
			)
		)

		slackAlert.onUpdatePayment(request)

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
		slackAlert.onSetPaymentPaid(payment)

		return response.ok()
	}

	@Transactional
	override fun deletePayment(paymentUuid: UUID): ResponseEntity<Any> {
		val payment = paymentRepository.findById(paymentUuid).orElseThrow {
			BadRequestException("Upss no se encuentra el pago")
		}

		paymentRepository.delete(payment)
		slackAlert.onDeletePayment(payment)

		return response.ok()
	}

}