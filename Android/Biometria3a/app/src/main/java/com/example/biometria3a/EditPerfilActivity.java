package com.example.biometria3a;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditPerfilActivity extends AppCompatActivity {

    private ImageView menuIcon;

    private EditText editName, editEmail, editPhone, editContrasenia;
    private ApiService apiService;
    private MenuHandler menuHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil); // Asegúrate de que el nombre del XML sea correcto





    // Configurar el Toolbar
    Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


        // Inicializar los campos
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editContrasenia = findViewById(R.id.edit_contrasenia);



        // Configurar el servicio API
        apiService = ApiClient.getClient().create(ApiService.class);

        // Cargar datos del usuario
        loadUserData();
        if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    ImageView userIcon = findViewById(R.id.user_icon);

    // Configurar OnClickListener para el ícono de usuario
        userIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Crear un Intent para abrir EditProfileActivity
            Intent intent = new Intent(EditPerfilActivity.this, EditPerfilActivity.class);
            startActivity(intent);
        }
    });


        menuHandler = new MenuHandler(this);
        menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(menuHandler::showPopupMenu);


        ImageView logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad del mapa
                Intent intent = new Intent(EditPerfilActivity.this, Mapa_Activity .class);
                startActivity(intent);
            }
        });

        // Botón para guardar cambios
        Button btnSave = findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();  // Guardar los datos cuando el botón es presionado
            }
        });
}




// Inflar el menú cuando se crea la actividad
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    return true;
}



    private int getUserIdFromSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1); // Devuelve -1 si no existe
    }
    private void loadUserData() {
        int userId = getUserIdFromSession();
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no encontrado en la sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cambiar el tipo del 'Call' para que sea 'List<Usuario>'
        Call<List<Usuario>> call = apiService.getUserById(userId);
        call.enqueue(new Callback<List<Usuario>>() {  // Callback para lista de usuarios
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Usuario user = response.body().get(0); // Obtén el primer elemento del array
                    editName.setText(user.getNombre());
                    editEmail.setText(user.getCorreo());
                    editPhone.setText(user.getTelefono());
                    editContrasenia.setText(""); // No mostrar la contraseña por seguridad
                } else {
                    Toast.makeText(EditPerfilActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(EditPerfilActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        // Obtener el ID del usuario desde la sesión
        int userId = getUserIdFromSession();
        Log.d("EditPerfilActivity", "ID de usuario en sesión: " + userId);
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no encontrado en la sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener los datos ingresados por el usuario
        String nombre = editName.getText().toString().trim();
        String email = editEmail.getText().toString();
        String telefono = editPhone.getText().toString().trim();
        String contrasena = editContrasenia.getText().toString().trim();

        // Validaciones básicas para asegurarse de que no estén vacíos los campos obligatorios
        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato del correo electrónico (si es necesario)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato del teléfono (si es necesario)
        if (telefono.length() < 9) {
            Toast.makeText(this, "El número de teléfono es demasiado corto.", Toast.LENGTH_SHORT).show();
            return;
        }


        Usuario usuario = new Usuario(userId,nombre, telefono, email, contrasena);
        // Crear un objeto Usuario con los nuevos datos
       /* Usuario usuario = new Usuario();
        usuario.setId_usuario(userId);  // Establecer el ID del usuario
        usuario.setNombre(nombre);      // Establecer el nombre
        usuario.setCorreo(email);       // Establecer el correo
        usuario.setTelefono(telefono);  // Establecer el teléfono

        */
        // Solo actualizar la contraseña si fue modificada
       /* if (!contrasena.isEmpty()) {
            usuario.setContrasena(contrasena);  // Establecer la contraseña si no está vacía
        }
*/
        // Log para verificar los datos que se van a enviar al backend
        Log.d("EditPerfilActivity", "Datos enviados al servidor: " + new Gson().toJson(usuario));

        // Llamar al endpoint para actualizar el usuario
        Call<Void> call = apiService.updateUser(userId, usuario);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPerfilActivity.this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
                    finish();  // Cerrar la actividad
                } else {
                    try {
                        // Obtener el cuerpo de la respuesta como texto
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No hay información de error";
                        Log.d("EditPerfilActivity", "Error al actualizar los datos: " + response.code() + " " + response.message());
                        Log.d("EditPerfilActivity", "Cuerpo de la respuesta: " + errorBody);
                        Toast.makeText(EditPerfilActivity.this, "Error al actualizar los datos.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditPerfilActivity.this, "Error al leer el cuerpo de la respuesta.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Si ocurre un error en la conexión, mostrar el mensaje de error
                Toast.makeText(EditPerfilActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("EditPerfilActivity", "Error de conexión: " + t.getMessage());
            }
        });
    }




// --------------------------------------------------------------
// --------------------------------------------------------------

}
