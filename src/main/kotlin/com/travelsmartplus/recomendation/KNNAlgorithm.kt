package com.travelsmartplus.recomendation

import kotlin.math.sqrt

/**
 * KNNAlgorithm represents a K-Nearest Neighbors algorithm implementation.
 * It can be used for predicting labels based on labeled instances and calculating distances between features.
 * @author Gabriel Salas
 * @param T the type of the label for labeled instances.
 * @throws IllegalStateException if instances list is empty
 */

class KNNAlgorithm<T> {
    private var trainInstances: List<LabeledInstance<T>> = emptyList()
    private var k: Int = 0

    fun train(instances: List<LabeledInstance<T>>, k: Int) {
        trainInstances = instances
        this.k = k
    }

    fun predict(instances: List<LabeledInstance<T>>): LabeledInstance<T>? {
        if (trainInstances.isEmpty()) {
            throw IllegalStateException("The model has not been trained. Call the train() method first.")
        }

        // Calculate distances and get nearest neighbours
        val nearestNeighbors = instances.map { instance ->
            val distances = trainInstances.map { trainInstance ->
                val distance = calculateDistance(instance.features, trainInstance.features)
                Distance(distance, trainInstance.label)
            }.sortedBy { it.distance }

            val kNearestNeighbors = distances.take(k)
            val majorityLabel = kNearestNeighbors.groupingBy { it.label }.eachCount().maxByOrNull { it.value }?.key

            NearestNeighbor(instance, kNearestNeighbors, majorityLabel)
        }

        // Get instance with the closest distance among the nearest neighbours
        val predictedLabel = nearestNeighbors.minByOrNull { it.nearestNeighbors.firstOrNull()?.distance ?: Double.MAX_VALUE }
        return predictedLabel?.instance
    }

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

data class LabeledInstance<T>(val features: List<Double>, val label: T)
data class Distance(val distance: Double, val label: Any?)

data class NearestNeighbor<T>(
    val instance: LabeledInstance<T>,
    val nearestNeighbors: List<Distance>,
    val majorityLabel: Any?
)