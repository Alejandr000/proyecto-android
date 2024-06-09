    package net.lrivas.proyecto_android.usuarios;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ListView;
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

    import net.lrivas.proyecto_android.AddUsuario;
    import net.lrivas.proyecto_android.EditUsuario;
    import net.lrivas.proyecto_android.R;
    import net.lrivas.proyecto_android.clases.config;
    import net.lrivas.proyecto_android.indexActivity;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.HashMap;
    import java.util.Map;

    public class listaUsuarios extends AppCompatActivity {
        Button botonAgregar,botonBuscar;

        ImageView Inicio;
        ListView listaContactos;
        EditText txtCriterio;
        config config = new config();
        String URL = config.urlWebServices;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_lista_usuarios);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            botonAgregar = findViewById(R.id.btnAgregar);
            botonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ventana = new Intent(listaUsuarios.this, AddUsuario.class);
                    startActivity(ventana);
                }
            });
            txtCriterio = findViewById(R.id.txtCriterio);
            listaContactos = findViewById(R.id.lvlUsuarios);

            botonBuscar = findViewById(R.id.btnBuscar);
            botonBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarLista();
                }
            });
            Inicio = findViewById(R.id.btnInicio);
            Inicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ventana = new Intent(listaUsuarios.this, indexActivity.class);
                    startActivity(ventana);
                }
            });








        }
        @Override
        protected void onResume() {
            super.onResume();
            try{
                llenarLista();
            }catch (Exception error){
                Toast.makeText(listaUsuarios.this, "Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        public void llenarLista(){
            // final String criterio = txtCriterio.getText().toString();
            RequestQueue objetoPeticion = Volley.newRequestQueue(listaUsuarios.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        JSONArray aDatosResultado = objJSONResultado.getJSONArray("resultado");

                        AdaptadorListaContacto miAdaptador = new AdaptadorListaContacto();
                        miAdaptador.arregloDatos = aDatosResultado;
                        listaContactos.setAdapter(miAdaptador);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(listaUsuarios.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","listar_usuario");
                   /* params.put("filtro", criterio); // Filtro por género
                    params.put("genero", ""); // Filtro por nombre*/
                    String criterio = txtCriterio.getText().toString().trim();

                    // Verificar si el criterio es un nombre o un género
                /*    if (criterio.equalsIgnoreCase("Administrador") || criterio.equalsIgnoreCase("Empleado")) {
                        params.put("idTipoUsuario", criterio); // Filtro por género
                        params.put("filtro", ""); // Filtro por nombre vacío
                    } else {
                        params.put("filtro", criterio); // Filtro por nombre
                        params.put("idTipoUsuario", ""); // Filtro por género vacío
                    }*/
                    // Verificar si el criterio es un nombre o un tipo de usuario
                    if (criterio.equalsIgnoreCase("Administrador")) {
                        params.put("idTipoUsuario", "1"); // Administrador
                    } else if (criterio.equalsIgnoreCase("Empleado")) {
                        params.put("idTipoUsuario", "2"); // Empleado
                    } else {
                        params.put("filtro", criterio); // Filtro por nombre
                    }

                    return params;
                }
            };

            objetoPeticion.add(peticion);


        }


        class AdaptadorListaContacto extends BaseAdapter {
            public JSONArray arregloDatos;

            @Override
            public int getCount() {
                return arregloDatos.length();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View v, ViewGroup parent) {
                v = getLayoutInflater().inflate(R.layout.fila_usuarios, null);
                TextView txtTitulo = v.findViewById(R.id.tvTituloFilaUsuario);
                TextView txtUsuario = v.findViewById(R.id.tvUsuarioFilaUsuario);
                TextView txtPassword = v.findViewById(R.id.tvPasswordFilaUsuario);
                TextView txtTipo = v.findViewById(R.id.tvTipoFilaUsuario);
                TextView txtCorreo = v.findViewById(R.id.tvCorreoFilaUsuario);
                Button btnVer = v.findViewById(R.id.btnVerUsuario);

                JSONObject objJSON = null;
                try {
                    objJSON = arregloDatos.getJSONObject(position);
                    final String idUsuario, nombre_completo, nombre_usuario,password, idTipoUsuario,correo;
                    idUsuario = objJSON.getString("idUsuario");
                    nombre_completo = objJSON.getString("nombre_completo");
                    nombre_usuario = objJSON.getString("nombre_usuario");
                    password = objJSON.getString("password");
                    idTipoUsuario = objJSON.getString("idTipoUsuario");
                    correo = objJSON.getString("correo");

                    txtTitulo.setText(nombre_completo);
                    txtUsuario.setText(nombre_usuario);
                    txtPassword.setText(password);
                    txtTipo.setText(idTipoUsuario);
                    txtCorreo.setText(correo);
                   // txtId.setText(id_usuario);

                    // Convert idTipoUsuario to its corresponding role name
                    String rolUsuario = "";
                    switch (idTipoUsuario) {
                        case "1":
                            rolUsuario = "Administrador";
                            break;
                        case "2":
                            rolUsuario = "Empleado";
                            break;
                        default:
                            rolUsuario = "Desconocido";
                            break;
                    }
                    txtTipo.setText(rolUsuario);

                    btnVer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ventanaModificar = new Intent(listaUsuarios.this, EditUsuario.class);
                            ventanaModificar.putExtra("idUsuario", idUsuario);
                            ventanaModificar.putExtra("nombre_completo", nombre_completo);
                            ventanaModificar.putExtra("nombre_usuario", nombre_usuario);
                            ventanaModificar.putExtra("password", password);
                            ventanaModificar.putExtra("idTipoUsuario", idTipoUsuario);
                            ventanaModificar.putExtra("correo", correo);
                            startActivity(ventanaModificar);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return v;
            }
        }






    }