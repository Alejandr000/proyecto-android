package net.lrivas.proyecto_android.incidencias;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import net.lrivas.proyecto_android.R;
import net.lrivas.proyecto_android.clases.config;
import net.lrivas.proyecto_android.indexActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistroIncidente extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView img;
    private Uri imageUri;
    private String imagePath;

    Button btnfec;
    TextView  idUsuario;
    TextInputEditText descripcion;
    Spinner TipoIncidencia,TipoEstadoIncidencia;
    Button CancelarIncidencia, AgregarIncidencia,SeleccionarImagen;
    config configIncidencia = new config();
    String URLIncidencia = configIncidencia.urlWebServices;

    Map<String, String> roleToIdMap = new HashMap<>();
    TextView fecha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_incidente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ///
        descripcion = findViewById(R.id.inputDescripcion);
        TipoIncidencia = findViewById(R.id.SpinnerIncidencia);
        TipoEstadoIncidencia = findViewById(R.id.SpinerEstado);
        img = findViewById(R.id.imgVistaIncidencia);

        idUsuario = findViewById(R.id.txtIdUsuario);

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
        CancelarIncidencia = findViewById(R.id.CancelarIncidencia);
        AgregarIncidencia = findViewById(R.id.btnImgIncidencia);
        SeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);

        CancelarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana = new Intent(RegistroIncidente.this, indexActivity.class);
                startActivity(ventana);
            }
        });
        AgregarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registrarIncidencia();
            }
        });

        //FUNCION PARA LA IMAGEN
        SeleccionarImagen.setOnClickListener(v -> seleccionarImagen());





        btnfec = findViewById(R.id.btnfecha);
        fecha = findViewById(R.id.InputFechaIncidencia);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        btnfec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        RegistroIncidente.this,
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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

    private String getPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

    private void registrarIncidencia() {
        if (descripcion.getText().toString().isEmpty() ||
                fecha.getText().toString().isEmpty()) {
            Toast.makeText(RegistroIncidente.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            RequestQueue objetoPeticion = Volley.newRequestQueue(RegistroIncidente.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URLIncidencia, response -> {
                try {
                    JSONObject objJSONResultado = new JSONObject(response);
                    String estado = objJSONResultado.getString("estado");
                    if (estado.equals("1")) {
                        Toast.makeText(RegistroIncidente.this, "Incidencia registrada con éxito..!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistroIncidente.this, ListarIncidencias01.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistroIncidente.this, "Error: " + estado, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(RegistroIncidente.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
                final SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accion", "agregar_incidencia");
                    params.put("descripcion", descripcion.getText().toString());
                    params.put("fecha", fecha.getText().toString());
                    params.put("idUsuario", sh.getString("id",""));
                    // Get the selected role and convert it to its corresponding ID
                    String selectedRole = TipoIncidencia.getSelectedItem().toString();
                    String roleId = roleToIdMap.get(selectedRole);
                    params.put("idTipoIncidente", roleId);
                    params.put("img", imagePath); // Añadimos la ruta de la imagen
                    return params;
                }
            };
            objetoPeticion.add(peticion);
        } catch (Exception error) {
            Toast.makeText(RegistroIncidente.this, "Error en tiempo de ejecución: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}