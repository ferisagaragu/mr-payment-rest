package org.pechblenda.mrpaymentrest.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.OneToMany

import java.util.UUID
import java.util.Date
import org.pechblenda.service.annotation.Key
import org.pechblenda.service.enum.DefaultValue

@Entity
@Table(name = "period")
class Period(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var date: Date,

	@OneToMany(mappedBy = "period")
	var payments: MutableList<Payment>
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		date = Date(),
		payments = mutableListOf()
	)

	constructor(date: Date): this() {
		this.uuid = UUID.randomUUID()
		this.date = date
		this.payments = mutableListOf()
	}

	@Key(name = "debt", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun debt(): Double {
		var debt = 0.0

		payments.forEach { payment -> debt += payment.quantity }

		return debt
	}

	@Key(name = "remainingDebt", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun remainingDebt(): Double {
		var remainingDebt = 0.0

		payments.forEach { payment ->
			if (!payment.pay) {
				remainingDebt += payment.quantity
			}
		}

		return remainingDebt
	}

	@Key(name = "freeMoney", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun freeMoney(): Double {
		return 27800 - debt()
	}

	@Key(name = "biweekly", autoCall = true, defaultNullValue = DefaultValue.NUMBER)
	fun biweekly(): Double {
		return freeMoney() / 2
	}

}