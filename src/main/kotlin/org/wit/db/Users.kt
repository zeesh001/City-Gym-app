package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one user.
//       Database wise, this is the table object.

object Users : Table("users") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val phone = integer ("phone")
    val address = varchar ("address",255)
    val gender = varchar ("gender",50)
    val timing_slot = varchar ("timing_slot",50)
    val trainer = varchar ("trainer",50)
    val service = varchar ("service",50)
}