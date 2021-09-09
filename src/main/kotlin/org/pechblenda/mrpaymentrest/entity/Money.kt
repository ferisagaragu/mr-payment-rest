package org.pechblenda.mrpaymentrest.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Id

import java.util.UUID

@Entity
@Table(name = "money")
class Money(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,
	var quantity: Double
)