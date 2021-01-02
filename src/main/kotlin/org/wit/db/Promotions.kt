package org.wit.db
import org.jetbrains.exposed.sql.Table

object Promotions : Table("promotions"){
    val id = integer ("id")
    val package_cat = varchar("package_cat",50)
    val service_name = varchar ("service_name",50)
    val discount = varchar("discount",50)
}