package org.pechblenda.mrpaymentrest.entity

import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import org.pechblenda.mrpaymentrest.enum.PaymentType
import org.pechblenda.service.annotation.Key
import org.pechblenda.service.enum.DefaultValue

import java.text.SimpleDateFormat
import javax.persistence.OneToOne

@Entity
@Table(name = "period")
class Period(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var date: Date,

	@OneToOne
	var money: Money?,

	@OneToMany(mappedBy = "period")
	var payments: MutableList<Payment>
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		date = Date(),
		money = null,
		payments = mutableListOf()
	)

	constructor(date: Date): this() {
		this.uuid = UUID.randomUUID()
		this.date = date
		this.payments = mutableListOf()
	}

	@Key(name = "name", autoCall = true, defaultNullValue = DefaultValue.TEXT)
	fun name(): String {
		return SimpleDateFormat("MMMMM yyyy", Locale("es", "ES")).format(this.date)
	}

	@Key(name = "totalMoney", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun totalMoney(): Double? {
		return money?.quantity
	}

	@Key(name = "debt", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun debt(): Double {
		var debt = 0.0

		payments.forEach { payment ->
			if (payment.type != PaymentType.SAVE.ordinal) {
				debt += payment.quantity
			}
		}

		return debt
	}

	@Key(name = "remainingDebt", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun remainingDebt(): Double {
		var remainingDebt = 0.0

		payments.forEach { payment ->
			if (!payment.pay && payment.type != PaymentType.SAVE.ordinal) {
				remainingDebt += payment.quantity
			}
		}

		return remainingDebt
	}

	@Key(name = "freeMoney", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun freeMoney(): Double {
		println()
		return (money?.quantity?.minus(debt()))!! - save()
	}

	@Key(name = "biweekly", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun biweekly(): Double {
		return freeMoney() / 2
	}

	@Key(name = "save", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun save(): Double {
		var save = 0.0

		payments.forEach { payment ->
			if (payment.type == PaymentType.SAVE.ordinal) {
				save += payment.quantity
			}
		}

		return save
	}

	@Key(name = "individual", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun individual(): Double {
		return biweekly() / 2
	}

	@Key(name = "enable", autoCall = true, defaultNullValue = DefaultValue.BOOLEAN)
	fun enable(): Boolean {
		val date = this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
		val startMonthText = "15/${date.monthValue}/${date.year}"
		val endMonth = SimpleDateFormat("dd/MM/yyyy").parse(startMonthText)
		val endMonthCalendar = Calendar.getInstance()

		endMonthCalendar.isLenient = false
		endMonthCalendar.time = endMonth
		endMonthCalendar.add(Calendar.MONTH, 1)

		if (Date() >= endMonth && Date() <= endMonthCalendar.time) {
			return true
		}

		return false
	}

}