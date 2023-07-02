package com.adelinarotaru.fooddelivery.customer.ui.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.databinding.ItemIngredietnsBinding
import com.adelinarotaru.fooddelivery.shared.BaseRVAdapter
import com.adelinarotaru.fooddelivery.shared.ItemAdapter

data class IngredientItem(val ingredient: Ingredient, override val id: Int) : ItemAdapter

sealed class Ingredient {
    @get:DrawableRes
    abstract val imgRes: Int

    data class Salad(override val imgRes: Int = R.drawable.salad) : Ingredient()
    data class Onions(override val imgRes: Int = R.drawable.onion) : Ingredient()
    data class Avocado(override val imgRes: Int = R.drawable.avocado) : Ingredient()
    data class Herbs(override val imgRes: Int = R.drawable.herbs) : Ingredient()
    data class Eggs(override val imgRes: Int = R.drawable.eggs) : Ingredient()
    data class Tomato(override val imgRes: Int = R.drawable.tomatoes) : Ingredient()
    data class Olives(override val imgRes: Int = R.drawable.olives) : Ingredient()
    data class Chicken(override val imgRes: Int = R.drawable.chicken) : Ingredient()
    data class Beef(override val imgRes: Int = R.drawable.beef) : Ingredient()
    data class Burrata(override val imgRes: Int = R.drawable.burrata) : Ingredient()
}

class IngredientsAdapter : BaseRVAdapter<ItemIngredietnsBinding, IngredientItem>() {
    override val refreshUi: (ItemIngredietnsBinding, IngredientItem) -> Unit =
        { binding, ingredientItem ->
            binding.ingredientImage.apply {
                setImageDrawable(context.getDrawable(ingredientItem.ingredient.imgRes))
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
    else -> throw IllegalStateException("$this ingredient is not mapped")
}
