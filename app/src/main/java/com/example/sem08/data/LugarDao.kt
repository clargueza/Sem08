package com.example.sem08.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sem08.model.Lugar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase


class LugarDao {
    //Firebase Var
    private var codigoUsuario: String
    private var firestore:FirebaseFirestore

    init {
        val usuario = Firebase.auth.currentUser?.email
        codigoUsuario = "$usuario"
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //CRUD

    fun saveLugar(lugar: Lugar) {
        val document: DocumentReference
        if (lugar.id.isEmpty()) {
            //Agregar
            document = firestore
                .collection("lugaresMiercoles")
                .document(codigoUsuario)
                .collection("misLugares")
                .document()
            lugar.id = document.id
        } else {
            //Modificar
            document = firestore
                .collection("lugaresMiercoles")
                .document(codigoUsuario)
                .collection("misLugares")
                .document(lugar.id)
        }
        document.set(lugar).addOnCompleteListener {
            Log.d("SaveLugar", "Guardado con exito")
        }
            .addOnCanceledListener {
                Log.e("SaveLugar", "Error al guardar")
            }
    }

        fun eliminarLugar(lugar: Lugar) {
            if (lugar.id.isNotEmpty()) {
                firestore.collection("lugaresMiercoles")
                    .document(codigoUsuario)
                    .collection("misLugares")
                    .document(lugar.id)
                    .delete()
                    .addOnCompleteListener {
                        Log.d("deleteLugar", "Eliminado con exito")
                    }
                    .addOnCanceledListener {
                        Log.d("deleteLugar", "Error al eliminar")
                    }
            }
        }

        fun obtenerLugares(): MutableLiveData<List<Lugar>> {
            val listaLugares = MutableLiveData<List<Lugar>>()
            firestore.collection("lugaresMiercoles")
                .document(codigoUsuario)
                .collection("misLugares")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val lista = ArrayList<Lugar>()
                        val lugares = snapshot.documents
                        lugares.forEach {
                            val lugar = it.toObject(Lugar::class.java)
                            if (lugar != null) {
                                lista.add(lugar)
                            }
                        }
                        listaLugares.value = lista
                    }
                }
            return listaLugares
        }
    }
