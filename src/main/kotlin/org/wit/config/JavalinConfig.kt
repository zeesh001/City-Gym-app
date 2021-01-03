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

            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/:user-id", VueComponent("<user-profile></user-profile>"))
            get("/services", VueComponent("<service-overview></service-overview>"))
            get("/services/:service_name/services", VueComponent("<enlist-users></enlist-users>"))
            get("/packages", VueComponent("<package-overview></package-overview>"))
            get("/packages/:id", VueComponent("<package-update></package-update>"))
            get("/promotions", VueComponent("<promotion-overview></promotion-overview>"))
            get("/employees", VueComponent("<employee-overview></employee-overview>"))
            get("/employees/:id", VueComponent("<employee-profile></employee-profile>"))



            ////userAPIs
            get("/api/users", CityGymAPI::getAllUsers)
            post("/api/users",CityGymAPI::addUser )
            get("/api/users/:user-id",CityGymAPI::getUserByUserId)
            get("/api/users/email/:email",CityGymAPI::getUserByEmail)
            get("/api/users/phone/:number",CityGymAPI::getUserByPhone)
            get("/api/users/service/:service_name", CityGymAPI::getUsersByService)
            delete("/api/users/:id", CityGymAPI::deleteUser)
            delete("/api/users/deleteByPhone/:phone", CityGymAPI::deleteUserByPhone)
            patch( "/api/users/:user-id", CityGymAPI::updateUser)

             ///////////Services endpoints
            post("/api/services", CityGymAPI::addService)
            patch("/api/services/:service_name", CityGymAPI::updateName)
            get("/api/services/:service_name", CityGymAPI::getServicesByName)
            get("/api/services/id/:id", CityGymAPI::getServicesById)
            delete("/api/services/:service_name", CityGymAPI::deleteService)
            get("/api/services", CityGymAPI::getAllServices)

            ///////packages endpoints
            get("/api/packages", CityGymAPI::getAllPackages)
            get("/api/packages/id/:id", CityGymAPI::getPackageById)
            get("/api/packages/:package_cat", CityGymAPI::getPackageByCategory)
            post("/api/packages", CityGymAPI::addPackage)
            patch("/api/packages/:id", CityGymAPI::updatePackage)
            delete("/api/packages/:id", CityGymAPI::deletePackage)

            ///////promotion endpoints
            get("/api/promotions", CityGymAPI::getAllPromotions)
            get("/api/promotions/id/:id", CityGymAPI::getPromotionById)
            get("/api/promotions/:package_cat", CityGymAPI::getPromotionByCategory)
            post("/api/promotions", CityGymAPI::addPromotion)
            patch("/api/promotions/:id", CityGymAPI::updatePromotion)
            delete("/api/promotions/:id", CityGymAPI::deletePromotion)


            ////EmployeeAPIs

            get("/api/employees", CityGymAPI::getAllEmployees)
            post("/api/employees",CityGymAPI::addEmployee )
            get("/api/employees/:id",CityGymAPI::getEmployeeById)
            get("/api/employees/phone/:number",CityGymAPI::getEmployeeByPhone)
            get("/api/employees/service/:designation", CityGymAPI::getEmployeesByDesig)
            delete("/api/employees/:id", CityGymAPI::deleteEmployee)
          //  delete("/api/employees/deleteByPhone/:phone", CityGymAPI::deleteEmployeeByPhone)
            patch( "/api/employees/:id", CityGymAPI::updateEmployee)

        }

    }
}