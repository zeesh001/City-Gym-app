package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Services
import org.wit.db.Users
import org.wit.domain.ServiceDTO
import org.wit.utilities.mapToServiceDTO

class ServiceDAO {

    fun getAll(): ArrayList<ServiceDTO> {
        val servicesList: ArrayList<ServiceDTO> = arrayListOf()
        transaction {
            Services.selectAll().map {
                servicesList.add(mapToServiceDTO(it)) }
        }
        return servicesList
    }

   fun updateServiceName(serv_name: String,serviceDTO: ServiceDTO){
       return transaction {
           Services.update ({ Services.service_name eq serv_name }){
               it[service_name] = serviceDTO.service_name
           }
       }
   }

    //Find a specific Service by Service NAME
    fun findByServiceName(service: String): ServiceDTO?{
        return transaction {
            Services.select() { Services.service_name eq service}.map{mapToServiceDTO(it)}
                .firstOrNull()
        }
    }

//    fun findByServiceId(id: Int): ServiceDTO?{
//        return transaction {
//            Services.select() { Services.id eq id}.map{mapToServiceDTO(it)}
//                .firstOrNull()
//        }
//    }

    //Save a Service to the database
    fun save(serviceDTO: ServiceDTO){
        transaction {
            Services.insert {
                it[id] = serviceDTO.id
                it[service_name] = serviceDTO.service_name
                it[enrolled_user] = serviceDTO.enrolled_user
            }
        }
    }

    fun deleteByServiceName(service_name : String) {
        return transaction{ Services.deleteWhere{
            Services.service_name eq service_name
        }
        }
    }

    //update enrolled users
    fun update(service: String ){
       val check =  findByServiceName(service)
       val check1 =  check!!.enrolled_user
        val check2 = check1+1
        transaction {
            Services.update ({
                Services.service_name eq service}){
                 it[enrolled_user] = check2     }


            }
        }
    }
