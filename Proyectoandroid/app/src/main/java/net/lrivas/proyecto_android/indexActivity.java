package net.lrivas.proyecto_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.lrivas.proyecto_android.incidencias.ListarIncidencias01;
import net.lrivas.proyecto_android.incidencias.RegistroIncidente;
import net.lrivas.proyecto_android.usuarios.listaUsuarios;

public class indexActivity extends AppCompatActivity {

    ImageView btncerrar, viewRegistrar,viewIncidencia,viewBuscaruser,viewBuscarIncidencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index);


        viewRegistrar = findViewById(R.id.imgAgregarUsuario);
        viewIncidencia = findViewById(R.id.imgIncidencias);
        viewBuscaruser = findViewById(R.id.imgBuscarUser);
        viewBuscarIncidencia = findViewById(R.id.imgBuscarIncidencia);
        btncerrar = findViewById(R.id.btncerrar);

        btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(indexActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la otra actividad
                Intent intent = new Intent(indexActivity.this, AddUsuario.class);
                startActivity(intent);
            }
        });

        viewIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la otra actividad
                Intent intent = new Intent(indexActivity.this, RegistroIncidente.class);
                startActivity(intent);

            }
        });

        viewBuscaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la otra actividad
                Intent intent = new Intent(indexActivity.this, listaUsuarios.class);
                startActivity(intent);
            }
        });

        viewBuscarIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la otra actividad
                Intent intent = new Intent(indexActivity.this, ListarIncidencias01.class);
                startActivity(intent);
            }
        });




    }
}