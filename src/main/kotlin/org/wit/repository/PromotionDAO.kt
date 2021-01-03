package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Packages
import org.wit.db.Promotions
import org.wit.db.Services
import org.wit.db.Users
import org.wit.domain.PackageDTO
import org.wit.domain.PromotionDTO
import org.wit.domain.ServiceDTO
import org.wit.utilities.mapToPackageDTO
import org.wit.utilities.mapToPromotionDTO
import org.wit.utilities.mapToServiceDTO

class PromotionDAO {

    fun getAll(): ArrayList<PromotionDTO> {
        val promotionsList: ArrayList<PromotionDTO> = arrayListOf()
        transaction {
            Promotions.selectAll().map {
                promotionsList.add(mapToPromotionDTO(it)) }
        }
        return promotionsList
    }

    fun updatePromotion(id:Int,promotionDTO: PromotionDTO):Int{
        return transaction {
            Promotions.update({ Promotions.id eq id }){
                it[package_cat] = promotionDTO.package_cat
                it[service_name] = promotionDTO.service_name
                it[discount] = promotionDTO.discount
            }
        }
    }

    fun findByPromotionId(id: Int): PromotionDTO?{
        return transaction {
            Promotions.select() { Promotions.id eq id}.map{mapToPromotionDTO(it)}
                .firstOrNull()
        }
    }

    fun findByPromotionCategory(package_cat: String): PromotionDTO?{
        return transaction {
            Promotions.select() { Promotions.package_cat eq package_cat}.map{mapToPromotionDTO(it)}
                .firstOrNull()
        }
    }

    fun save(promotionDTO: PromotionDTO){
        transaction {
            Promotions.insert {
                it[id] = promotionDTO.id
                it[package_cat] = promotionDTO.package_cat
                it[service_name] = promotionDTO.service_name
                it[discount] = promotionDTO.discount
            }
        }
    }

    fun deleteById(id : Int) {
        return transaction{ Promotions.deleteWhere{
            Promotions.id eq id
        } } }
}
