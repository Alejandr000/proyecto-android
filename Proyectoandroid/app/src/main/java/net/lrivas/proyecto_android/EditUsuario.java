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

public class EditUsuario extends AppCompatActivity {

    EditText nombrecompleto, nombreusuario,Password,Correo;
    String id_usuario,nombre_completo, nombre_usuario,password,correo,idTipoUsuario;
    Spinner id_TipoUsuario;
    Button RegistrarEdit,CancelarRegistro,EliminarAdd;
    config objConfiguracion = new config();
    String URL = objConfiguracion.urlWebServices;

    // Map to store role to ID mappings
    Map<String, String> roleToIdMap = new HashMap<>();

    //String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_usuario);

        nombrecompleto = findViewById(R.id.nombreCompletoEdit);
        nombreusuario = findViewById(R.id.nombreUsuarioEdit);
        Password =  findViewById(R.id.passwordEdit);
        Correo = findViewById(R.id.correoEdit);
        id_TipoUsuario = findViewById(R.id.RolesEdit);

        // Initialize the role to ID mappings
        roleToIdMap.put("Administrador", "1");
        roleToIdMap.put("Empleado", "2");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Roles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        id_TipoUsuario.setAdapter(adapter);




        RegistrarEdit = findViewById(R.id.btnActualizarEdit);
        CancelarRegistro= findViewById(R.id.btnCancelarEdit);
        EliminarAdd = findViewById(R.id.btnEliminarEdit);

        RegistrarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar();
            }
        });

        CancelarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        EliminarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;






        });





    }

    private void modificar() {
        // Verificar que todos los campos estén llenos
        /*if (nombrecompleto.getText().toString().isEmpty() ||
                nombreusuario.getText().toString().isEmpty() ||
                Password.getText().toString().isEmpty() ||
                Correo.getText().toString().isEmpty() ||
                id_TipoUsuario.getSelectedItem().toString().isEmpty()) {

            Toast.makeText(EditUsuario.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }*/
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(EditUsuario.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(EditUsuario.this, "Contacto Modificado con exito", Toast.LENGTH_SHORT).show();
                            // Redirigir a la ventana de inicio de sesión
                            Intent intent = new Intent(EditUsuario.this,listaUsuarios.class);
                            startActivity(intent);
                            finish(); // Finaliza la actividad actual para que el usuario no pueda volver con el botón de atrás


                        }else{
                            Toast.makeText(EditUsuario.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditUsuario.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","editar_usuario");
                    params.put("id_usuario",id_usuario);
                    params.put("nombre_completo",nombrecompleto.getText().toString());
                    params.put("nombre_usuario",nombreusuario.getText().toString());
                    params.put("password",Password.getText().toString());
                    // Get the selected role and convert it to its corresponding ID
                    String selectedRole = id_TipoUsuario.getSelectedItem().toString();
                    String roleId = roleToIdMap.get(selectedRole);
                    params.put("idTipoUsuario", roleId);
                    params.put("correo", Correo.getText().toString());  // Agregar género seleccionado


                    return params;
                }
            };

            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(EditUsuario.this, "Error en tiempo de ejecucion: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar() {
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(EditUsuario.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(EditUsuario.this, "Usuario Eliminado con exito.!!", Toast.LENGTH_SHORT).show();
                            // Redirigir a la ventana de inicio de sesión
                            Intent intent = new Intent(EditUsuario.this, listaUsuarios.class);
                            startActivity(intent);
                            finish(); // Finaliza la actividad actual para que el usuario no pueda volver con el botón de atrás

                        }else{
                            Toast.makeText(EditUsuario.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditUsuario.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","eliminar_usuario");
                    params.put("id_usuario",id_usuario);
                    return params;
                }
            };

            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(EditUsuario.this, "Error en tiempo de ejecucion: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        Bundle valoresAdicionales = getIntent().getExtras();
        if(valoresAdicionales==null){
            Toast.makeText(EditUsuario.this, "Debe enviar un ID Usuario", Toast.LENGTH_SHORT).show();
            id_usuario = "";
            regresar();
        }else{
            id_usuario = valoresAdicionales.getString("idUsuario");
            nombre_completo = valoresAdicionales.getString("nombre_completo");
            nombre_usuario= valoresAdicionales.getString("nombre_usuario");
            password = valoresAdicionales.getString("password");
            idTipoUsuario = valoresAdicionales.getString("idTipoUsuario");
            correo = valoresAdicionales.getString("correo");

            verUsuario();
        }
    }
    private void verUsuario(){

        nombrecompleto.setText(nombre_completo);
        nombreusuario.setText(nombre_usuario);
        Password.setText(password);
        Correo.setText(correo);
        // Ajustar el Spinner para mostrar el tipo de usuario actual del usuario
        if(idTipoUsuario.equals("1")){
            id_TipoUsuario.setSelection(0); // Administrador
        } else if(idTipoUsuario.equals("2")){
            id_TipoUsuario.setSelection(1); // Empleado
        }

    }
    private void regresar(){
        Intent actividad = new Intent(EditUsuario.this, listaUsuarios.class);
        startActivity(actividad);
        EditUsuario.this.finish();
    }






}