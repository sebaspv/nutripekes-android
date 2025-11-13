package com.example.nutripekes_android

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val pk: String,
    val name: String,
    val imgUrl: String,
    val instructions: String
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val name: String
)

@Entity(tableName = "recipe_ingredient_cross_ref", primaryKeys = ["pk", "ingredientName"])
data class RecipeIngredientCrossRef(
    val pk: String,
    val ingredientName: String
)

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "pk",
        entityColumn = "name",
        associateBy = Junction(
            RecipeIngredientCrossRef::class,
            parentColumn = "pk",
            entityColumn = "ingredientName"
        )
    )
    val ingredients: List<IngredientEntity>
)


@Entity(tableName = "info_cards")
data class InfoCardEntity(
    @PrimaryKey val pk: String,
    val title: String,
    val content: List<List<String>>,
    var color: String
)