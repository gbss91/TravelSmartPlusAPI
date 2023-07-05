package com.travelsmartplus.unit

import com.travelsmartplus.models.TravelData
import kotlinx.datetime.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class TravelDataTests {

    @Test
    fun `create travel data`() {

        val travelData = TravelData(
            id = 1,
            userId = 1,
            dob =  LocalDate(2033, 2, 15),
            nationality =  "United States",
            passportNumber = "A4842452",
            passportExpiryDate = LocalDate(1985, 8, 23)
        )
        
        assertEquals(1, travelData.id)
        assertEquals(1, travelData.userId)
        assertEquals("United States", travelData.nationality)
        assertEquals("A4842452", travelData.passportNumber)
    }
}