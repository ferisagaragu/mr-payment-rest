package org.pechblenda.mrpaymentrest.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.Lob

import java.util.UUID

@Entity
@Table(name = "notification")
class Notification(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var uuid: UUID,

	@Lob
	var webHockUrl: String
)