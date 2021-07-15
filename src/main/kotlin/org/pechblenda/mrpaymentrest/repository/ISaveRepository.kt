package org.pechblenda.mrpaymentrest.repository

import java.util.UUID

import org.pechblenda.mrpaymentrest.entity.Save

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ISaveRepository: JpaRepository<Save, UUID> {

	@Query("select SUM(save.quantity) from Save save")
	fun sumTotalSave(): Double?

}