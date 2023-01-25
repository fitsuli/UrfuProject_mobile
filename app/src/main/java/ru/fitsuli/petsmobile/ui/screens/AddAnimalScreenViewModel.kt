package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.fitsuli.petsmobile.data.dto.CreateAnimalEntityDto
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.BaseViewModel
import timber.log.Timber
import java.io.File
import java.util.Locale


/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */
class AddAnimalScreenViewModel(application: Application) : BaseViewModel(application) {

    val location = MutableStateFlow(Location("").apply {
        latitude = 56.826961
        longitude = 60.603849
    })

    var autocompleteAddress by mutableStateOf("")
        private set

    val isSendButtonVisible by derivedStateOf {
        createEntity.isValid()
    }

    fun getLocationFromAddress(strAddress: String, onResult: (location: Location) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses: List<Address>? = geocoder.getFromLocationName(strAddress, 5)
            Timber.d(Geocoder.isPresent().toString())
            Timber.d("addresses: $addresses")

            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]

                onResult(
                    Location(null).apply {
                        latitude = address.latitude
                        longitude = address.longitude
                    }
                )
            }

            onResult(location.value)
        }
    }

    fun getAddressFromLocation(location: Location, onResult: (address: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale("ru", "RU"))

            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]

                onResult(address.getAddressLine(0))
            }
        }
    }

    fun getAutocompleteAddressFromGoogle(address: String, onResult: (address: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val location = apiClient.getCoordinatesFromGoogle(address = address).getOrNull()
            Timber.d("location: $location")
            location?.results?.get(0)?.let { onResult(it.formattedAddress) }
        }
    }

    var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))
        private set


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

    fun sendAnimal(
        pageType: PageType
    ) {
        addAnimal(pageType, createEntity)
    }

    fun addAnimal(
        pageType: PageType,
        animal: CreateAnimalEntityDto
    ) {
        viewModelScope.launch {
            val requestBody = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                animal.animalName?.let { addFormDataPart("AnimalName", it) }
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
                animal.contacts.email?.let { addFormDataPart("Contacts.email", it) }
                animal.contacts.telegram?.let { addFormDataPart("Contacts.telegram", it) }
                animal.contacts.vk?.let { addFormDataPart("Contacts.vk", it) }
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
                url = pageType.urlPrefix, requestBody = requestBody
            )
                .onSuccess {

                }
        }
    }

}