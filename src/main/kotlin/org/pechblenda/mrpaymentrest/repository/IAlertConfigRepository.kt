package org.pechblenda.mrpaymentrest.repository

import java.util.UUID

import org.pechblenda.mrpaymentrest.entity.AlertConfig

import org.springframework.data.jpa.repository.JpaRepository

interface IAlertConfigRepository: JpaRepository<AlertConfig, UUID>