package com.travelsmartplus.fixtures

import com.travelsmartplus.models.TravelData
import kotlinx.datetime.LocalDate

object TravelDataFixtures {
    val travelDetails = listOf(
        TravelData(
            id = 1,
            userId = 1,
            dob =  LocalDate(2033, 2, 15),
            nationality =  "United States",
            passportNumber = "A4842452",
            passportExpiryDate = LocalDate(1985, 8, 23)
        ),
        TravelData(
            id = 2,
            userId = 2,
            dob =  LocalDate(2029, 2, 15),
            nationality =  "British",
            passportNumber = "G09873643",
            passportExpiryDate = LocalDate(1995, 2, 15)
        )
    )
}