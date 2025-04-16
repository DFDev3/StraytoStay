package com.ejemplo.straytostay

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeAdoptanteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_adoptante)

        // Tarjeta 1: Adopción exitosa
        val card1 = findViewById<View>(R.id.card1)
        val icono1 = card1.findViewById<ImageView>(R.id.icono)
        val titulo1 = card1.findViewById<TextView>(R.id.titulo)
        val descripcion1 = card1.findViewById<TextView>(R.id.descripcion)

        icono1.setImageResource(R.drawable.ic_adopcion)
        titulo1.text = "¡Adopción exitosa!"
        descripcion1.text = "Luna fue adoptada por Ana en Bucaramanga. ¡Gracias por cambiar una vida!"

        // Tarjeta 2: Nueva entidad vinculada
        val card2 = findViewById<View>(R.id.card2)
        val icono2 = card2.findViewById<ImageView>(R.id.icono)
        val titulo2 = card2.findViewById<TextView>(R.id.titulo)
        val descripcion2 = card2.findViewById<TextView>(R.id.descripcion)

        icono2.setImageResource(R.drawable.ic_entidad)
        titulo2.text = "Nueva entidad vinculada"
        descripcion2.text = "Veterinaria San Francisco ahora forma parte de StrayToStay."

        // Tarjeta 3: Tip educativo
        val card3 = findViewById<View>(R.id.card3)
        val icono3 = card3.findViewById<ImageView>(R.id.icono)
        val titulo3 = card3.findViewById<TextView>(R.id.titulo)
        val descripcion3 = card3.findViewById<TextView>(R.id.descripcion)

        icono3.setImageResource(R.drawable.ic_tips)
        titulo3.text = "Tips para educar a tu mascota"
        descripcion3.text = "Nuevo video: ¿Cómo enseñar a tu perro a hacer sus necesidades fuera de casa?"
    }
}