package org.wit.utilities

import org.jetbrains.exposed.sql.ResultRow
import org.wit.db.Users
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
    service = it[Users.service]

)