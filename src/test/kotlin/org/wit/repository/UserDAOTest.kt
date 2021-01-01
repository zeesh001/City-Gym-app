package org.wit.repository

import junit.framework.Assert.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Users
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.helpers.populateUserTable

//retrieving some test data from Fixtures
val user1 = users.get(0)
val user2 = users.get(1)
val user3 = users.get(2)

class UserDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateUsers{

        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate table with three users
               //val serviceDAO = populateServiceTable()
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                assertEquals(user1, userDAO.findById(user1.id))
                assertEquals(user2, userDAO.findById(user2.id))
                assertEquals(user3, userDAO.findById(user3.id))

            }
        }



    }

    @Nested
    inner class ReadUsers {


        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                //Arrange - create and setup userDAO object
                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                //Act & Assert
                assertEquals(0, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user2  , userDAO.findByEmail(user2.email))
            }
        }

        @Test
        fun `getting all users by service name from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                //Act & Assert
                assertEquals(2, userDAO.findUserByServiceName("workout").size)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findById(4))
            }
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                //Act & Assert
                assertEquals(null, userDAO.findById(4))
            }
        }

        @Test
        fun `get user by phone that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()


                //Act & Assert
                assertEquals(null, userDAO.findByPhone(7777))
            }
        }

        @Test
        fun `get user by phone that exists, results in a correct user returned`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                //Act & Assert
                assertEquals(null, userDAO.findByPhone(7777))
            }
        }
    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()

                //Act & Assert
                val user3Updated = UserDTO(3, "new username", "new@email.ie",phone =5689 ,address="aspen",gender="Male",timing_slot = "Morning",trainer = "yes",service_name = "Workout")
                userDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated, userDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                val user4Updated = UserDTO(4, "new username", "new@email.ie",phone =5689 ,address="aspen",gender="Male",timing_slot = "Morning",trainer = "yes",service_name = "Workout" )
                userDAO.update(4, user4Updated)
                assertEquals(null, userDAO.findById(4))
                assertEquals(3, userDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteUsers{

        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(4)
                assertEquals(3, userDAO.getAll().size)
            }
        }
        @Test
        fun `deleting by phone a non-existant user in table results in no deletion`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.deleteByPhone(1111)
                assertEquals(3, userDAO.getAll().size)
            }
        }


        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(user3.id)
                assertEquals(2, userDAO.getAll().size)
            }
        }


        @Test
        fun `deleting by phone an existing user in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.deleteByPhone(user3.phone)
                assertEquals(2, userDAO.getAll().size)
            }
        }

    }

}