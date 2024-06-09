package net.lrivas.proyecto_android.incidencias;


import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
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


import net.lrivas.proyecto_android.R;

import net.lrivas.proyecto_android.clases.config;
import net.lrivas.proyecto_android.indexActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ListarIncidencias01 extends AppCompatActivity {

    Button botnAddInciClass,botonBuscarInciClass;
    ListView listaIncidenciaClass;

    ImageView botonIniciClass;
    EditText txtCriterioIncidenciaClass;

    config configuraciones = new config();

    String UrlIncidencia = configuraciones.urlWebServices;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_incidencias01);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar el botón antes de usarlo
        txtCriterioIncidenciaClass = findViewById(R.id.txtxIncidenciaCriterio);
        listaIncidenciaClass = findViewById(R.id.lvIncidenciaLista);
        botnAddInciClass = findViewById(R.id.btnAddIncidenciaVista);
        botonBuscarInciClass = findViewById(R.id.IncidenciaBuscarBtn);
        botonIniciClass = findViewById(R.id.btnInicio);
        botnAddInciClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent incidenciaV = new Intent(ListarIncidencias01.this, RegistroIncidente.class);
                startActivity(incidenciaV);
            }
        });

        botonBuscarInciClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarListaIncidencia();
            }
        });

        botonIniciClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón de inicio
                Intent incidenciaHome = new Intent(ListarIncidencias01.this, indexActivity.class);
                startActivity(incidenciaHome);
            }
        });

    }
    @Override
    protected  void onResume(){
        super.onResume();
        try{
            llenarListaIncidencia();
        }catch (Exception error){
            Toast.makeText(ListarIncidencias01.this, "Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarListaIncidencia() {
        RequestQueue objetoPeticion = Volley.newRequestQueue(ListarIncidencias01.this);
        StringRequest peticion = new StringRequest(Request.Method.POST, UrlIncidencia, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Api", response);
                    JSONObject objJSONResultado = new JSONObject(response.toString());
                    JSONArray aDatosResultadoIncidencia = objJSONResultado.getJSONArray("resultado");

                    AdatadorListaIncidencia miAdaptadorIncidencia = new AdatadorListaIncidencia();
                    miAdaptadorIncidencia.arreglosDatosIncidencia = aDatosResultadoIncidencia;
                    listaIncidenciaClass.setAdapter(miAdaptadorIncidencia);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarIncidencias01.this, "Error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("accion","listar_incidencia");

                String criterio = txtCriterioIncidenciaClass.getText().toString().trim();

                // Verificar si el criterio es un nombre o un tipo de usuario
                if (criterio.equalsIgnoreCase("Electrocucion")) {
                    params.put("idTiponIncidente", "1"); // Administrador
                } else if (criterio.equalsIgnoreCase("Golpe")) {
                    params.put("idTiponIncidente", "2"); // Empleado
                }else if (criterio.equalsIgnoreCase("Otros")) {
                    params.put("idTiponIncidente", "3"); // Empleado
                } else {
                    params.put("filtro", criterio); // Filtro por nombre
                }

                return params;
            }
        };
objetoPeticion.add(peticion);

    }

    class  AdatadorListaIncidencia extends BaseAdapter
    {
        public JSONArray arreglosDatosIncidencia;

        @Override
        public int getCount() {
            return arreglosDatosIncidencia.length();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View v, ViewGroup parent) {
            v = getLayoutInflater().inflate(R.layout.fila_incidencias, null);
            TextView txtDescripcion = v.findViewById(R.id.FilaDescripcionIncidencia);
            TextView txtFecha = v.findViewById(R.id.FilaFechaIncidencia);
            TextView txtEstado = v.findViewById(R.id.FilaEstadoIncidencia);
            TextView txtTipoIncidencia = v.findViewById(R.id.FilaIdTipoIncidencia);
            TextView txtImg = v.findViewById(R.id.FilaImagen);
            Button btnVerIncidencia = v.findViewById(R.id.btnVerIncidencia);

            JSONObject objJSON = null;
            try {
                objJSON = arreglosDatosIncidencia.getJSONObject(position);
                final String idIncidente, idTipoIncidente, descripcion, fecha, status, idUsuario,img;
                idIncidente = objJSON.getString("idIncidente");
                idTipoIncidente = objJSON.getString("idTipoIncidente");
                descripcion = objJSON.getString("descripcion");
                fecha = objJSON.getString("fecha");
                status = objJSON.getString("status");
                idUsuario = objJSON.getString("idUsuario");
                img = objJSON.getString("img");

                txtDescripcion.setText(descripcion);
                txtFecha.setText(fecha);
                txtEstado.setText(status);
                txtTipoIncidencia.setText(idTipoIncidente);
                txtImg.setText(img);


                // Convert idTipoUsuario to its corresponding role name
                String rolIncidencia = "";
                switch (idTipoIncidente) {
                    case "1":
                        rolIncidencia = "Electrocucion";
                        break;
                    case "2":
                        rolIncidencia  = "Golpe";
                        break;
                    case "3":
                        rolIncidencia  = "Otros";
                        break;
                    default:
                        rolIncidencia  = "Desconocido";
                        break;
                }
                txtTipoIncidencia.setText(rolIncidencia);

                btnVerIncidencia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ventanaModificar = new Intent(ListarIncidencias01.this, EditIncidencia.class);
                        ventanaModificar.putExtra("idIncidente", idIncidente);
                        ventanaModificar.putExtra("descripcion", descripcion);
                        ventanaModificar.putExtra("fecha", fecha);
                        ventanaModificar.putExtra("status", status);
                        ventanaModificar.putExtra("idUsuario", idUsuario);
                        ventanaModificar.putExtra("idTipoIncidente", idTipoIncidente);
                        ventanaModificar.putExtra("img", img);

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