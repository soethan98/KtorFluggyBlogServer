package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullName:String,
    val photo:String?,
    val email:String,
    val firstName:String?,
    val lastName:String?,
    val createdAt:Long?,
    val updatedAt:Long? = null,
    val id:String
)