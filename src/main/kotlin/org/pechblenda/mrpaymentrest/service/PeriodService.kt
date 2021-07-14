package org.pechblenda.mrpaymentrest.service

import org.pechblenda.mrpaymentrest.entity.Payment
import org.pechblenda.mrpaymentrest.entity.Period
import org.pechblenda.mrpaymentrest.enum.PaymentType
import org.pechblenda.mrpaymentrest.repository.IPaymentRepository
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.service.`interface`.IPeriodService
import org.pechblenda.service.Response
import org.pechblenda.exception.BadRequestException

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

import java.util.UUID
import java.util.Date
import java.util.Calendar

import java.text.SimpleDateFormat
import java.time.ZoneId

@Service
class PeriodService(
	private val periodRepository: IPeriodRepository,
	private val paymentRepository: IPaymentRepository,
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

	override fun calculateNextPeriod(): ResponseEntity<Any> {
		if (Date() >= getMiddleMonth()) {
			println("Ya pasa del 14 de este mes")

			if (!periodRepository.existsByDate(getNextMonth())) {
				val period = periodRepository.save(Period(getNextMonth()))
				val recurrentPayments = paymentRepository.findAllByRecurrentTrue()
				val monthlyPayments = paymentRepository.findAllByMonthlyTrue()

				recurrentPayments.forEach {
					payment -> paymentRepository.save(
						Payment(
							payment.name,
							payment.quantity,
							period
						)
					)
				}

				monthlyPayments.forEach {
					payment -> paymentRepository.save(
						Payment(
							payment.name,
							payment.quantity,
							payment.startDate!!,
							payment.endDate!!,
							payment.totalQuantity!!,
							payment.monthCount!!,
							period
						)
					)
				}

				return response.created()
			}

			return response.ok()
		} else {
			return response.ok()
		}
	}

	private fun getMiddleMonth(): Date {
		val date = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val middleMonth = "15/${date.monthValue}/${date.year}"

		return SimpleDateFormat("dd/MM/yyyy").parse(middleMonth)
	}

	private fun getNextMonth(): Date {
		val date = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val nextMonth = "1/${date.monthValue}/${date.year}"
		val nextMonthDate = SimpleDateFormat("dd/MM/yyyy").parse(nextMonth)
		val nextMonthCalendar = Calendar.getInstance()

		nextMonthCalendar.isLenient = false
		nextMonthCalendar.time = nextMonthDate
		nextMonthCalendar.add(Calendar.MONTH, 1)

		return nextMonthCalendar.time
	}

}