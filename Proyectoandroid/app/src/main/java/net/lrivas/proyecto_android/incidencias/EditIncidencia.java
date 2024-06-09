package net.lrivas.proyecto_android.incidencias;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;

import net.lrivas.proyecto_android.EditUsuario;
import net.lrivas.proyecto_android.MainActivity;
import net.lrivas.proyecto_android.MainActivityEmpleado;
import net.lrivas.proyecto_android.R;
import net.lrivas.proyecto_android.clases.config;
import net.lrivas.proyecto_android.indexActivity;
import net.lrivas.proyecto_android.usuarios.listaUsuarios;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditIncidencia extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView img;
    private Uri imageUri;
    private String imagePath,idIncidente;

    Button btnfec;
    TextView idUsuario;
    TextInputEditText descripcion;
    Spinner TipoIncidencia,TipoEstadoIncidencia;
    Button CancelarIncidenciaEdit, ActualizarIncidenciaEdit,SeleccionarImagen,EliminarIncidencia;
    config configIncidencia = new config();
    String URLIncidencia = configIncidencia.urlWebServices;

    String descripcionEdit,fechaEdit,statusEdit,idUsuarioEdit,idTipoIncidenteEdit,imgEdit;

    Map<String, String> roleToIdMap = new HashMap<>();
    TextView fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_incidencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        descripcion = findViewById(R.id.EditDescripcion);
        TipoIncidencia = findViewById(R.id.SpinnerEdit);
        TipoEstadoIncidencia = findViewById(R.id.SpinerEstadoEdit);
        img = findViewById(R.id.imgVistaIncidenciaEdit);

        idUsuario = findViewById(R.id.txtIdUsuarioEdit);

        idUsuario.setText("1");

        roleToIdMap.put("Electrocucion","1");
        roleToIdMap.put("Golpe","2");
        roleToIdMap.put("Otros","3");

        // Crear un ArrayAdapter usando un array de strings y un diseño de spinner predeterminado
        ArrayAdapter<CharSequence> adapterIncidencia = ArrayAdapter.createFromResource(this,
                R.array.RolesIncidencia, android.R.layout.simple_spinner_item);
        adapterIncidencia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TipoIncidencia.setAdapter(adapterIncidencia);

        // Crear un ArrayAdapter usando un array de strings y un diseño de spinner predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.RolesEstado, android.R.layout.simple_spinner_item);

        // Especificar el diseño que se usará cuando la lista de opciones aparezca
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el adaptador al spinner
        TipoEstadoIncidencia.setAdapter(adapter);

        ////BOTONES....
        CancelarIncidenciaEdit = findViewById(R.id.CancelarIncidenciaEdit);
        ActualizarIncidenciaEdit = findViewById(R.id.btnImgIncidenciaEdit);
        SeleccionarImagen = findViewById(R.id.btnSeleccionarImagenEdit);
        EliminarIncidencia = findViewById(R.id.btnEliminarIncidencia);

        CancelarIncidenciaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
        ActualizarIncidenciaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarIncidencia();
            }
        });

        EliminarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarIncidencia();
            }
        });
        //FUNCION PARA LA IMAGEN
        SeleccionarImagen.setOnClickListener(v -> seleccionarImagen());

        btnfec = findViewById(R.id.btnfechaEdit);
        fecha = findViewById(R.id.InputFechaIncidenciaEdit);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        btnfec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        EditIncidencia.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                fecha.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });





    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                img.setImageBitmap(bitmap);
                imagePath = saveImageToInternalStorage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = new File(getFilesDir(), "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void modificarIncidencia() {
        try {
            RequestQueue objetoPeticion = Volley.newRequestQueue(EditIncidencia.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URLIncidencia, response -> {
                try {
                    JSONObject objJSONResultado = new JSONObject(response);
                    String estado = objJSONResultado.getString("estado");
                    if (estado.equals("1")) {
                        Toast.makeText(EditIncidencia.this, "Incidencia registrada con éxito..!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditIncidencia.this, ListarIncidencias01.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditIncidencia.this, "Error: " + estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(EditIncidencia.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "editar_incidencia");
                    params.put("idIncidente", idIncidente);
                    params.put("descripcionEdit", descripcion.getText().toString()); // Cambiado a descripcionEdit
                    params.put("fechaEdit", fecha.getText().toString()); // Cambiado a fechaEdit
                    String estadoSeleccionado = TipoEstadoIncidencia.getSelectedItem().toString();
                    params.put("statusEdit", estadoSeleccionado);  // Cambiado a statusEdit
                    params.put("idUsuarioEdit", idUsuario.getText().toString()); // Cambiado a idUsuarioEdit
                    String selectedRole = TipoIncidencia.getSelectedItem().toString();
                    String id_TipoIncidente = roleToIdMap.get(selectedRole);
                    params.put("idTipoIncidenteEdit", id_TipoIncidente); // Cambiado a idTipoIncidenteEdit
                    params.put("imgEdit", imagePath); // Cambiado a imgEdit
                    return params;
                }
            };
            objetoPeticion.add(peticion);
        } catch (Exception error) {
            Toast.makeText(EditIncidencia.this, "Error en tiempo de ejecución: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void eliminarIncidencia() {
        try{
            RequestQueue objetoPeticion = Volley.newRequestQueue(EditIncidencia.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URLIncidencia, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(EditIncidencia.this, "Incidencia Eliminado con exito", Toast.LENGTH_SHORT).show();
                            // Redirigir a la ventana de inicio de sesión
                            Intent intent = new Intent(EditIncidencia.this, ListarIncidencias01.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(EditIncidencia.this, "Error: "+ estado, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditIncidencia.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","eliminar_incidencia");
                    params.put("idIncidente",idIncidente);
                    return params;
                }
            };

            objetoPeticion.add(peticion);
        }catch (Exception error){
            Toast.makeText(EditIncidencia.this, "Error en tiempo de ejecucion: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle valoresAdicionales = getIntent().getExtras();
        if (valoresAdicionales == null) {
            Toast.makeText(EditIncidencia.this, "Debe enviar un ID de incidencia", Toast.LENGTH_SHORT).show();
            idIncidente = "";
            regresar();
        } else {
            idIncidente = valoresAdicionales.getString("idIncidente");
            descripcion.setText(valoresAdicionales.getString("descripcion"));
            fecha.setText(valoresAdicionales.getString("fecha"));
            String estado = valoresAdicionales.getString("status");
            idUsuario.setText(valoresAdicionales.getString("idUsuario"));
            String idTipoIncidenteEdit = valoresAdicionales.getString("idTipoIncidente");
            String imgEdit = valoresAdicionales.getString("img");

            if (estado != null) {
                if (estado.equals("Activo")) {
                    TipoEstadoIncidencia.setSelection(0);
                } else if (estado.equals("Resuelto")) {
                    TipoEstadoIncidencia.setSelection(1);
                }
            }

            if (idTipoIncidenteEdit != null) {
                if (idTipoIncidenteEdit.equals("1")) {
                    TipoIncidencia.setSelection(0);
                } else if (idTipoIncidenteEdit.equals("2")) {
                    TipoIncidencia.setSelection(1);
                } else if (idTipoIncidenteEdit.equals("3")) {
                    TipoIncidencia.setSelection(2);
                }
            }

            if (imgEdit != null && !imgEdit.isEmpty()) {
                File imgFile = new File(imgEdit);
                if (imgFile.exists()) {
                    Bitmap myBitmap;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            myBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), Uri.fromFile(imgFile)));
                        } else {
                            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imgFile));
                        }
                        img.setImageBitmap(myBitmap);
                        imagePath = imgEdit;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void regresar() {

        final SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String ROL= sh.getString("rol","");

        if (ROL.equals("Administrador")) {
            Intent intent = new Intent(EditIncidencia.this, ListarIncidencias01.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(EditIncidencia.this, IncidenciasClientes.class);
            startActivity(intent);
            finish();
        }


    }

  /*  private void verIncidencia() {
        descripcion.setText(descripcionEdit);
        fecha.setText(fechaEdit);



        if (statusEdit.equals("Activo")){
            TipoEstadoIncidencia.setSelection(0);
        }else if (statusEdit.equals("Resuelto")){
            TipoEstadoIncidencia.setSelection(1);
        }
        idUsuario.setText(idUsuarioEdit);

        // Ajustar el Spinner para mostrar el tipo de usuario actual del usuario
        if(idTipoIncidenteEdit.equals("1")){
            TipoIncidencia.setSelection(0); // Administrador
        } else if(idTipoIncidenteEdit.equals("2")){
            TipoIncidencia.setSelection(1); // Empleado
        }else if(idTipoIncidenteEdit.equals("3")){
            TipoIncidencia.setSelection(2); // Empleado
        }

        if (imgEdit != null && !imgEdit.isEmpty()) {
            File imgFile = new File(imgEdit);
            if (imgFile.exists()) {
                Bitmap myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imgFile));
                img.setImageBitmap(myBitmap);
                imagePath = imgEdit;
            }



    }

    private void regresar() {
        // Redirigir a la ventana de inicio de sesión
        Intent intent = new Intent(EditIncidencia.this, ListarIncidencias01.class);
        startActivity(intent);
        finish();
    }*/


}