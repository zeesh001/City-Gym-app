package org.wit.db
import org.jetbrains.exposed.sql.Table

object Packages : Table("packages"){
    val id = integer ("id")
    val package_cat = varchar("package_cat",50)
    val amount = integer("amount")
    val service_name = varchar ("service_name",50)
}