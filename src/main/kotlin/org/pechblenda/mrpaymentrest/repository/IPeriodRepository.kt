package org.pechblenda.mrpaymentrest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.pechblenda.mrpaymentrest.entity.Period

import java.util.UUID
import java.util.Date

interface IPeriodRepository: JpaRepository<Period, UUID> {
	fun existsByDate(date: Date): Boolean

	@Query("select period from Period period order by period.date desc")
	override fun findAll(): MutableList<Period>

	@Query(
		"select sum(payment.quantity) from Payment payment " +
		"inner join payment.period period where period.uuid = :periodUuid"
	)
	fun sumDebtByPeriodUuid(periodUuid: UUID): Double?

}