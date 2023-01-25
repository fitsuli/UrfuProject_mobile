package ru.fitsuli.petsmobile.data.dto

import java.io.File

/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */
data class AnimalEntity(
    val animalName: String?,
    val animalType: String,
    val addressFull: String,
    val addressCity: String,
    val geoPosition: String,
    val date: String,
    val description: String,
    val gender: Int,
    val contacts: Contacts,
    val age: Int,
    val fileNames: List<String>,
    val isClosed: Boolean,
    val userId: String?,
    val id: String
)

enum class Gender(val value: Int, val text: String) {
    MALE(0, "Мужской"), FEMALE(1, "Женский"), UNKNOWN(2, "Не знаю")
}

data class Contacts(
    val name: String,
    val phone: String,
    val email: String?,
    val telegram: String?,
    val vk: String?,
)

data class CreateAnimalEntityDto(
    val animalName: String? = null,
    val animalType: String = "Собака",
    val addressFull: String = "",
    val addressCity: String = "",
    val geoPosition: String = "",
    val date: String = "",
    val postCreationDate: String = "",
    val description: String = "",
    val gender: Int = Gender.MALE.value,
    val contacts: Contacts = Contacts("", "", "", "", ""),
    val age: Int = 0,
    val files: List<File> = emptyList(),
    val userId: String? = null,
    val id: String? = null,
) {
    fun isValid(): Boolean {
        return geoPosition.isNotEmpty() &&
                ///addressCity.isNotEmpty() &&
                date.isNotEmpty() &&
                description.isNotEmpty() &&
                contacts.name.isNotEmpty() &&
                contacts.phone.isNotEmpty()
//                && files.isNotEmpty()
    }
}