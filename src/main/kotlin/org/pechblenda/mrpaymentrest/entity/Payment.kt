package org.pechblenda.mrpaymentrest.entity

import org.pechblenda.mrpaymentrest.enum.PaymentType

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table

import java.util.UUID
import java.util.Date

@Entity
@Table(name = "payment")
class Payment(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var name: String,
	var quantity: Double,
	var startDate: Date?,
	var endDate: Date?,
	var payDate: Date?,
	var totalQuantity: Double?,
	var createDate: Date?,
	var type: Int,
	var monthCount: Int?,

	@Column(columnDefinition = "boolean default false")
	var pay: Boolean,

	@ManyToOne
	var period: Period?
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		name = "",
		quantity = 0.0,
		startDate = null,
		endDate = null,
		payDate = null,
		totalQuantity = null,
		createDate = null,
		type = PaymentType.UNIQUE.ordinal,
		monthCount = null,
		pay = false,
		period = null
	)

	constructor(
		name: String,
		quantity: Double,
		period: Period
	): this() {
		this.uuid = UUID.randomUUID()
		this.name = name
		this.quantity = quantity
		this.type = PaymentType.RECURRENT.ordinal
		this.period = period
	}

	constructor(
		name: String,
		quantity: Double,
		startDate: Date,
		endDate: Date,
		totalQuantity: Double,
		monthCount: Int,
		period: Period
	): this() {
		this.name = name
		this.quantity = quantity
		this.type = PaymentType.MONTHLY.ordinal
		this.startDate = startDate
		this.endDate = endDate
		this.totalQuantity = totalQuantity
		this.monthCount = monthCount - 1
		this.period = period
	}

	@PrePersist
	fun onCreate() {
		if (createDate == null) {
			createDate = Date()
		}
	}

}