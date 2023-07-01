package com.adelinarotaru.fooddelivery.shared.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.adelinarotaru.fooddelivery.R

sealed class CuisineType(
    open val id: Int,
    @StringRes open val nameRes: Int,
    @DrawableRes open val iconRes: Int,
    open val enum: String
) {
    data class Burger(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "BURGER"
    ) : CuisineType(id, nameRes, iconRes, enum)

    data class Pizza(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "PIZZA"
    ) : CuisineType(id, nameRes, iconRes, enum) {
        companion object {
            val default = Pizza(0, R.string.pizza, R.drawable.ic_pizza)
        }
    }

    data class Taco(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "TACO"
    ) : CuisineType(id, nameRes, iconRes, enum)

    data class Sushi(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "SUSHI"
    ) : CuisineType(id, nameRes, iconRes, enum)

    data class Asian(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "ASIAN"
    ) : CuisineType(id, nameRes, iconRes, enum)

    data class Kebab(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "KEBAB"
    ) : CuisineType(id, nameRes, iconRes, enum)

    data class Donut(
        override val id: Int,
        override val nameRes: Int,
        override val iconRes: Int,
        override val enum: String = "DONUT"
    ) : CuisineType(id, nameRes, iconRes, enum)
}
