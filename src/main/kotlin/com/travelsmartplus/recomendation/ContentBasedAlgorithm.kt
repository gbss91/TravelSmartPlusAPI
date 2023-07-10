package com.travelsmartplus.recomendation

/**
 * Content-Based filtering algorithm implementation.
 * Provides personalized recommendations based on user preferences and item features.
 * [Reference](https://www.stratascratch.com/blog/step-by-step-guide-to-building-content-based-filtering/)
 *
 * @param T the type of the items to recommend
 * @throws IllegalArgumentException if user preferences is empty or item not found
 */

class ContentBasedAlgorithm<T> {

    // Recommends using user and items matrix
    fun recommend(userPreferences: DoubleArray, itemFeatures: List<DoubleArray>, items: List<T>): T? {

        var maxSimilarity = Double.MIN_VALUE
        var recommendedItem: T? = null

        // Iterate items and calculate  the similarity using the user preferences.
        // Assign item to recommended item if similarity is greater than previous items
        for (i in itemFeatures.indices) {
            val similarity = calculateSimilarity(userPreferences, itemFeatures[i])
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity
                recommendedItem = items[i]
            }
        }

        // Return item and similarity score
        return recommendedItem
    }


    // Calculate the dot product between the preference vector and the feature vector - Reference [https://www.learndatasci.com/glossary/cosine-similarity/]
    private fun calculateSimilarity(preferenceMatrix: DoubleArray, featureMatrix: DoubleArray): Double {
        val dotProduct = preferenceMatrix.zip(featureMatrix).sumOf { (preference, feature) ->
            preference * feature
        }

        return dotProduct
    }


}






