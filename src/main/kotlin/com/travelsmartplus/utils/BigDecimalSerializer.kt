package com.travelsmartplus.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

// Custom Serializer for BigDecimal - Reference [https://petnagy.medium.com/kotlinx-serialization-part3-4bb41a33c1a3]
class BigDecimalSerializer : KSerializer<BigDecimal> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        val string = decoder.decodeString()
        return BigDecimal(string)
    }
}