package net.lrivas.proyecto_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import net.lrivas.proyecto_android.incidencias.AgregarIncidenciasCLI;
import net.lrivas.proyecto_android.incidencias.IncidenciasClientes;

public class MainActivityEmpleado extends AppCompatActivity {

    TextView nuser;
    CardView op1, op2;
    ImageView btnc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_empleado);
        nuser = findViewById(R.id.textView11);
        op1 = findViewById(R.id.cardView);
        op2 = findViewById(R.id.card2);
        final SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        nuser.setText(sh.getString("nombre", ""));

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityEmpleado.this, AgregarIncidenciasCLI.class);
                startActivity(intent);
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityEmpleado.this, IncidenciasClientes.class);
                startActivity(intent);
            }
        });

        btnc = findViewById(R.id.btncerrar);


        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityEmpleado.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}