package org.pechblenda.mrpaymentrest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import java.util.UUID

import org.pechblenda.mrpaymentrest.entity.Payment

interface IPaymentRepository: JpaRepository<Payment, UUID> {

	@Query(
		nativeQuery = true,
		value =
			"select pay.* from payment pay " +
			"inner join period per on pay.period_uuid = per.uuid " +
			"where pay.type = 2 and to_char(per.date, 'MM/YYYY') = to_char(now(), 'MM/YYYY')"
	)
	fun findAllByRecurrentTrue(): List<Payment>

	@Query(
		nativeQuery = true,
		value =
			"select pay.* from payment pay " +
			"inner join period per on pay.period_uuid = per.uuid " +
			"where pay.type = 1 and pay.month_count > 1 " +
			"and to_char(per.date, 'MM/YYYY') = to_char(now(), 'MM/YYYY')"
	)
	fun findAllByMonthlyTrue(): List<Payment>

	@Query(
		"select payment from Payment payment " +
		"inner join payment.period period where " +
		"period.uuid = :periodUuid order by payment.createDate"
	)
	fun findAllByPeriodUuid(periodUuid: UUID): List<Payment>

}