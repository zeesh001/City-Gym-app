package org.wit.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.wit.controllers.CityGymAPI

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create().apply {
            exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }
    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin){
        app.routes {
            get("/api/users", CityGymAPI::getAllUsers)
            get("/api/users/:user-id",CityGymAPI::getUserByUserId)
            get("/api/users/email/:email",CityGymAPI::getUserByEmail)
            get("/api/users/phone/:phone",CityGymAPI::getUserByPhone)
            post("/api/users",CityGymAPI::addUser)
            delete("/api/users/:user-id", CityGymAPI::deleteUser)
            delete("/api/users/deleteByPhone/:phone", CityGymAPI::deleteUserByPhone)
            patch( "/api/users/:user-id", CityGymAPI::updateUser)
        }

    }
}