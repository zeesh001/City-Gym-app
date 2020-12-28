package org.wit.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id")//.autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val phone = integer ("phone")
    val address = varchar ("address",255)
    val gender = varchar ("gender",50)
    val timing_slot = varchar ("timing_slot",50)
    val trainer = varchar ("trainer",50)
    val service_name = varchar ("service_name",50)//.references(Services.service_name, onDelete = ReferenceOption.CASCADE)
   // val started = datetime("started")
}