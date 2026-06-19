package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LocationDao
import com.example.data.local.SavedLocation
import com.example.data.model.WeatherData
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationDao: LocationDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _savedLocations = locationDao.getAllLocations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val savedLocations: StateFlow<List<SavedLocation>> = _savedLocations

    init {
        loadInitialWeather()
    }

    private fun loadInitialWeather() {
        viewModelScope.launch {
            val currentLocation = locationDao.getCurrentUserLocation()
            if (currentLocation != null) {
                fetchWeather(currentLocation.latitude, currentLocation.longitude)
            } else {
                // Default to New York if no location saved
                fetchWeather(40.7128, -74.0060)
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val data = repository.getWeatherData(lat, lon)
                _uiState.value = WeatherUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun addLocation(name: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            locationDao.insertLocation(SavedLocation(name = name, latitude = lat, longitude = lon))
        }
    }

    fun deleteLocation(location: SavedLocation) {
        viewModelScope.launch {
            locationDao.deleteLocation(location)
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
