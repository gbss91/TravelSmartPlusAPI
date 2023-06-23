package com.travelsmartplus.recomendation

import kotlin.math.sqrt

/**
 * K-Nearest Neighbours algorithm implementation.
 * Calculates data based on similarity to nearby labeled classes. Modified to return instance with closest neighbour from list of instances.
 * @author Gabriel Salas
 * @param T the type of the label for labeled instances.
 * @throws IllegalStateException if instances list is empty
 */

class KNNAlgorithm<T> {
    private var trainInstances: List<LabeledInstance<T>> = emptyList()

    fun train(instances: List<LabeledInstance<T>>) {
        trainInstances = instances
    }

    fun predict(instances: List<LabeledInstance<T>>): LabeledInstance<T>? {
        if (trainInstances.isEmpty()) {
            throw IllegalStateException("The model has not been trained. Call the train() method first.")
        }

        val nearestNeighbours = instances.map { instance ->
            val distances = trainInstances.map { trainInstance ->
                val distance = calculateDistance(instance.features, trainInstance.features)
                Distance(distance, trainInstance.label)
            }.sortedBy { it.distance }

            val closestNeighbour = distances.first()
            NearestNeighbour(instance, closestNeighbour)
        }

        // Return the instance with the closest neighbour
        return nearestNeighbours.minByOrNull { it.nearestNeighbour.distance }?.instance
    }

    // Calculates distance using Euclidean distance
    private fun calculateDistance(features1: List<Double>, features2: List<Double>): Double {
        require(features1.size == features2.size)

        var sum = 0.0
        for (i in features1.indices) {
            val diff = features1[i] - features2[i]
            sum += diff * diff
        }
        return sqrt(sum)
    }
}
