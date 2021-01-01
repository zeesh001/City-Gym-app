package org.wit.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.rendering.vue.VueComponent
import org.wit.controllers.CityGymAPI

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create { config ->
            config.enableWebjars()
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



            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/:user-id", VueComponent("<user-profile></user-profile>"))
            get("/users/:service_name/services", VueComponent("<user-service-overview></user-service-overview>"))
            get("/services", VueComponent("<service-overview></service-overview>"))
            get("/services/:service_name/services", VueComponent("<enlist-users></enlist-users>"))


            ////userAPIs
            get("/api/users", CityGymAPI::getAllUsers)
            get("/api/users/:user-id",CityGymAPI::getUserByUserId)
            get("/api/users/email/:email",CityGymAPI::getUserByEmail)
            get("/api/users/phone/:phone",CityGymAPI::getUserByPhone)
            get("/api/users/service/:service_name", CityGymAPI::getUsersByService)
            post("/api/users",CityGymAPI::addUser )
            delete("/api/users/:id", CityGymAPI::deleteUser)
            delete("/api/users/deleteByPhone/:phone", CityGymAPI::deleteUserByPhone)
            patch( "/api/users/:user-id", CityGymAPI::updateUser)

             ///////////Services endpoints

            get("/api/services/:service_name", CityGymAPI::getServicesByName)
           // get("/api/services/:id", CityGymAPI::getServicesById)
            delete("/api/services/:service_name", CityGymAPI::deleteService)
            get("/api/services", CityGymAPI::getAllServices)
            post("/api/services", CityGymAPI::addService)
            patch("/api/services/:id", CityGymAPI::updateName)



        }

    }
}