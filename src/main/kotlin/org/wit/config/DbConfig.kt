package org.wit.config
import org.jetbrains.exposed.sql.Database
import java.net.URI

class DbConfig{

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
//    fun getDbConnection() :Database{
//        return Database.connect(
//            "jdbc:postgresql://ec2-35-169-92-231.compute-1.amazonaws.com:5432/d64qcmuokr0kh4?sslmode=require",
//            driver = "org.postgresql.Driver",
//            user = "nlgduwsarkpsqm",
//            password = "9331b0e4ead8a69eaaa9e14990cdc5a98e6df43d520a8155ad1e2ce17de00173")
//    }
    fun getDbConnection() :Database{
        val databaseURL = URI(System.getenv("DATABASE_URL"))
        return Database.connect(
            "jdbc:postgresql://" + databaseURL.host + ":" + databaseURL.port + databaseURL.path + "?sslmode=require",
            driver = "org.postgresql.Driver",
            user = databaseURL.userInfo.split(":").toTypedArray()[0],
            password = databaseURL.userInfo.split(":").toTypedArray()[1])
    }

}