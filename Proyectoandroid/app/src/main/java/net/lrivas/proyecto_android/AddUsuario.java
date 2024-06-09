package net.lrivas.proyecto_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.lrivas.proyecto_android.clases.config;
import net.lrivas.proyecto_android.usuarios.listaUsuarios;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUsuario extends AppCompatActivity {


    EditText NombreCompleto, NombreUsuario,Password,Correo;
    Spinner TipoRol;
    Button BtnRegistrarAdd,CancelarRegistro;
    config objConfiguracion = new config();
    String URL = objConfiguracion.urlWebServices;

    // Map to store role to ID mappings
    Map<String, String> roleToIdMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_usuario);

        NombreCompleto = findViewById(R.id.nombreCompletoEdit);
        NombreUsuario = findViewById(R.id.nombreUsuarioEdit);
        Password =  findViewById(R.id.passwordEdit);
        Correo = findViewById(R.id.correoEdit);
        BtnRegistrarAdd= findViewById(R.id.btnActualizarEdit);

        TipoRol = findViewById(R.id.RolesEdit);

        // Initialize the role to ID mappings
        roleToIdMap.put("Administrador", "1");
        roleToIdMap.put("Empleado", "2");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Roles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        TipoRol.setAdapter(adapter);

        CancelarRegistro = findViewById(R.id.btnCancelarEdit);

        CancelarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUsuario.this, listaUsuarios.class);
                startActivity(intent);
            }
        });

        BtnRegistrarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registrar() {
        // Verificar que todos los campos estén llenos
        if (NombreCompleto.getText().toString().isEmpty() ||
                NombreUsuario.getText().toString().isEmpty() ||
                Password.getText().toString().isEmpty() ||
                Correo.getText().toString().isEmpty() ||
                TipoRol.getSelectedItem().toString().isEmpty()) {

            Toast.makeText(AddUsuario.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue( AddUsuario.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(AddUsuario.this, "Usuario Registrado con exito..!!!", Toast.LENGTH_SHORT).show();
                            // Redirigir a la ventana de inicio de sesión
                            Intent intent = new Intent(AddUsuario.this, listaUsuarios.class);
                            startActivity(intent);
                            finish(); // Finaliza la actividad actual para que el usuario no pueda volver con el botón de atrás
                        }else{
                            Toast.makeText(AddUsuario.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddUsuario.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","registrar_usuario");
                    params.put("nombre_completo",NombreCompleto.getText().toString());
                    params.put("nombre_usuario",NombreUsuario.getText().toString());
                    params.put("password",Password.getText().toString());
                    // Get the selected role and convert it to its corresponding ID
                    String selectedRole = TipoRol.getSelectedItem().toString();
                    String roleId = roleToIdMap.get(selectedRole);
                    params.put("idTipoUsuario", roleId);
                    params.put("correo", Correo.getText().toString());  // Agregar género seleccionado
                    return params;
                }
            };

            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(AddUsuario.this, "Error en tiempo de ejecucion: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}