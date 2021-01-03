package org.wit.db

import org.jetbrains.exposed.sql.Table

object Employees : Table("employees"){
    var id = integer("id")
    var e_name = varchar("e_name",50)
    var e_phone = integer("e_phone")
    var designation = varchar("designation",50)
}