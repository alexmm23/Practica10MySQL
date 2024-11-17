package com.example.practica10mysql
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberCheckBox: CheckBox
    private lateinit var loginButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar las vistas
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        rememberCheckBox = findViewById(R.id.rememberCheckBox)
        loginButton = findViewById(R.id.loginButton)
        clearButton = findViewById(R.id.clearButton)

        // Cargar las preferencias si están guardadas
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")
        usernameEditText.setText(savedUsername)
        passwordEditText.setText(savedPassword)

        // Configurar el botón de ingresar
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username == "admin" && password == "admin") {  // Validación simple, puedes agregar una autenticación real
                if (rememberCheckBox.isChecked) {
                    // Guardar los datos en SharedPreferences si se selecciona recordar
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.apply()
                }

                // Ir a la siguiente actividad (Formulario de Vehículos)
                val intent = Intent(this, VehiculoActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Mostrar un mensaje de error
                usernameEditText.error = "Usuario o contraseña incorrectos"
                passwordEditText.error = "Usuario o contraseña incorrectos"

            }
        }

        // Configurar el botón de limpiar
        clearButton.setOnClickListener {
            usernameEditText.text.clear()
            passwordEditText.text.clear()
            rememberCheckBox.isChecked = false
        }
    }
}
