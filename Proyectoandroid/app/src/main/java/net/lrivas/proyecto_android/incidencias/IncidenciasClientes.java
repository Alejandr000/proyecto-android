package net.lrivas.proyecto_android.incidencias;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.lrivas.proyecto_android.MainActivityEmpleado;
import net.lrivas.proyecto_android.R;
import net.lrivas.proyecto_android.clases.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IncidenciasClientes extends AppCompatActivity {
    ListView listaIncidenciaClass;
    Button botnAddInciClass;

    ImageView botonBuscarInciClass,botonIniciClass;

    EditText txtCriterioIncidenciaClass;

    config configuraciones = new config();

    String UrlIncidencia = configuraciones.urlWebServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_incidencias_clientes);
        txtCriterioIncidenciaClass = findViewById(R.id.txtIncidenciaCriterio);
        listaIncidenciaClass = findViewById(R.id.lvIncidenciaLista);

        botnAddInciClass = findViewById(R.id.btnAddIncidenciaVistacli);
        botonBuscarInciClass = findViewById(R.id.IncidenciaBuscarCliBtn);
        botonIniciClass = findViewById(R.id.btnInicio);
        botnAddInciClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent incidenciaV = new Intent(IncidenciasClientes.this, AgregarIncidenciasCLI.class);
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
                Intent incidenciaHome = new Intent(IncidenciasClientes.this, MainActivityEmpleado.class);
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
            Toast.makeText(IncidenciasClientes.this, "Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarListaIncidencia() {
        RequestQueue objetoPeticion = Volley.newRequestQueue(IncidenciasClientes.this);
        StringRequest peticion = new StringRequest(Request.Method.POST, UrlIncidencia, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Api", response);
                    JSONObject objJSONResultado = new JSONObject(response.toString());
                    JSONArray aDatosResultadoIncidencia = objJSONResultado.getJSONArray("resultado");

                    IncidenciasClientes.AdatadorListaIncidencia miAdaptadorIncidencia = new IncidenciasClientes.AdatadorListaIncidencia();
                    miAdaptadorIncidencia.arreglosDatosIncidencia = aDatosResultadoIncidencia;
                    listaIncidenciaClass.setAdapter(miAdaptadorIncidencia);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IncidenciasClientes.this, "Error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            final SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("accion","listar_incidencia_emeplado");

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
                params.put("idUsuario", sh.getString("id",""));

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
            v = getLayoutInflater().inflate(R.layout.listaincliente, null);
            TextView txtDescripcion = v.findViewById(R.id.FilaDescripcionIncidenciacli);
            TextView txtFecha = v.findViewById(R.id.FilaFechaIncidenciacli);
            TextView txtEstado = v.findViewById(R.id.FilaEstadoIncidenciacli);
            TextView txtTipoIncidencia = v.findViewById(R.id.FilaIdTipoIncidenciacli);
            //TextView txtImg = v.findViewById(R.id.FilaImagen);
            Button btnVerIncidencia = v.findViewById(R.id.btnVerIncidenciacli);

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
                //txtImg.setText(img);


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
                        Intent ventanaModificar = new Intent(IncidenciasClientes.this, EditIncidencia.class);
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