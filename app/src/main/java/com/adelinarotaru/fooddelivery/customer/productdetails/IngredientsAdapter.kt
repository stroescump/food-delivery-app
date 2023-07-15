package com.adelinarotaru.fooddelivery.customer.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemIngredietnsBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseRVAdapter
import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter

data class IngredientItem(val ingredient: Ingredient, override val id: Int) : ItemAdapter

sealed class Ingredient {
    @get:DrawableRes
    abstract val imgRes: Int

    abstract val name: String

    data class Salad(
        override val imgRes: Int = R.drawable.salad,
        override val name: String = "Salad"
    ) :
        Ingredient()

    data class Onions(
        override val imgRes: Int = R.drawable.onion,
        override val name: String = "Onions"
    ) :
        Ingredient()

    data class Avocado(
        override val imgRes: Int = R.drawable.avocado,
        override val name: String = "Avocado"
    ) :
        Ingredient()

    data class Herbs(
        override val imgRes: Int = R.drawable.herbs,
        override val name: String = "Herbs"
    ) :
        Ingredient()

    data class Eggs(
        override val imgRes: Int = R.drawable.eggs,
        override val name: String = "Eggs"
    ) :
        Ingredient()

    data class Tomato(
        override val imgRes: Int = R.drawable.tomatoes,
        override val name: String = "Tomato"
    ) :
        Ingredient()

    data class Olives(
        override val imgRes: Int = R.drawable.olives,
        override val name: String = "Olives"
    ) :
        Ingredient()

    data class Chicken(
        override val imgRes: Int = R.drawable.chicken,
        override val name: String = "Chicken"
    ) :
        Ingredient()

    data class Beef(
        override val imgRes: Int = R.drawable.beef,
        override val name: String = "Beef"
    ) :
        Ingredient()

    data class Burrata(
        override val imgRes: Int = R.drawable.burrata,
        override val name: String = "Burrata"
    ) :
        Ingredient()

    data class Unknown(
        override val imgRes: Int = R.drawable.missing_resource,
        override val name: String
    ) : Ingredient()
}

class IngredientsAdapter : BaseRVAdapter<ItemIngredietnsBinding, IngredientItem>() {
    override val refreshUi: (ItemIngredietnsBinding, IngredientItem) -> Unit =
        { binding, ingredientItem ->
            binding.apply {
                ingredientImage.apply {
                    setImageDrawable(
                        AppCompatResources.getDrawable(
                            context,
                            ingredientItem.ingredient.imgRes
                        )
                    )
                }
                ingredientName.text = ingredientItem.ingredient.name
            }
        }

    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemIngredietnsBinding =
        ItemIngredietnsBinding::inflate

}

fun String.toIngredient() = when (this) {
    "Salad" -> Ingredient.Salad()
    "Avocado" -> Ingredient.Avocado()
    "Onions" -> Ingredient.Onions()
    "Herbs" -> Ingredient.Herbs()
    "Eggs" -> Ingredient.Eggs()
    "Tomatoes" -> Ingredient.Tomato()
    "Olives" -> Ingredient.Olives()
    "Chicken" -> Ingredient.Chicken()
    "Beef" -> Ingredient.Beef()
    "Burrata" -> Ingredient.Burrata()
    else -> Ingredient.Unknown(name = this)
}
