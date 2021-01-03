package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.wit.repository.UserDAO
import io.javalin.http.Context
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.wit.db.Services
import org.wit.domain.PackageDTO
import org.wit.domain.PromotionDTO
import org.wit.domain.ServiceDTO
import org.wit.domain.UserDTO
import org.wit.repository.PackageDAO
import org.wit.repository.PromotionDAO
import org.wit.repository.ServiceDAO
import org.wit.utilities.jsonToObject

object CityGymAPI {
   private val userDao = UserDAO()
   private val serviceDAO = ServiceDAO()
    private val packageDAO = PackageDAO()
    private val promotionDAO = PromotionDAO()

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
        val user = userDao.findByPhone(ctx.pathParam("number").toInt())
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

    fun getServicesById(ctx: Context) {
        val service = serviceDAO.findByServiceId(ctx.pathParam("id").toInt())
        if (service != null){
            ctx.json(service)
            ctx.status(200)
        }
        else{ ctx.json("not available")
            ctx.status(404)
        }
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

    //--------------------------------------------------------------
    // PackagesDAO specifics
    //-------------------------------------------------------------

    fun getAllPackages(ctx: Context) {
        ctx.json(packageDAO.getAll())
    }

    fun updatePackage(ctx: Context){
        val packge : PackageDTO = jsonToObject(ctx.body())
        packageDAO.updatePackage(
            id = ctx.pathParam("id").toInt(),
            packageDTO = packge )
    }

    fun getPackageById(ctx: Context) {
        val packge = packageDAO.findByPackageId(ctx.pathParam("id").toInt())
        if (packge != null){
            ctx.json(packge)
            ctx.status(200)
        }
        else{ ctx.json("not available")
            ctx.status(404)
        }
    }

    fun getPackageByCategory(ctx: Context) {
        val Packge = packageDAO.findByPackageCategory(ctx.pathParam("package_cat"))
        if (Packge != null){
            ctx.json(Packge)
            ctx.status(200)
        }
        else{ ctx.json("not exist")
            ctx.status(404)
        }
    }

    fun deletePackage(ctx: Context){
        val service = packageDAO.deleteById(ctx.pathParam("id").toInt())
        ctx.json(service)

    }

    fun addPackage(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val Packge = mapper.readValue<PackageDTO>(ctx.body())
        packageDAO.save(Packge)
        ctx.json(Packge)
    }


    //--------------------------------------------------------------
    // PromotionDAO specifics
    //-------------------------------------------------------


    fun getAllPromotions(ctx: Context) {
        ctx.json(promotionDAO.getAll())
    }

    fun updatePromotion(ctx: Context){
        val promotion : PromotionDTO = jsonToObject(ctx.body())
        promotionDAO.updatePromotion(
            id = ctx.pathParam("id").toInt(),
            promotionDTO = promotion )
    }

    fun getPromotionById(ctx: Context) {
        val promotion = promotionDAO.findByPromotionId(ctx.pathParam("id").toInt())
        if (promotion != null){
            ctx.json(promotion)
            ctx.status(200)
        }
        else{ ctx.json("not available")
            ctx.status(404)
        }
    }

    fun getPromotionByCategory(ctx: Context) {
        val promotion = promotionDAO.findByPromotionCategory(ctx.pathParam("package_cat"))
        if (promotion != null){
            ctx.json(promotion)
            ctx.status(200)
        }
        else{ ctx.json("not exist")
            ctx.status(404)
        }
    }

    fun deletePromotion(ctx: Context){
        val promotion = promotionDAO.deleteById(ctx.pathParam("id").toInt())
        ctx.json(promotion)

    }

    fun addPromotion(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val promotion = mapper.readValue<PromotionDTO>(ctx.body())
        promotionDAO.save(promotion)
        ctx.json(promotion)
    }





}
