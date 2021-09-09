package org.pechblenda.mrpaymentrest.scheduled

import org.pechblenda.mrpaymentrest.entity.Payment
import org.pechblenda.mrpaymentrest.entity.Period
import org.pechblenda.mrpaymentrest.entity.Save
import org.pechblenda.mrpaymentrest.repository.IPaymentRepository
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.repository.ISaveRepository
import org.pechblenda.service.Response
import org.pechblenda.mrpaymentrest.webhook.SlackAlertMessage

import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@Component
class PeriodScheduled(
	private val periodRepository: IPeriodRepository,
	private val paymentRepository: IPaymentRepository,
	private val saveRepository: ISaveRepository,
	private val slackAlert: SlackAlertMessage,
	private val response: Response
) {

	@Scheduled(cron = "0 0 16 15 * *")
	fun calculateNextPeriod(): ResponseEntity<Any> {
		if ((Date() >= getMiddleMonth()) && !periodRepository.existsByDate(getNewPeriodDate())) {
			val period = periodRepository.save(Period(getNewPeriodDate()))
			val recurrentPayments = paymentRepository.findAllRecurrentByPeriodDate(getBeforeMonth())
			val monthlyPayments = paymentRepository.findAllMonthlyByPeriodDate(getBeforeMonth())
			val savePayments = paymentRepository.findAllSaveByPeriodDate(getBeforeMonth())

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

			savePayments.forEach { payment ->
				if (payment.pay) {
					saveRepository.save(
						Save(
							Date(),
							payment.quantity
						)
					)
				} else {
					paymentRepository.delete(payment)
				}
			}

			slackAlert.onCreatePeriod(period)
			return response.created()
		}

		return response.ok()
	}

	private fun getMiddleMonth(): Date {
		val date = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val middleMonth = "15/${date.monthValue}/${date.year}"

		return SimpleDateFormat("dd/MM/yyyy").parse(middleMonth)
	}

	private fun getNewPeriodDate(): Date {
		val date = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val nextMonth = "1/${date.monthValue}/${date.year}"

		return SimpleDateFormat("dd/MM/yyyy").parse(nextMonth)
	}

	private fun getBeforeMonth(): Date {
		val date = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val nextMonth = "1/${date.monthValue}/${date.year}"
		val nextMonthDate = SimpleDateFormat("dd/MM/yyyy").parse(nextMonth)
		val nextMonthCalendar = Calendar.getInstance()

		nextMonthCalendar.isLenient = false
		nextMonthCalendar.time = nextMonthDate
		nextMonthCalendar.add(Calendar.MONTH, -1)

		return nextMonthCalendar.time
	}

}