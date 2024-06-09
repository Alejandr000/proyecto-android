package net.lrivas.proyecto_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import net.lrivas.proyecto_android.incidencias.ListarIncidencias01;
import net.lrivas.proyecto_android.usuarios.listaUsuarios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnIniciar;
    TextView pass, user;
    config objConfiguracion = new config();
    String URL = objConfiguracion.urlWebServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        pass = findViewById(R.id.usertxt);
        user = findViewById(R.id.passtxt);
        btnIniciar = findViewById(R.id.btn_iniciar);



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                iniciar();
            }
        });
    }

    private void iniciar() {

        if (pass.getText().toString().isEmpty() ||
                user.getText().toString().isEmpty()
                ) {

            Toast.makeText(MainActivity.this, "Ingrese los datos correctos", Toast.LENGTH_SHORT).show();
            return;
        }

            RequestQueue objetoPeticion = Volley.newRequestQueue( MainActivity.this);

            StringRequest iniciar  = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {


                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");


                     if (estado.equals("1")){
                         JSONArray aDatosResultado = objJSONResultado.getJSONArray("resultado");

                         JSONObject datos = aDatosResultado.getJSONObject(0);
                         String UID= datos.getString("idUsuario");
                         String USR= datos.getString("nombre_usuario");
                         String ROL= datos.getString("tipo_usuario");
                         SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                         SharedPreferences.Editor myEdit = sharedPreferences.edit();

                         // write all the data entered by the user in SharedPreference and apply
                         myEdit.putString("nombre", USR);
                         myEdit.putString("id", UID);
                         myEdit.putString("rol", ROL);
                         myEdit.apply();
                         //myEdit.clear();

                         //SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                         if (ROL.equals("Administrador")) {
                             Intent intent = new Intent(MainActivity.this, indexActivity.class);
                             startActivity(intent);
                             finish();
                         } else {
                             Intent intent = new Intent(MainActivity.this, MainActivityEmpleado.class);
                             startActivity(intent);
                             finish();
                         }


                     }
                     else{

                         Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();

                     }

                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();

                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "validar_usuario");
                    params.put("pass", pass.getText().toString());
                    params.put("user", user.getText().toString());

                    return params;
                }

            };
            objetoPeticion.add(iniciar);




    }
}