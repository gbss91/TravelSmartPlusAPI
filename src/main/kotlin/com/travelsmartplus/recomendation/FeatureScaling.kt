package com.travelsmartplus.recomendation

/**
 * Normalises and standardises the data.
 * @author Gabriel Salas
 */

object FeatureScaling {

    fun minMaxNormalization(features: List<Double>): List<Double> {
        val normalizedFeatures = mutableListOf<Double>()

        val min = features.minOrNull() ?: return normalizedFeatures
        val max = features.maxOrNull() ?: return normalizedFeatures
        val range = max - min

        for (feature in features) {
            val normalizedValue = (feature - min) / range
            normalizedFeatures.add(normalizedValue)
        }

        return normalizedFeatures
    }

}