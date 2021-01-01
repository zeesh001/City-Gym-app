package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.wit.repository.UserDAO
import io.javalin.http.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.wit.db.Services
import org.wit.domain.ServiceDTO
import org.wit.domain.UserDTO
import org.wit.repository.ServiceDAO
import org.wit.utilities.jsonToObject

object CityGymAPI {
   private val userDao = UserDAO()
   private val serviceDAO = ServiceDAO()

    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null)
        {
          ctx.json(user)
            ctx.status(200)
        }
        else
        {
            ctx.status(404)
        }
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null)
        {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }

    }

    fun getUsersByService(ctx: Context) {
        val service = userDao.findUserByServiceName(ctx.pathParam("service_name"))
        if (service != null){
            ctx.json(service)
            ctx.status(200)
        }
    else{
            ctx.json("not exist")
        ctx.status(404)
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
       val user = userDao.deleteByPhone(ctx.pathParam("phone").toInt())
        if(user != 0 )
            ctx.status(204)
        else
            ctx.status(404)
        }

    fun deleteUser(ctx: Context){
        val value = ctx.pathParam("id").toInt()
        val service = userDao.findById(value)
        decrementService(service!!.service_name)
        val user = userDao.delete(value)
        if (user != 0) {
            ctx.status(204)
        }
            else
            ctx.status(404)
    }

    fun addUser(ctx: Context) {
        val user : UserDTO = jsonToObject(ctx.body())
        val verify = user
        val userId = userDao.save(user)
        if (userId != null) {
           // user.id = userId
            ctx.json(user)
            ctx.status(201)
            update(verify.service_name)
        }
    }

    fun updateUser(ctx: Context){
        val user : UserDTO = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), userDTO=user)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    //--------------------------------------------------------------
    // ServiceDAO specifics
    //-------------------------------------------------------------

    fun updateName(ctx: Context){
        val service : ServiceDTO = jsonToObject(ctx.body())
        serviceDAO.updateServiceName(
            serv_name = ctx.pathParam("service_name"),
            serviceDTO = service )
    }

    fun getAllServices(ctx: Context) {
        ctx.json(serviceDAO.getAll())
    }

    fun getServicesByName(ctx: Context) {
        val service = serviceDAO.findByServiceName(ctx.pathParam("service_name"))
        if (service != null){
            ctx.json(service)
            ctx.status(200)
        }
        else{ ctx.json("not exist")
            ctx.status(404)
        }
    }

    fun deleteService(ctx: Context){
        val service = serviceDAO.deleteByServiceName(ctx.pathParam("service_name"))

            ctx.json(service)

    }

    fun addService(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val service = mapper.readValue<ServiceDTO>(ctx.body())
        serviceDAO.save(service)
        ctx.json(service)
            }

    //update enrolled users
    fun update(service: String ){
        val check =  serviceDAO.findByServiceName(service)
        val check1 =  check!!.enrolled_user
        val check2 = check1+1
        transaction {
            Services.update ({
                Services.service_name eq service}){
                it[enrolled_user] = check2     }

        }
    }

    fun decrementService(service: String ){
        val check =  serviceDAO.findByServiceName(service)
        val check1 =  check!!.enrolled_user
        val check2 = check1-1
        transaction {
            Services.update ({
                Services.service_name eq service}){
                it[enrolled_user] = check2     }


        }
    }



}
