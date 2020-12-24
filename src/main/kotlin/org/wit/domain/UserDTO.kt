package org.wit.domain


data class UserDTO (var id: Int,
                    var name:String,
                    var email:String,
                    var phone:Int,
                    var address:String,
                    var gender:String,
                    var timing_slot:String,
                    var trainer:String,
                    var service:String
                     )