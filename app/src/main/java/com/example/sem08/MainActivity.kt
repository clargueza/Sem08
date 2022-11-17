package com.example.sem08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.sem08.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.Principal


class MainActivity : AppCompatActivity() {

    private lateinit var auth  : FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        setContentView(binding.root)

        //Inicializar
        val initializeApp = FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //        //Definir evento onClic  del boton Login
        binding.btLogin.setOnClickListener(){
            hacerlogin();
        }
        //Definir evento onClic  del boton Register
        binding.btRegister.setOnClickListener(){
            hacerRegister();
        }
    }

    private fun hacerRegister() {
        //Recupero la información que el usuario escribió en el App
        var email = binding.etMail.text.toString()
        var clave = binding.etClave.text.toString()

        Log.d("Registrándose", "Haciendo llamado a creación")
        //Utilizo el objeto auth para hacer el registro
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){ // SI se logró, se creó el usuario
                    Log.d( "Registrándose", "Se registró")
                    val user = auth.currentUser
                    if (user != null) {
                        refresca(user)
                    }
                }else{ // Si no se logró hubo un error
                    Log.d( "Registrándose", "Error de registro")
                    Toast.makeText(baseContext, "Falló", Toast.LENGTH_LONG).show()

                }
            }
        Log.d( "Registrándose", "Sale del proceso")
    }

    private fun hacerlogin() {
        //Recupero la información que el usuario escribió en el App
        var email = binding.etMail.text.toString()
        var clave = binding.etClave.text.toString()

        Log.d("Autenticándose", "Haciendo llamado a autenticación")
        //Utilizo el objeto auth para hacer el registro
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){ //Si se logró, se creó el usuario
                    Log.d( "Autenticando", "Se autenticó")
                    val user = auth.currentUser
                    if (user != null) {
                        refresca(user)
                    }
                }else{ //Si no se logró hubo un error
                    Log.d( "Autenticando", "Error en la autenticación")
                    Toast.makeText(baseContext, "Fallo", Toast.LENGTH_LONG).show()

                }
            }
    }

    private fun refresca(user: FirebaseUser?) {
        if (user != null){ //Si hay un usuario entonces paso al pantalla principal
            val intent = Intent( this, Principal::class.java)
            startActivity(intent)
        }
    }
    //Esto ejecuta toda vez que se presenta el app en la pantalla, valida si hay un usuario autenticado
    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        refresca(usuario)
    }

}