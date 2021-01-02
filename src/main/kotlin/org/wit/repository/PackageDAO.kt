package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Packages
import org.wit.db.Services
import org.wit.db.Users
import org.wit.domain.PackageDTO
import org.wit.domain.ServiceDTO
import org.wit.utilities.mapToPackageDTO
import org.wit.utilities.mapToServiceDTO

class PackageDAO {

    fun getAll(): ArrayList<PackageDTO> {
        val packagesList: ArrayList<PackageDTO> = arrayListOf()
        transaction {
            Packages.selectAll().map {
                packagesList.add(mapToPackageDTO(it)) }
        }
        return packagesList
    }

    fun updatePackage(id:Int,packageDTO: PackageDTO):Int{
        return transaction {
            Packages.update({ Packages.id eq id }){
                it[package_cat] = packageDTO.package_cat
                it[amount] = packageDTO.amount
                it[service_name] = packageDTO.service_name
                it[discount] = packageDTO.discount
            }
        }
    }

    fun findByPackageId(id: Int): PackageDTO?{
        return transaction {
            Packages.select() { Packages.id eq id}.map{mapToPackageDTO(it)}
                .firstOrNull()
        }
    }

    fun findByPackageCategory(package_cat: String): PackageDTO?{
        return transaction {
            Packages.select() { Packages.package_cat eq package_cat}.map{mapToPackageDTO(it)}
                .firstOrNull()
        }
    }

    fun save(packageDTO: PackageDTO){
        transaction {
            Packages.insert {
                it[id] = packageDTO.id
                it[package_cat] = packageDTO.package_cat
                it[amount] = packageDTO.amount
                it[service_name] = packageDTO.service_name
                it[discount] = packageDTO.discount
            }
        }
    }

    fun deleteById(id : Int) {
        return transaction{ Packages.deleteWhere{
            Packages.id eq id
        } } }
}
