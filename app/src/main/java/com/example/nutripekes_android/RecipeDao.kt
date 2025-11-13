package com.example.nutripekes_android

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeWithIngredients>>

    @Transaction
    @Query("""
        SELECT * FROM recipes WHERE pk IN (
            SELECT pk FROM recipe_ingredient_cross_ref WHERE ingredientName IN (:ingredientNames)
        )
    """)
    fun getRecipesByIngredients(ingredientNames: List<String>): Flow<List<RecipeWithIngredients>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun _insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertCrossRefs(crossRefs: List<RecipeIngredientCrossRef>)

    @Query("DELETE FROM recipes")
    suspend fun clearAllRecipes()

    @Query("DELETE FROM ingredients")
    suspend fun clearAllIngredients()

    @Query("DELETE FROM recipe_ingredient_cross_ref")
    suspend fun clearAllCrossRefs()

    @Transaction
    suspend fun refreshRecipesFromApi(apiItems: List<RecipeApiResponseItem>) {
        clearAllRecipes()
        clearAllIngredients()
        clearAllCrossRefs()

        val recipes = apiItems.map {
            RecipeEntity(it.pk, it.name, it.image, it.instructions)
        }

        val ingredients = apiItems.flatMap { it.ingredients }
            .map { IngredientEntity(it.lowercase()) }
            .distinct()

        val crossRefs = apiItems.flatMap { recipe ->
            recipe.ingredients.map { ingredientName ->
                RecipeIngredientCrossRef(
                    pk = recipe.pk,
                    ingredientName = ingredientName.lowercase()
                )
            }
        }

        _insertRecipes(recipes)
        _insertIngredients(ingredients)
        _insertCrossRefs(crossRefs)
    }
}