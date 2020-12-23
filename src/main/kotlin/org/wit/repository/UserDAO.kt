package org.wit.repository

import org.wit.domain.UserDTO

class UserDAO {

    private val users = arrayListOf<UserDTO>(
        UserDTO(name = "Alice", email = "alice@wonderland.com",phone = 5689,address = "aspen",gender = "Male",timing_slot ="Morning",trainer = true,service ="Work out",  id = 0),
       // UserDTO(name = "Bob", email = "bob@cat.ie",phone = 5689,address = "aspen",gender = "Male",timing_slot ="Morning",trainer = true, service = "Swiming", id = 1),
        //UserDTO(name = "Mary", email = "mary@contrary.com",phone = 5689,address = "aspen",gender = "Male",timing_slot ="Morning",trainer = true,service = "all" ,id = 2),
        //UserDTO(name = "Carol", email = "carol@singer.com",phone = 5689,address = "aspen",gender = "Male",timing_slot ="Morning",trainer = true,service = "all", id = 3)
    )

    fun getAll(): ArrayList<UserDTO> {
        return users
    }
    fun findById(id: Int): UserDTO?{
        return users.find{it.id == id}
    }
    fun save(userDTO: UserDTO){
        users.add(userDTO)
    }
    fun findByEmail(email: String):UserDTO? {
        return users.find {
            it.email == email
        }
    }
    fun findByPhone(Phone: Int):UserDTO? {
        return users.find {
            it.phone == Phone
        }
    }
    fun deleteByPhone(Phone: Int) {
        var user = findByPhone(Phone)
        users.remove(user)
    }

    fun delete(id: Int) {
        var user = findById(id)
        users.remove(user)
    }

    fun update(id: Int, userDTO: UserDTO){
        var user = findById(id)
        user?.email = userDTO.email
        user?.name = userDTO.name
        user?.id = userDTO.id
        user?.gender = userDTO.gender
        user?.phone = userDTO.phone
        user?.service = userDTO.service
        user?.timing_slot = userDTO.timing_slot
        user?.address = userDTO.address
        user?.trainer = userDTO.trainer
    }


}