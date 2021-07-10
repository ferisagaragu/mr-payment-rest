package org.pechblenda.mrpaymentrest.repository

import java.util.Date
import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.pechblenda.mrpaymentrest.entity.Period

interface IPeriodRepository: JpaRepository<Period, UUID> {

	fun existsByDate(date: Date): Boolean

	@Query("select period from Period period order by period.date desc")
	override fun findAll(): MutableList<Period>

}