package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.fitsuli.petsmobile.data.dto.CreateAnimalEntityDto
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.BaseViewModel
import java.io.File

/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */
class AddAnimalScreenViewModel(application: Application) : BaseViewModel(application) {

    var createEntity by mutableStateOf(CreateAnimalEntityDto())
        private set

    fun setAnimalType(animalType: String) {
        createEntity = createEntity.copy(animalType = animalType)
    }

    fun setAnimalName(animalName: String) {
        createEntity = createEntity.copy(animalName = animalName)
    }

    fun setAddressFull(addressFull: String) {
        createEntity = createEntity.copy(addressFull = addressFull)
    }

    fun setAddressCity(addressCity: String) {
        createEntity = createEntity.copy(addressCity = addressCity)
    }

    fun setGeoPosition(geoPosition: String) {
        createEntity = createEntity.copy(geoPosition = geoPosition)
    }

    fun setDate(date: String) {
        createEntity = createEntity.copy(date = date)
    }

    fun setPostCreationDate(postCreationDate: String) {
        createEntity = createEntity.copy(postCreationDate = postCreationDate)
    }

    fun setDescription(description: String) {
        createEntity = createEntity.copy(description = description)
    }

    fun setGender(gender: Gender) {
        createEntity = createEntity.copy(gender = gender.value)
    }

    fun setContactName(name: String) {
        createEntity = createEntity.copy(contacts = createEntity.contacts.copy(name = name))
    }

    fun setContactPhone(phone: String) {
        createEntity = createEntity.copy(contacts = createEntity.contacts.copy(phone = phone))
    }

    fun setContactEmail(email: String) {
        createEntity = createEntity.copy(contacts = createEntity.contacts.copy(email = email))
    }

    fun setContactTelegram(telegram: String) {
        createEntity = createEntity.copy(contacts = createEntity.contacts.copy(telegram = telegram))
    }

    fun setContactVk(vk: String) {
        createEntity = createEntity.copy(contacts = createEntity.contacts.copy(vk = vk))
    }

    fun setAge(age: Int) {
        createEntity = createEntity.copy(age = age)
    }

    fun setFiles(files: List<File>) {
        createEntity = createEntity.copy(files = files)
    }


    fun addAnimal(
        addPage: AddPage,
        animal: CreateAnimalEntityDto
    ) {
        viewModelScope.launch {
            val requestBody = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                addFormDataPart("AnimalName", animal.animalName)
                addFormDataPart("AnimalType", animal.animalType)
                addFormDataPart("AddressFull", animal.addressFull)
                addFormDataPart("AddressCity", animal.addressCity)
                addFormDataPart("GeoPosition", animal.geoPosition)
                addFormDataPart("Date", animal.date)
                addFormDataPart("PostCreationDate", animal.postCreationDate)
                addFormDataPart("Description", animal.description)
                addFormDataPart("Gender", animal.gender.toString())
                addFormDataPart("Contacts.name", animal.contacts.name)
                addFormDataPart("Contacts.phone", animal.contacts.phone)
                addFormDataPart("Contacts.email", animal.contacts.email)
                addFormDataPart("Contacts.telegram", animal.contacts.telegram)
                addFormDataPart("Contacts.vk", animal.contacts.vk)
                addFormDataPart("Age", animal.age.toString())
                animal.files.forEach { file ->
                    addFormDataPart(
                        "Images",
                        null,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }
            }.build()

            apiClient.addLostPet(
                url = addPage.urlPrefix, requestBody = requestBody
            )
                .onSuccess {
                }
        }
    }

}