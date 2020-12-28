package org.wit.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import org.junit.jupiter.api.TestInstance
import org.wit.config.DbConfig
import org.wit.helpers.ServerContainer
import kong.unirest.Unirest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.wit.domain.UserDTO
import org.wit.helpers.*
import org.wit.utilities.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityGymAPITest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()




    @Nested
    inner class ReadUsers {

        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<UserDTO> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedUsers.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }



        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/email/${nonExistingEmail}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by phone when phone exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addUser(id , validName, validEmail , validphone ,validaddress , validgender , validtiming_slot , validtrainer ,  validservice_name  )

            val addedUser : UserDTO = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)
            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.phone)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            addUser(id, validName, validEmail , validphone ,validaddress , validgender , validtiming_slot , validtrainer ,  validservice_name  )

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : UserDTO = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.phone)
        }
    }

    @Nested
    inner class CreateUsers {

        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(id, validName, validEmail , validphone ,validaddress , validgender , validtiming_slot , validtrainer ,  validservice_name  )
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : UserDTO = jsonToObject(addResponse.body.toString())
            assertEquals(id, retrievedUser.id)
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)
            assertEquals(validphone, retrievedUser.phone)
            assertEquals(validaddress, retrievedUser.address)
            assertEquals(validgender, retrievedUser.gender)
            assertEquals(validtiming_slot, retrievedUser.timing_slot)
            assertEquals(validtrainer, retrievedUser.trainer)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.phone)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on
            val Uid = 2
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"
            val Uvalidphone = 3045245
            val Uvalidaddress="Gujk"
            val Uvalidgender="feMale"
            val Uvalidtiming_slot = "evening"
            val Uvalidtrainer="no"
            val Uvalidservice_name="swimming"

            val addedResponse = addUser(id, validName, validEmail , validphone ,validaddress , validgender , validtiming_slot , validtrainer ,  validservice_name  )

            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateUser(addedUser.id, updatedName, updatedEmail, Uvalidphone ,Uvalidaddress , Uvalidgender , Uvalidtiming_slot , Uvalidtrainer ,  Uvalidservice_name ).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.id)
            val updatedUser : UserDTO = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(Uid , updatedUser.id)
            assertEquals(updatedName, updatedUser.name)
            assertEquals(updatedEmail, updatedUser.email)
            assertEquals(Uvalidphone, updatedUser.phone)
            assertEquals(Uvalidgender, updatedUser.gender)
            assertEquals(Uvalidtiming_slot, updatedUser.timing_slot)
            assertEquals(Uvalidtrainer, updatedUser.trainer)
            assertEquals(Uvalidservice_name, updatedUser.service_name)


            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.phone)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
            val Uid = 333
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"
            val Uvalidphone = 3045245
            val Uvalidaddress="Gujk"
            val Uvalidgender="feMale"
            val Uvalidtiming_slot = "evening"
            val Uvalidtrainer="no"
            val Uvalidservice_name="updated service"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateUser(Uid, updatedName, updatedEmail , Uvalidphone ,Uvalidaddress , Uvalidgender , Uvalidtiming_slot , Uvalidtrainer ,  Uvalidservice_name ).status)
        }
    }

    @Nested
    inner class DeleteUsers {

        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteUser(245).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addUser(id,validName, validEmail , validphone ,validaddress , validgender , validtiming_slot , validtrainer ,  validservice_name )
            val addedUser : UserDTO = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.phone).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveUserById(addedUser.phone).status)
        }
    }


    //helper function to retrieve a test user from the database by email
    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }
    //helper function to delete a test user from the database
    private fun deleteUser (phone: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/deleteByPhone/$phone").asString()
    }

    //helper function to add a test user to the database
    private fun addUser (id: Int, name: String,
                          email: String,
                          phone:Int,
                          address:String,
                          gender:String,
                          timing_slot:String,
                          trainer:String,
                          service_name:String):
            HttpResponse<JsonNode>
    {   return Unirest.post(origin + "/api/users")
        .body("{\"id\":\"$id\",\"name\":\"$name\", \"email\":\"$email\",\"phone\":\"$phone\",\"address\":\"$address\",\"gender\":\"$gender\",\"timing_slot\":\"$timing_slot\",\"trainer\":\"$trainer\",\"service_name\":\"$service_name\"}")
        .asJson()
    }
    //helper function to add a test user to the database
    private fun updateUser (id: Int,
                            name: String,
                            email: String,
                            phone:Int,
                            address:String,
                            gender:String,
                            timing_slot:String,
                            trainer:String,
                            service_name:String ): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\",\"phone\":\"$phone\",\"address\":\"$address\",\"gender\":\"$gender\",\"timing_slot\":\"$timing_slot\",\"trainer\":\"$trainer\",\"service_name\":\"$service_name\"}")
            .asJson()
    }
}
