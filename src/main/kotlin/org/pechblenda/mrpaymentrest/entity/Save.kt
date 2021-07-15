package org.pechblenda.mrpaymentrest.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

import java.util.Date
import java.util.UUID

@Entity
@Table(name = "save")
class Save (
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var periodDate: Date,
	var quantity: Double,
	var used: Boolean
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		periodDate = Date(),
		quantity = 0.0,
		used = false
	)

	constructor(
		periodDate: Date,
		quantity: Double
	): this() {
		this.uuid = UUID.randomUUID()
		this.periodDate = periodDate
		this.quantity = quantity
		this.used = false
	}

}