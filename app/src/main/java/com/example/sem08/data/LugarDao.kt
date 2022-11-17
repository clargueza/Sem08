package com.example.sem08.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sem08.model.Lugar


@Dao
interface LugarDao {
    //Obtener datos
    @Query("SELECT * FROM LUGAR")
    fun obtenerLugares() : LiveData<List<Lugar>>

    //Insertar datos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarLugar(lugar: Lugar)

    //Actualizar datos
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun modificarLugar(lugar: Lugar)

    //Eliminar datos
    @Delete
    suspend fun eliminarLugar(lugar: Lugar)
}