package com.example.nutripekes_android

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {

    @Query("SELECT * FROM info_cards")
    fun getAllInfo(): Flow<List<InfoCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertInfoCards(cards: List<InfoCardEntity>)

    @Query("DELETE FROM info_cards")
    suspend fun clearAllInfo()

    @Transaction
    suspend fun refreshInfoFromApi(apiItems: List<InfoItem>) {

        clearAllInfo()

        val entities = apiItems.map {
            InfoCardEntity(
                pk = it.pk,
                title = it.title,
                content = it.content,
                color = it.color
            )
        }

        _insertInfoCards(entities)
    }
}