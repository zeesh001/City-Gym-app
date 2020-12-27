package org.wit.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.wit.repository.UserDAO
import io.javalin.http.Context
import org.wit.domain.ServiceDTO
import org.wit.domain.UserDTO
import org.wit.repository.ServiceDAO

object CityGymAPI {
   private val userDao = UserDAO()
   private val serviceDAO = ServiceDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null)
        {
          ctx.json(user)
        }
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null)
        {
            ctx.json(user)
        }

    }

    fun getUserByPhone(ctx: Context) {
        val user = userDao.findByPhone(ctx.pathParam("phone").toInt())
        if (user != null)
        {
            ctx.json(user)
        }

    }

    fun deleteUserByPhone(ctx: Context){
        userDao.deleteByPhone(ctx.pathParam("phone").toInt())
    }

    fun deleteUser(ctx: Context){
        userDao.delete(ctx.pathParam("user-id").toInt())
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val verify = mapper.readValue<UserDTO>(ctx.body())
        val user = mapper.readValue<UserDTO>(ctx.body())
            userDao.save(user)
            ctx.json(user)
           serviceDAO.update(verify.service_name)
    }

    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        userDao.update(
            id = ctx.pathParam("user-id").toInt(),
            userDTO=user)
    }
    //--------------------------------------------------------------
    // ServiceDAO specifics
    //-------------------------------------------------------------

    fun updateName(ctx: Context){
        val mapper = jacksonObjectMapper()
        val service = mapper.readValue<ServiceDTO>(ctx.body())
        serviceDAO.updateServiceName(
            serv_name = ctx.pathParam("service_name"),
            serviceDTO = service )
    }
    fun getAllServices(ctx: Context) {
        ctx.json(serviceDAO.getAll())
    }

//    fun getServicesById(ctx: Context) {
//        val service = serviceDAO.findByServiceId(ctx.pathParam("id"))
//        if (service != null){
//            ctx.json(service)
//        }
//        else{ ctx.json("not exist") }
//    }

    fun getServicesByName(ctx: Context) {
        val service = serviceDAO.findByServiceName(ctx.pathParam("service_name"))
        if (service != null){
            ctx.json(service)
        }
        else{ ctx.json("not exist") }
    }


    fun deleteService(ctx: Context){
        val service = serviceDAO.deleteByServiceName(ctx.pathParam("service_namr"))
        if (service != null){
            ctx.json(service)
        }
        else{ ctx.json("not exist") }
    }

    fun addService(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val service = mapper.readValue<ServiceDTO>(ctx.body())
        serviceDAO.save(service)
        ctx.json(service)
            }

}
