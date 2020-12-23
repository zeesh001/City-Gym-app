package org.wit.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.wit.repository.UserDAO
import io.javalin.http.Context
import org.wit.domain.UserDTO

object CityGymAPI {
    val userDao = UserDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context)
    {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null)
        {
          ctx.json(user)
        }
    }
    fun getUserByEmail(ctx: Context)
    {
        val user = userDao.findByEmail(ctx.pathParam("email").toString())
        if (user != null)
        {
            ctx.json(user)
        }

    }
    fun getUserByPhone(ctx: Context)
    {
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

    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())
        userDao.update(
            id = ctx.pathParam("user-id").toInt(),
            userDTO=user)
    }

    fun addUser(ctx: Context)
    {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<UserDTO>(ctx.body())

        val old_id = userDao.findById(user.id)
        val old_email = userDao.findByEmail(user.email)
        val old_phone = userDao.findByPhone(user.phone)
        if (old_id == null && old_email == null && old_phone == null)
        {
            userDao.save(user)
            ctx.json(user)
        }
        else
            ctx.json("User already exist")



    }
}