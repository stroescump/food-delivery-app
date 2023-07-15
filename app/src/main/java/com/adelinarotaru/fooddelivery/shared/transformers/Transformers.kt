package com.adelinarotaru.fooddelivery.shared.transformers

import com.adelinarotaru.fooddelivery.customer.domain.IRestaurant
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.shared.models.Restaurant

fun List<IRestaurant>.transformToRestaurantDto() = map {
    with(it) {
        Restaurant(
            id,
            name,
            lat,
            long,
            phoneNumber,
            rating,
            freeDelivery,
            openingHours,
            closingHours,
            menuItems?.map { item ->
                with(item) {
                    MenuItem(
                        id,
                        name,
                        price,
                        restaurantId,
                        restaurantRating,
                        foodCategory,
                        ingredients.split(","),
                        description
                    )
                }
            },
            reviews,
            cuisineTypes
        )
    }
}