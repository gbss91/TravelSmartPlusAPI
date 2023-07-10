package com.travelsmartplus.dao.hotel

import com.travelsmartplus.models.Hotel

interface HotelDAOFacade {
    suspend fun getHotel(code: String): Hotel?
    suspend fun getAllHotels(): List<Hotel>
}