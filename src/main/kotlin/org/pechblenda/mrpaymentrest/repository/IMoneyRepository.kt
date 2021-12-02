package org.pechblenda.mrpaymentrest.repository

import java.util.Optional
import java.util.UUID

import org.pechblenda.mrpaymentrest.entity.Money

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IMoneyRepository: JpaRepository<Money, UUID> {

	@Query(
		nativeQuery = true,
		value = "select * from money where priority = :priority LIMIT 1"
	)
	fun findByPriority(priority: Int): Optional<Money>

}