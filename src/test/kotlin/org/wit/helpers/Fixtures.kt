package org.wit.helpers

import org.wit.domain.UserDTO

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"


val users = arrayListOf<UserDTO>(
    UserDTO (id = 1 ,name="Alice",email ="alice@wonderland.com",phone =5689 ,address="aspen",gender="Male",timing_slot = "Morning",trainer = "yes",service = "Workout"),
    UserDTO (id = 2 ,name="Bob Cat",email ="mary@contrary.com",phone =6666 ,address="aspen close",gender="female",timing_slot = "Morning",trainer = "yes",service = "swimimg"),
    UserDTO (id = 3 ,name="Mary Contrary",email ="bob@cat.ie",phone =9999 ,address="villa rose",gender="male",timing_slot = "evening",trainer = "yes",service = "Workout"),
    UserDTO (id = 4 ,name="Carol Singe",email ="carol@singer.com",phone =7777 ,address="view mount",gender="others",timing_slot = "Morning",trainer = "yes",service = "Workout")
)