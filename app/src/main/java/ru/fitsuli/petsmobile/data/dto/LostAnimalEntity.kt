package ru.fitsuli.petsmobile.data.dto

/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */
data class LostAnimalEntity(
    val animalName: String,
    val animalType: String,
    val lostArea: String,
    val lostDate: String,
    val description: String,
    val fileNames: List<String>,
    val userId: String,
    val id: String
)
