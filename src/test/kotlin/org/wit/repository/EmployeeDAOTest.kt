package org.wit.repository

import junit.framework.Assert.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Employees
import org.wit.db.Users
import org.wit.domain.EmployeeDTO
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.helpers.populateUserTable

//retrieving some test data from Fixtures
val employee1 = employees.get(0)
val employee2 = employees.get(1)
val employee3 = employees.get(2)

class EmployeeDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateEmployees{

        @Test
        fun `multiple Employees added to table can be retrieved successfully`() {
            transaction {
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                assertEquals(3, employeeDAO.getAll().size)
                assertEquals(employee1, employeeDAO.findById(employee1.id))
                assertEquals(employee2, employeeDAO.findById(employee2.id))
                assertEquals(employee3, employeeDAO.findById(employee3.id))

            }
        }



    }

    @Nested
    inner class ReadEmployees {


        @Test
        fun `getting all Employees from a populated table returns all rows`() {
            transaction {
                val employeeDAO = populateEmployeeTable()
                //Act & Assert
                assertEquals(3, employeeDAO.getAll().size)
            }
        }

        @Test
        fun `get all Employees over empty table returns none`() {
            transaction {
                SchemaUtils.create(Employees)
                val employeeDAO = EmployeeDAO()

                //Act & Assert
                assertEquals(0, employeeDAO.getAll().size)
            }
        }

        @Test
        fun `get Employee by id that doesn't exist, results in no Employee returned`() {
            transaction {

                //Arrange - create and populate table with three Employees
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                assertEquals(null, employeeDAO.findById(6))
            }
        }

        @Test
        fun `get Employee by id that exists, results in correct Employee returned`() {
            transaction {

                //Arrange - create and populate table with three Employees
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                assertEquals(employee2  , employeeDAO.findById(employee2.id))
            }
        }

        @Test
        fun `getting all Employees by designation from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate table with three Employees
                val employeeDAO = populateEmployeeTable()
                //Act & Assert
                assertEquals(1, employeeDAO.findEmployeeByDesignation("trainer").size)
            }
        }


        @Test
        fun `get Employee by phone that doesn't exist, results in no Employee returned`() {
            transaction {

                //Arrange - create and populate table with three Employees
                val employeeDAO = populateEmployeeTable()


                //Act & Assert
                assertEquals(null, employeeDAO.findByPhone(7777))
            }
        }

        @Test
        fun `get Employee by phone that exists, results in a correct Employee returned`() {
            transaction {
                //Arrange - create and populate table with three Employees
                val employeeDAO = populateEmployeeTable()
                //Act & Assert
                assertEquals(5689, employeeDAO.findByPhone(5689)?.e_phone)
            }
        }
    }

    @Nested
    inner class UpdateEmployees {

        @Test
        fun `updating existing Employee in table results in successful update`() {
            transaction {
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                val employee3Updated = EmployeeDTO(3, "new username", 5689 ,"aspen")
                employeeDAO.update(employee3.id, employee3Updated)
                assertEquals(employee3Updated, employeeDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existant Employee in table results in no updates`() {
            transaction {
                //Arrange - create and populate table with three users
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                val employee4Updated = EmployeeDTO(4, "new username",5689 ,"aspen" )
                employeeDAO.update(4, employee4Updated)
                assertEquals(null, employeeDAO.findById(4))
                assertEquals(3, employeeDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteUsers{

        @Test
        fun `deleting a non-existant Employee in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with three users
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                assertEquals(3, employeeDAO.getAll().size)
                employeeDAO.delete(4)
                assertEquals(3, employeeDAO.getAll().size)
            }
        }


        @Test
        fun `deleting an existing Employee in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val employeeDAO = populateEmployeeTable()

                //Act & Assert
                assertEquals(3, employeeDAO.getAll().size)
                employeeDAO.delete(employee3.id)
                assertEquals(2, employeeDAO.getAll().size)
            }
        }


    }

}