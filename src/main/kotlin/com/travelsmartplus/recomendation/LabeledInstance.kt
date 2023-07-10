package com.travelsmartplus.recomendation

/**
 * Data classes used for both recommendation algorithms
 * @author Gabriel Salas
 * @
 */

// Represents features vector and label
data class LabeledInstance<T>(
    val features: List<Double>,
    val label: T
)

// Represents distance to other instances
data class Distance(
    val distance: Double,
    val label: Any?
)

// Represents the nearest neighbour with the majority label
data class NearestNeighbour<T>(
    val instance: LabeledInstance<T>,
    val nearestNeighbour: Distance
)
