package org.wit.helpers

import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import org.wit.db.Services
import org.wit.db.Users
import org.wit.domain.ServiceDTO
import org.wit.domain.UserDTO
import org.wit.repository.*

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val id = 2
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val validphone = 1090
val validaddress="Gk"
val validgender="Male"
val validtiming_slot = "Morning"
val validtrainer="yes"
val validservice_name="swimming"
//val validstarted = DateTime.now()


val services = arrayListOf<ServiceDTO>(
    ServiceDTO(id = 1, service_name = "workout",enrolled_user = 1),
    ServiceDTO(id = 2, service_name = "swiming",enrolled_user = 1),
    ServiceDTO(id = 3, service_name = "kick boxing",enrolled_user = 1)
)
//, started = DateTime.now()
val users = arrayListOf<UserDTO>(
    UserDTO (id = 1 ,name="Alice",email ="alice@wonderland.com",phone =5689 ,address="aspen",gender="Male",timing_slot = "Morning",trainer = "yes",service_name = "workout" ),
    UserDTO (id = 2 ,name="Bob Cat",email ="mary@contrary.com",phone =6666 ,address="aspen close",gender="female",timing_slot = "Morning",trainer = "yes",service_name = "swimming" ),
    UserDTO (id = 3 ,name="Mary Contrary",email ="bob@cat.ie",phone =9999 ,address="villa rose",gender="male",timing_slot = "evening",trainer = "yes",service_name = "workout" ),
    UserDTO (id = 4 ,name="Carol Singe",email ="carol@singer.com",phone =7777 ,address="view mount",gender="others",timing_slot = "Morning",trainer = "yes",service_name = "swimming" )
)

fun populateServiceTable(): ServiceDAO {
    SchemaUtils.create(Services)
    val serviceDAO = ServiceDAO()
    serviceDAO.save(services.get(0))
    serviceDAO.save(services.get(1))
    serviceDAO.save(services.get(2))
    return serviceDAO
}
fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
