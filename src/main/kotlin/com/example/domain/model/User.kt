package com.example.domain.model

data class User(
    val fullName:String,
    val photo:String?,
    val email:String,
    val firstName:String?,
    val lastName:String?,
    val createdAt:Long,
    val updatedAt:Long?,
    val id:String
)