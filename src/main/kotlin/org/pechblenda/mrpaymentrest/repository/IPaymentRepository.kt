package org.pechblenda.mrpaymentrest.repository

import org.pechblenda.mrpaymentrest.entity.Payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import java.util.UUID
import java.util.Date

interface IPaymentRepository: JpaRepository<Payment, UUID> {

	@Query(
		nativeQuery = true,
		value = "select pay.* from payment pay " +
			"inner join period per on pay.period_uuid = per.uuid " +
			"where pay.type = 3 and " +
			"to_char(per.date, 'MM/YYYY') = to_char(to_date(:periodDate, 'yyyy-mm-dd'), 'MM/YYYY')"
	)
	fun findAllSaveByPeriodDate(
		@Param("periodDate") periodDate: Date
	): List<Payment>

	@Query(
		nativeQuery = true,
		value = "select pay.* from payment pay " +
			"inner join period per on pay.period_uuid = per.uuid " +
			"where pay.type = 2 and " +
			"to_char(per.date, 'MM/YYYY') = to_char(to_date(:periodDate, 'yyyy-mm-dd'), 'MM/YYYY')"
	)
	fun findAllRecurrentByPeriodDate(
		@Param("periodDate") periodDate: Date
	): List<Payment>

	@Query(
		nativeQuery = true,
		value = "select pay.* from payment pay " +
			"inner join period per on pay.period_uuid = per.uuid " +
			"where pay.type = 1 and pay.month_count > 1 " +
			"and to_char(per.date, 'MM/YYYY') = to_char(to_date(:periodDate, 'yyyy-mm-dd'), 'MM/YYYY')"
	)
	fun findAllMonthlyByPeriodDate(
		@Param("periodDate") periodDate: Date
	): List<Payment>

	@Query(
		"select payment from Payment payment " +
		"inner join payment.period period where " +
		"period.uuid = :periodUuid order by payment.createDate"
	)
	fun findAllByPeriodUuid(periodUuid: UUID): List<Payment>

	@Query(
		"select sum(payment.quantity) from Period period " +
		"inner join period.payments payment where period.uuid = " +
		"(select pay.period.uuid from Payment pay where pay.uuid = :paymentUuid)"
	)
	fun sumDebtByPaymentUuid(paymentUuid: UUID): Double?

}