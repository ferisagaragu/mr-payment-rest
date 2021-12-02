package org.pechblenda.mrpaymentrest.webhook

import org.pechblenda.mrpaymentrest.enum.PaymentType
import org.pechblenda.mrpaymentrest.entity.Payment
import org.pechblenda.mrpaymentrest.repository.IPaymentRepository
import org.pechblenda.mrpaymentrest.entity.Period
import org.pechblenda.mrpaymentrest.repository.IPeriodRepository
import org.pechblenda.mrpaymentrest.repository.ISaveRepository

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import org.springframework.stereotype.Component

import java.text.NumberFormat

@Component
class SlackAlertMessage(
	private val paymentRepository: IPaymentRepository,
	private val periodRepository: IPeriodRepository,
	private val saveRepository: ISaveRepository
) {

	fun onCreatePeriod(period: Period) {
		sendAlert(
			"Se creo el nuevo periodo de *_${period.name()}_* con un monto de " +
			"*${currentFormat(periodRepository.sumDebtByPeriodUuid(period.uuid))}*. " +
			"Tienes ahorrado ${currentFormat(saveRepository.sumTotalSave())} :moneybag:."
		)
	}

	fun onCreatePayment(payment: Payment) {
		sendAlert(
			"Se creo un _${getPaymentTypeName(payment.type)}_ por " +
			"*${currentFormat(payment.quantity)}* para el periodo de " +
			"*_${payment.period?.name()}_* tus gastos ascienden a " +
			"*${currentFormat(paymentRepository.sumDebtByPaymentUuid(payment.uuid))}*."
		)
	}

	fun onUpdatePayment(payment: Payment) {
		sendAlert(
			"Se actualizo el _${getPaymentTypeName(payment.type)}_/*${payment.name}* por " +
			"*${currentFormat(payment.quantity)}* para el periodo de " +
			"*_${payment.period?.name()}_* tus gastos ascienden a " +
			"*${currentFormat(paymentRepository.sumDebtByPaymentUuid(payment.uuid))}*."
		)
	}

	fun onSetPaymentPaid(payment: Payment) {
		if (payment.pay) {
			sendAlert(
				"¡Genial! se pago el _${getPaymentTypeName(payment.type)}_/*${payment.name}* por " +
				"*${currentFormat(payment.quantity)}* para el periodo de " +
				"*_${payment.period?.name()}_*."
			)
		} else {
			sendAlert(
				"¡Upss! se anulo el _${getPaymentTypeName(payment.type)}_/*${payment.name}* por " +
				"*${currentFormat(payment.quantity)}* para el periodo de " +
				"*_${payment.period?.name()}_*."
			)
		}
	}

	fun onDeletePayment(payment: Payment) {
		sendAlert(
			"Se anulo el _${getPaymentTypeName(payment.type)}_/*${payment.name}* por " +
			"*${currentFormat(payment.quantity)}* para el periodo de " +
			"*_${payment.period?.name()}_* tus gastos se ajustaron a " +
			"*${currentFormat(periodRepository.sumDebtByPeriodUuid(payment.period?.uuid!!))}*."
		)
	}

	private fun getPaymentTypeName(paymentType: Int): String {
		return when(paymentType) {
			PaymentType.UNIQUE.ordinal -> "pago *Único*"
			PaymentType.MONTHLY.ordinal -> "pago *Mensual*"
			PaymentType.RECURRENT.ordinal -> "pago *Recurrente*"
			PaymentType.SAVE.ordinal -> "*ahorro*"
			PaymentType.EXTRA.ordinal -> "*dinero extra*"
			PaymentType.LOAN.ordinal -> "*préstamo*"
			else -> ""
		}
	}

	private fun currentFormat(number: Double?): String {
		if (number == null) {
			return "$0 :white_check_mark:"
		}

		val numberFormat = NumberFormat.getCurrencyInstance()
		return "${numberFormat.format(number).replace("¤", "$")} :dollar:"
	}

	private fun sendAlert(message: String) {
		try {
			val restTemplate = RestTemplate()
			val data = mutableMapOf<String, Any>()
			data["username"] = "Don Pagador"
			data["text"] = message
			data["icon_url"] = "https://firebasestorage.googleapis.com/v0/b" +
					"/mr-payment.appspot.com/o/mr-payment-icon.png?" +
					"alt=media&token=bb73ce2f-c4fe-4234-8839-24bf69fa79b7"

			val request: HttpEntity<Map<String, Any>> = HttpEntity(data)
			restTemplate.exchange(
				"https://hooks.slack.com/services/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",
				HttpMethod.POST,
				request,
				String::class.java
			)
		} catch (e: Exception) {
			println(e.printStackTrace())
		}
	}

}