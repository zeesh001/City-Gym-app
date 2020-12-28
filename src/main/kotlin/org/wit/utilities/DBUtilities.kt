package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.Services
import org.wit.db.Users
import org.wit.domain.ServiceDTO
import org.wit.domain.UserDTO

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