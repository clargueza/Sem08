package com.example.sem08.repository

import androidx.lifecycle.MutableLiveData
import com.example.sem08.data.LugarDao
import com.example.sem08.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {

    fun guardarLugar(lugar: Lugar){
        lugarDao.saveLugar(lugar)
    }

    fun eliminarLugar(lugar: Lugar){
        lugarDao.eliminarLugar(lugar)
    }

    val obtenerLugar: MutableLiveData<List<Lugar>> = lugarDao.obtenerLugares()
}