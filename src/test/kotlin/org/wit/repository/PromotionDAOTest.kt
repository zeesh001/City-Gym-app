package org.wit.repository

import org.wit.helpers.promotions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.db.Promotions
import org.wit.domain.PromotionDTO
import org.wit.helpers.populatePromotionTable
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val promotion1 = promotions.get(0)
private val promotion2 = promotions.get(1)
private val promotion3 = promotions.get(2)

class PromotionDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreatePromotion {

        @Test
        fun `multiple promotion added to table can be retrieved successfully`() {
            transaction {

                val promotionDAO = populatePromotionTable()

                assertEquals(3, promotionDAO.getAll().size)
                assertEquals(promotion1, promotionDAO.findByPromotionCategory(promotion1.package_cat))
                assertEquals(promotion2, promotionDAO.findByPromotionCategory(promotion2.package_cat))
                assertEquals(promotion3, promotionDAO.findByPromotionCategory(promotion3.package_cat))
            }
        }
    }

    @Nested
    inner class ReadPromotions {

        @Test
        fun `getting all Promotions from a populated table returns all rows`() {
            transaction {
                val promotionDAO = populatePromotionTable()
                //Act & Assert
                assertEquals(3, promotionDAO.getAll().size)
            }
        }

        @Test
        fun `get all Promotions over empty table returns none`() {
            transaction {

                SchemaUtils.create(Promotions)
                val promotionDAO = PromotionDAO()

                //Act & Assert
                assertEquals(0, promotionDAO.getAll().size)
            }
        }

        @Test
        fun `get promotions by promotion Category that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val promotionDAO = populatePromotionTable()
                //Act & Assert
                assertEquals(null,promotionDAO.findByPromotionCategory("yearlyy"))}
        }

        @Test
        fun `get promotions by promotion Category that exists, results in a correct promotion returned`() {
            transaction {
                val promotionDAO = populatePromotionTable()
                //Act & Assert
                assertEquals(promotion1, promotionDAO.findByPromotionCategory("daily"))
                assertEquals(promotion3, promotionDAO.findByPromotionCategory("monthly"))
            }
        }
    }

    @Nested
    inner class UpdatePromotions {

        @Test
        fun `updating existing promotions in table results in successful update`() {
            transaction {
                val promotionDAO = populatePromotionTable()

                //Act & Assert
                val promotion3updated = PromotionDTO(1,"daily", "swimming", "50%"  )
                promotionDAO.updatePromotion(promotion3updated.id, promotion3updated)
                assertEquals(promotion3updated, promotionDAO.findByPromotionId(1))
            }
        }

        @Test
        fun `updating non-existant promotion in table results in no updates`() {
            transaction {
                val promotionDAO = populatePromotionTable()

                //Act & Assert
                val promotion4updated = PromotionDTO(1,"yearlly", "swimming", "50%"  )
                promotionDAO.updatePromotion(1, promotion4updated)
                assertEquals(null, promotionDAO.findByPromotionCategory("yearlyy"))
                assertEquals(3, promotionDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeletePromotions {

        @Test
        fun `deleting a non-existant promotion in table results in no deletion`() {
            transaction {
                transaction {

                    val promotionDAO = populatePromotionTable()

                    //Act & Assert
                    assertEquals(3, promotionDAO.getAll().size)
                    promotionDAO.deleteById(44)
                    assertEquals(3, promotionDAO.getAll().size)
                }
            }
        }

        @Test
        fun `deleting an existing promotion in table results in record being deleted`() {
            transaction {
                val promotionDAO = populatePromotionTable()
                //Act & Assert
                assertEquals(3, promotionDAO.getAll().size)
                promotionDAO.deleteById(promotion3.id)
                assertEquals(2, promotionDAO.getAll().size)
            }
        }

    }
}
