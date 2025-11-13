package com.example.nutripekes_android

import java.util.Calendar
import java.util.stream.IntStream

data class PortionData(
    val verdurasfrutasMax: Int,
    val origenanimalMax: Int,
    val leguminosasMax: Int,
    val cerealesMax: Int,
    val aguaMax: Int
)

object PortionLogic {
    fun calculateAge(birthYear: Int): Int {
        if (birthYear <= 0) return 0
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }

    fun getPortionsForAge(age: Int): PortionData {
        return when (age) {
            in 1 .. 3 -> PortionData(
                verdurasfrutasMax = 2,
                origenanimalMax = 1,
                leguminosasMax = 1,
                cerealesMax = 3,
                aguaMax = 4
            )
            in 3..5 -> PortionData(
                verdurasfrutasMax = 3,
                origenanimalMax = 2,
                leguminosasMax = 1,
                cerealesMax = 4,
                aguaMax = 5
            )

            in 6..8 -> PortionData(
                verdurasfrutasMax = 4,
                origenanimalMax = 2,
                leguminosasMax = 2,
                cerealesMax = 5,
                aguaMax = 6
            )

            in 9..15 -> PortionData(
                verdurasfrutasMax = 5,
                origenanimalMax = 3,
                leguminosasMax = 2,
                cerealesMax = 6,
                aguaMax = 8
            )

            else -> PortionData(
                verdurasfrutasMax = 7,
                origenanimalMax = 5,
                leguminosasMax = 3,
                cerealesMax = 8,
                aguaMax = 10
            )
        }
    }
}