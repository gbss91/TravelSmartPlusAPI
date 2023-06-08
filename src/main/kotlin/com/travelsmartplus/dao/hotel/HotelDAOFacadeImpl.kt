package com.travelsmartplus.dao.hotel

import com.travelsmartplus.models.Hotel
import com.travelsmartplus.models.HotelEntity
import com.travelsmartplus.models.Hotels
import com.travelsmartplus.models.toHotel
import com.travelsmartplus.utils.DatabaseFactory.dbQuery

class HotelDAOFacadeImpl : HotelDAOFacade {
    override suspend fun getHotel(code: String): Hotel? = dbQuery {
        HotelEntity.find { Hotels.code eq code }.singleOrNull()?.toHotel()
    }

    override suspend fun getAllHotels(): List<Hotel> = dbQuery {
        HotelEntity.all().map(HotelEntity::toHotel)
    }
}