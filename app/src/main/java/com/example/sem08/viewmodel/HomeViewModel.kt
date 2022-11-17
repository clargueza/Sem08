package com.example.sem08.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.sem08.data.LugarDatabase
import com.example.sem08.model.Lugar
import com.example.sem08.repository.LugarRepository
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val obtenerLugares: LiveData<List<Lugar>>
    private val repository: LugarRepository

    init {
        val lugarDao = LugarDatabase.getDatabase(application).lugarDao()
        repository = LugarRepository(lugarDao)
        obtenerLugares = repository.obtenerLugar
    }
    fun saveLugar(lugar: Lugar) {
        viewModelScope.launch { repository.guardarLugar(lugar) }
    }

    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch { repository.eliminarLugar(lugar) }
    }
}