package com.kaushik.kavach.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey val phoneNumber: String,
    @ColumnInfo(name = "name") val name: String,
)
