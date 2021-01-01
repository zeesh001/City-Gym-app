package org.wit.repository

import org.wit.db.Services
import org.wit.domain.ServiceDTO
import org.wit.helpers.populateServiceTable
import org.wit.helpers.services
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.helpers.populateUserTable
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val service1 = services.get(0)
private val service2 = services.get(1)
private val service3 = services.get(2)

class ServiceDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateServices {

        @Test
        fun `multiple services added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
//                val userDAO = populateUserTable()
                val serviceDAO = populateServiceTable()
                //Act & Assert
                assertEquals(3, serviceDAO.getAll().size)
                assertEquals(service1, serviceDAO.findByServiceName(service1.service_name))
                assertEquals(service2, serviceDAO.findByServiceName(service2.service_name))
                assertEquals(service3, serviceDAO.findByServiceName(service3.service_name))
            }
        }
    }

    @Nested
    inner class ReadServices {

        @Test
        fun `getting all Services from a populated table returns all rows`() {
            transaction {
                val serviceDAO = populateServiceTable()
                //Act & Assert
                assertEquals(3, serviceDAO.getAll().size)
            }
        }

        @Test
        fun `get all Services over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Services)
                val serviceDAO = ServiceDAO()

                //Act & Assert
                assertEquals(0, serviceDAO.getAll().size)
            }
        }

        @Test
        fun `get Service by Service name that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val serviceDAO = populateServiceTable()
                //Act & Assert
                assertEquals(null, serviceDAO.findByServiceName("cardio"))}
        }

        @Test
        fun `get service by service name that exists, results in a correct service returned`() {
            transaction {

                val serviceDAO = populateServiceTable()
                //Act & Assert
                assertEquals(service1, serviceDAO.findByServiceName("workout"))
                assertEquals(service3, serviceDAO.findByServiceName("kick boxing"))
            }
        }
    }

    @Nested
    inner class UpdateServices {

        @Test
        fun `updating existing Service in table results in successful update`() {
            transaction {
                val serviceDAO = populateServiceTable()

                //Act & Assert
                val service3updated = ServiceDTO(id = 1,  service_name = "workout",enrolled_user = 1)
                serviceDAO.updateServiceName(service3updated.service_name, service3updated)
                assertEquals(service3updated, serviceDAO.findByServiceName("workout"))
            }
        }

        @Test
        fun `updating non-existant service in table results in no updates`() {
            transaction {
                val serviceDAO = populateServiceTable()

                //Act & Assert
                val service4updated = ServiceDTO(id = 1, service_name = "kick",enrolled_user = 1)
                serviceDAO.updateServiceName("kick", service4updated)
                assertEquals(null, serviceDAO.findByServiceName("kick"))
                assertEquals(3, serviceDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteServices {

        @Test
        fun `deleting a non-existant service in table results in no deletion`() {
            transaction {

                val serviceDAO = populateServiceTable()

                //Act & Assert
                assertEquals(3, serviceDAO.getAll().size)
                serviceDAO.deleteByServiceName("run")
                assertEquals(3, serviceDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing service in table results in record being deleted`() {
            transaction {
                val serviceDAO = populateServiceTable()
                //Act & Assert
                assertEquals(3, serviceDAO.getAll().size)
                serviceDAO.deleteByServiceName(service3.service_name)
                assertEquals(2, serviceDAO.getAll().size)
            }
        }

        }
    }
