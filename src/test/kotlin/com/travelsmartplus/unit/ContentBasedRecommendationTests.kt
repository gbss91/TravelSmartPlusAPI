package com.travelsmartplus.unit

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.fixtures.*
import com.travelsmartplus.recomendation.ContentBasedRecommendationFacadeImpl
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertNotNull

class ContentBasedRecommendationTests {

    private var recommendationFacade: ContentBasedRecommendationFacadeImpl = ContentBasedRecommendationFacadeImpl()
    private val preferredAirlines = UserFixtures.users[0].preferredAirlines!!
    private val preferredHotels = UserFixtures.users[0].preferredHotelChains!!

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `should return flight based on preferences only`() = runBlocking {

        // Create mock flight bookings for predictions
        val origin = AirportFixtures.airports[0]
        val destination = AirportFixtures.airports[1]
        val departureDate = LocalDate.of(2023, 11, 21)
        val returnDate = LocalDate.of(2023, 11, 24)
        val flights = listOf(
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate, 0),
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate, 1),
            FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate, 2)
        )

        val recommendedFlight = recommendationFacade.recommendFlights(preferredAirlines, flights)
        assertNotNull(recommendedFlight)
        println(recommendedFlight)


    }

    @Test
    fun `should return hotel based on preferences`() = runBlocking {

        // Create mock hotel bookings for predictions
        val checkInDate = LocalDate.of(2023, 11, 21).toKotlinLocalDate()
        val checkOutDate = LocalDate.of(2023, 11, 24).toKotlinLocalDate()
        val hotels = listOf(
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate, 0),
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate, 1),
            HotelFixtures.createMockHotelBooking(checkInDate, checkOutDate, 2)
        )

        val recommendedHotel = recommendationFacade.recommendHotels(preferredHotels, hotels)
        assertNotNull(recommendedHotel)
        println(recommendedHotel)
    }

}