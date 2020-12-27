package org.wit.db

import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Services : Table("services") {
    val id = integer("id").autoIncrement().primaryKey()
    val service_name = varchar("service_name", 50)
    val enrolled_user = integer("enrolled_user")
   }