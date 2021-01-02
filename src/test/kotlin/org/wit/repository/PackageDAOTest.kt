package org.wit.repository
import org.wit.helpers.packages
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Packages
import org.wit.domain.PackageDTO
import org.wit.helpers.populatePackageTable
import org.wit.helpers.populateServiceTable
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val package1 = packages.get(0)
private val package2 = packages.get(1)
private val package3 = packages.get(2)

class PackageDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreatePackage {

        @Test
        fun `multiple paclages added to table can be retrieved successfully`() {
            transaction {

                val packageDAO = populatePackageTable()

                assertEquals(3, packageDAO.getAll().size)
                assertEquals(package1, packageDAO.findByPackageCategory(package1.package_cat))
                assertEquals(package2, packageDAO.findByPackageCategory(package2.package_cat))
                assertEquals(package3, packageDAO.findByPackageCategory(package3.package_cat))
            }
        }
    }

    @Nested
    inner class ReadPackages {

        @Test
        fun `getting all Packages from a populated table returns all rows`() {
            transaction {
                val packageDAO = populatePackageTable()
                //Act & Assert
                assertEquals(3, packageDAO.getAll().size)
            }
        }

        @Test
        fun `get all Packages over empty table returns none`() {
            transaction {

                SchemaUtils.create(Packages)
                val packageDAO = PackageDAO()

                //Act & Assert
                assertEquals(0, packageDAO.getAll().size)
            }
        }

        @Test
        fun `get Packages by Package Category that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val packageDAO = populatePackageTable()
                //Act & Assert
                assertEquals(null,packageDAO.findByPackageCategory("yearlyy"))}
        }

        @Test
        fun `get Packages by Package Category that exists, results in a correct package returned`() {
            transaction {
                val packageDAO = populatePackageTable()
                //Act & Assert
                assertEquals(package1, packageDAO.findByPackageCategory("daily"))
                assertEquals(package3, packageDAO.findByPackageCategory("monthly"))
            }
        }
    }

    @Nested
    inner class UpdatePackages {

        @Test
        fun `updating existing Packages in table results in successful update`() {
            transaction {
                val packageDAO = populatePackageTable()

                //Act & Assert
                val package3updated = PackageDTO(1,"daily",5 , "swimming", "50%"  )
                packageDAO.updatePackage(package3updated.id, package3updated)
                assertEquals(package3updated, packageDAO.findByPackageId(1))
            }
        }

        @Test
        fun `updating non-existant package in table results in no updates`() {
            transaction {
                val packageDAO = populatePackageTable()

                //Act & Assert
                val package4updated = PackageDTO(1,"yearlly",5 , "swimming", "50%"  )
                packageDAO.updatePackage(1, package4updated)
                assertEquals(null, packageDAO.findByPackageCategory("yearlyy"))
                assertEquals(3, packageDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeletePackages {

        @Test
        fun `deleting a non-existant package in table results in no deletion`() {
            transaction {
                transaction {

                    val packageDAO = populatePackageTable()

                    //Act & Assert
                    assertEquals(3, packageDAO.getAll().size)
                    packageDAO.deleteById(44)
                    assertEquals(3, packageDAO.getAll().size)
                }
            }
        }

        @Test
        fun `deleting an existing package in table results in record being deleted`() {
            transaction {
                val packageDAO = populatePackageTable()
                //Act & Assert
                assertEquals(3, packageDAO.getAll().size)
                packageDAO.deleteById(package3.id)
                assertEquals(2, packageDAO.getAll().size)
            }
        }

    }
}
