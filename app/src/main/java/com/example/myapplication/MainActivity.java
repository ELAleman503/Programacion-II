package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TabHost  tbhgeneral;
    Button BotonR;
    TextView tempVal;
    Spinner spnOpcionDe, spnOpcionA;
    conversores miConversor = new conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbhgeneral = findViewById(R.id.tbhparcia_1);
        tbhgeneral.setup();

        tbhgeneral.addTab(tbhgeneral.newTabSpec("CalculadordeAgua").setContent(R.id.ContadorAgua).setIndicator("",getResources().getDrawable(R.drawable.ic_agua)));
        tbhgeneral.addTab(tbhgeneral.newTabSpec("Area").setContent(R.id.Area).setIndicator("",getResources().getDrawable(R.drawable.ic_area)));

        BotonR = findViewById(R.id.btnCalcular);
        BotonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
            }

            private void calcular() {
                try {
                    tempVal = (TextView) findViewById(R.id.TXTcantidad);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    tempVal = (TextView) findViewById(R.id.LblRespuesta);
                    /*Valiables*/
                    double calculo ;
                    double ca = cantidad;
                    double resultado1 ;
                    double resultado2 ;
                    double resultado ;
                    double calcular ;
                    double x ;


                    if (cantidad >=0 && cantidad <=18 ){
                        tempVal.setText("Usted Pagara la cantidad de $6");
                    } else if (cantidad >= 19 & cantidad <=28){

                        calcular = cantidad - 18;

                        x = calcular * 0.45;

                        resultado = 6 + x;

                        tempVal.setText("Usted Pagara la cantidad de $" + resultado);
                    } else {

                        calculo = ca - 28;
                        resultado1 = calculo * 0.65;
                        resultado2 = 10 * 0.45;
                        resultado = 6 + resultado1 + resultado2;
                        tempVal.setText("Usted Pagara la cantidad de $" + resultado);
                    }
                }catch (Exception e){
                    tempVal = findViewById(R.id.LblRespuesta);
                    tempVal.setText("Por favor ingrese algun valor correspondiente");
                    Toast.makeText(getApplicationContext(), "Por favor ingrese algun valor correspondiente", Toast.LENGTH_LONG).show();
                }

            }
        });
        /*Calcular Area*/
        BotonR = findViewById(R.id.btnCalcularA);
        BotonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempVal = findViewById(R.id.txtCantidadA);
                    double cantidad= Double.parseDouble(tempVal.getText().toString());
                    spnOpcionDe=findViewById(R.id.cboDel);
                    spnOpcionA=findViewById(R.id.cboA);
                    tempVal = findViewById(R.id.lblRespuestaA);
                    tempVal.setText("Respuesta" + miConversor.convertir(7,spnOpcionDe.getSelectedItemPosition(),spnOpcionA.getSelectedItemPosition(),cantidad));
                }catch(Exception e){
                    tempVal = findViewById(R.id.lblRespuestaA);
                    tempVal.setText("Por favor ingrese algun valor correspondiente");
                    Toast.makeText(getApplicationContext(),"Por Favor ingrese algun valor" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
class conversores{
    double[][] conversor = {
            {1.0,0.1323,0.111111,0.092903,0.00014774656489,0.000013188960818,0.0000092903},/*Area*/
    };
    public double convertir(int opcion, int de, int a, double cantidad){
        return conversor[opcion][a] / conversor[opcion][de] * cantidad;
    }
}