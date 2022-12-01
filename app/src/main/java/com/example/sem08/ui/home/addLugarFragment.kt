package com.example.sem08.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sem08.R
import com.example.sem08.databinding.FragmentAddLugarBinding
import com.example.sem08.model.Lugar
import com.example.sem08.utilidades.AudioUtilities
import com.example.sem08.utilidades.ImagenUtiles
import com.example.sem08.viewmodel.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


/**
 * A simple [Fragment] subclass.
 * Use the [addLugarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addLugarFragment : Fragment() {
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var audioUtiles: AudioUtilities

    private lateinit var imagenUtiles: ImagenUtiles
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAddLugar.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msgGuardandoLugar)
            binding.msgMensaje.visibility = TextView.VISIBLE

            subirAudio()
        }

        //Audio
        audioUtiles = AudioUtilities(requireActivity(), requireContext(), binding.btAccion, binding.btPlay, binding.btDelete,
        getString(R.string.msgInicioNotaAudio), getString(R.string.msgDetineNotaAudio))

        //Fotos
        tomarFotoActivity = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles(requireContext(), binding.btPhoto, binding.btRotaL,
            binding.btRotaR, binding.imagen, tomarFotoActivity)


        //binding.btAgregarLugar.setOnClickListener { agregarLugar()}

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun subirAudio(){
        val audioFile = audioUtiles.audioFile
        if(audioFile.exists() && audioFile.isFile && audioFile.canRead()){
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subirImagen(rutaAudio)
                        }
                }
                .addOnCanceledListener { subirImagen("") }
        }
        else{
            subirImagen("")
        }
    }

    private fun subirImagen(rutaAudio:String){
        val imagenFile = imagenUtiles.imagenFile
        if(imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()){
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/images/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            agregarLugar(rutaAudio, rutaImagen)
                        }
                }
                .addOnCanceledListener { agregarLugar(rutaAudio, "") }
        }
        else{
            agregarLugar(rutaAudio, "")
        }
    }

    //Efectivamente hace el registro del lugar en la base de datos
    private fun agregarLugar(rutaAudio: String, rutaImagen: String) {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etEmail.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()

        if(nombre.isNotEmpty()){
           val lugar = Lugar("", nombre, correo, telefono, web, rutaAudio, rutaImagen)
            //Proceso de agregar BD
            homeViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(), "Lugar agregado",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_home_to_addLugarFragment)
        }
        else{ //NO hay info de lugar
            Toast.makeText(requireContext(), getString(R.string.msg_error), Toast.LENGTH_LONG).show()
        }
    }
}