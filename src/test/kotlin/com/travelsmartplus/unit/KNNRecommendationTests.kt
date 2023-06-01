package com.travelsmartplus.unit

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.fixtures.AirportFixtures
import com.travelsmartplus.fixtures.BookingFixtures
import com.travelsmartplus.fixtures.FlightFixtures
import com.travelsmartplus.fixtures.HotelFixtures
import com.travelsmartplus.recomendation.KNNRecommendationFacadeImpl
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertNotNull


class KNNRecommendationTests {
    private var recommendationFacade: KNNRecommendationFacadeImpl = KNNRecommendationFacadeImpl()
    private val previousBookings = BookingFixtures.createMockBookings()
    private val k = 3

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `should return flight based on previous bookings`() = runBlocking {

        recommendationFacade.trainModel(previousBookings, k)

        // Create mock flight bookings for predictions
        val origin = AirportFixtures.airports[0]
        val destination = AirportFixtures.airports[1]
        val departureDate = LocalDate.of(2023, 11, 21)
        val returnDate = LocalDate.of(2023, 11, 24)
        val flights = listOf(
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate),
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate),
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate)
        )

        // Predict flight and assert
        val predictedFlight = recommendationFacade.predict(flights)
        assertNotNull(predictedFlight)
        println(predictedFlight)

    }

    @Test
    fun `should return hotel based on previous bookings`() = runBlocking {

        // Train the model with previous bookings
        recommendationFacade.trainModel(previousBookings, k)

        // Create mock hotel bookings for predictions
        val checkInDate = LocalDate.of(2023, 11, 21).toKotlinLocalDate()
        val checkOutDate = LocalDate.of(2023, 11, 24).toKotlinLocalDate()
        val hotels = listOf(
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate),
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate),
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate)
        )

        // Predict hotel and assert
        val predictedHotel = recommendationFacade.predict(hotels)
        assertNotNull(predictedHotel)
        println(predictedHotel)
    }

}