package org.pechblenda.mrpaymentrest.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

import java.util.UUID

@Entity
@Table(name = "alert_config")
class AlertConfig(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var hardQuantity: Double,
	var lowQuantity: Double,
	var mediumQuantity: Double
) {

	constructor(): this(
		uuid = UUID.randomUUID(),
		hardQuantity = 0.0,
		lowQuantity = 0.0,
		mediumQuantity = 0.0
	)

	constructor(
		lowQuantity: Double,
		mediumQuantity: Double,
		hardQuantity: Double
	): this() {
		this.uuid = UUID.randomUUID()
		this.lowQuantity = lowQuantity
		this.mediumQuantity = mediumQuantity
		this.hardQuantity = hardQuantity
	}

}