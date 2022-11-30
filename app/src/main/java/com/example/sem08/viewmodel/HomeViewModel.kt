package com.example.sem08.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.sem08.data.LugarDao
import com.example.sem08.model.Lugar
import com.example.sem08.repository.LugarRepository
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val obtenerLugares: MutableLiveData<List<Lugar>>
    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {
        obtenerLugares = repository.obtenerLugar
    }
    fun saveLugar(lugar: Lugar) {
        repository.guardarLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.eliminarLugar(lugar)
    }
}