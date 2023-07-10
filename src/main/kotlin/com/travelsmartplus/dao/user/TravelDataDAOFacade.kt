package com.travelsmartplus.dao.user

import com.travelsmartplus.models.TravelData

interface TravelDataDAOFacade {
    suspend fun getTravelData(userId: Int): TravelData?
    suspend fun addTravelData(travelData: TravelData): TravelData?
    suspend fun editTravelData(id: Int, travelData: TravelData): TravelData
    suspend fun deleteTravelData(id: Int)
}