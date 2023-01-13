package ru.fitsuli.petsmobile.data.dto

import java.io.File

/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */
data class LostAnimalEntity(
    val animalName: String,
    val animalType: String,
    val lostAddressFull: String,
    val lostAddressCity: String,
    val lostGeoPosition: String,
    val lostDate: String,
    val description: String,
    val gender: Int,
    val contacts: Contacts,
    val age: Int,
    val fileNames: List<String>,
    val userId: String,
    val id: String
)

enum class Gender(val value: Int, val text: String) {
    MALE(0, "Мужской"), FEMALE(1, "Женский")
}

data class Contacts(
    val name: String,
    val phone: String,
    val email: String,
    val telegram: String,
    val vk: String,
)

data class CreateLostAnimalEntityDto(
    val animalName: String,
    val animalType: String,
    val lostAddressFull: String,
    val lostAddressCity: String,
    val lostGeoPosition: String,
    val lostDate: String,
    val postCreationDate: String,
    val description: String,
    val gender: Int,
    val contacts: Contacts,
    val age: Int,
    val files: List<File>,
    val userId: String?,
    val id: String?,
)