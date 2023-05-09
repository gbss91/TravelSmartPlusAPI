package com.travelsmartplus.fixtures

import com.travelsmartplus.models.User

object UserFixture {
    val users = listOf(
        User(
            orgId = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@test.com",
            admin = true,
            password = "23646131f8752ab2e9d65345cfc7b5d515af4661a15ba749922cb2e674c36d9d", // myPass123
            salt = "e41ea5cc46b2b8b8099f81cd1e493bc6ad6f9d4d19fc149f37ca4ae154ba28f7"
        ),
        User(
            orgId = 1,
            firstName = "Jane",
            lastName = "Smith",
            email = "jane@test.com",
            admin = false,
            password = "23646131f8752ab2e9d65345cfc7b5d515af4661a15ba749922cb2e674c36d9d",// myPass123
            salt = "e41ea5cc46b2b8b8099f81cd1e493bc6ad6f9d4d19fc149f37ca4ae154ba28f7",
        )
    )
}