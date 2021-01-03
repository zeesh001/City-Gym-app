package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.*
import org.wit.domain.*

fun mapToUserDTO(it: ResultRow) = UserDTO(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    phone = it[Users.phone],
    address = it[Users.address],
    gender = it[Users.gender],
    timing_slot = it[Users.timing_slot],
    trainer = it[Users.trainer],
    service_name = it[Users.service_name],
  //  started = it[Users.started]
)

fun mapToServiceDTO(it: ResultRow) = ServiceDTO(
    id = it[Services.id],
    service_name = it[Services.service_name],
    enrolled_user = it[Services.enrolled_user]
)

fun mapToPackageDTO(it: ResultRow) = PackageDTO(
    id = it[Packages.id],
    package_cat = it[Packages.package_cat],
    amount = it[Packages.amount],
    service_name = it[Packages.service_name]
)

fun mapToPromotionDTO(it: ResultRow) = PromotionDTO(
    id = it[Promotions.id],
    package_cat = it[Promotions.package_cat],
    service_name = it[Promotions.service_name],
    discount = it[Promotions.discount]
)

fun mapToEmployeeDTO(it: ResultRow) = EmployeeDTO(
    id = it[Employees.id],
    e_name = it[Employees.e_name],
    e_phone = it[Employees.e_phone],
    designation = it[Employees.designation],
    )