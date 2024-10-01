package com.kaushik.kavach.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ContactDao {
    @Query("select * from contact order by name")
    fun getAll(): List<Contact>

    @Upsert
    fun upsertAll(vararg users: Contact)

    @Query("delete from contact")
    fun deleteAll()

    @Delete
    fun delete(contact: Contact)

}