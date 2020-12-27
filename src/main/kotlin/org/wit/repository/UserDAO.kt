package org.wit.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.utilities.mapToUserDTO
import org.wit.db.Users
import org.wit.domain.UserDTO

class UserDAO {

    fun getAll(): ArrayList<UserDTO> {
        val userList: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUserDTO(it)) }
        }
        return userList
    }

    fun findById(id: Int): UserDTO?{
        return transaction {
            Users.select() {
                Users.id eq id}
                .map{mapToUserDTO(it)}
                .firstOrNull()
        }
    }

    fun save(userDTO: UserDTO){
        transaction {
            Users.insert {
                it[name] = userDTO.name
                it[email] = userDTO.email
                it[phone] =  userDTO.phone
                it[address] = userDTO.address
                it[gender] = userDTO.gender
                it[timing_slot] = userDTO.timing_slot
                it[trainer] = userDTO.trainer
                it[service_name] = userDTO.service_name
                it[started] = userDTO.started
            }
        }
    }

    fun findByEmail(email: String):UserDTO? {
        return transaction {
            Users.select(){
                Users.email eq email }.map{mapToUserDTO(it)}.firstOrNull()
        }
    }

    fun findByPhone(phone: Int):UserDTO? {
        return  transaction {
            Users.select(){
                Users.phone eq phone }.map{mapToUserDTO(it)}.firstOrNull()
        }
    }

    fun deleteByPhone(phone: Int) {
        return transaction{ Users.deleteWhere{
            Users.phone eq phone
        }
        }
    }

    fun delete(id: Int):Int{
        return transaction{ Users.deleteWhere{
            Users.id eq id
        }
        }
    }

    fun update(id: Int, userDTO: UserDTO){
        transaction {
            Users.update ({
                Users.id eq id}) {
                it[name] = userDTO.name
                it[email] = userDTO.email
                it[phone] =  userDTO.phone
                it[address] = userDTO.address
                it[gender] = userDTO.gender
                it[timing_slot] = userDTO.timing_slot
                it[trainer] = userDTO.trainer
                it[service_name] = userDTO.service_name
                it[started] = userDTO.started
            }
        }
    }


}

