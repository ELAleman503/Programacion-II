package com.example.micalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Calcular(View view) {
        TextView tempVal = (TextView)findViewById(R.id.num1);
        double num1 = Double.parseDouble(tempVal.getText().toString());

        tempVal = (TextView)findViewById(R.id.num2);
        double num2 = Double.parseDouble(tempVal.getText().toString());

        double respuesta = 1;

        Spinner OperacionesBasicas = findViewById(R.id.OperacionesBasicas);
        switch (OperacionesBasicas.getSelectedItemPosition()){
            case 0://Suma
                respuesta = num1+num2;
                break;
            case 1://Resta
                respuesta = num1-num2;
                break;
            case 2://Multiplicacion
                respuesta = num1*num2;
                break;
            case 3: //Division
                respuesta = num1/num2;
                break;
            case 4: //Factorial
                respuesta = 1;
                for(int i=1; i<=num1; i++){
                    respuesta = i * respuesta;}
                break;
            case 5: //porcentaje
                respuesta = (num2/100) * num1;
                break;
            case 6: //Exponente
                respuesta = Math.pow(num1,num2);
                break;
            case 7: //Raiz
                respuesta = Math.pow(num1,1/num2);
                Break;
            case 8: //modulo
                respuesta = (num1 % num2);
                break;
            case 9: //mayor que
                if ((num1 >= num2)) {respuesta= num2;} else {
                    respuesta = num1;
                    break;
                }
                tempVal = findViewById(R.id.btnCalcular);
                tempVal.setText(String.format("Respuesta: %s", respuesta));
        }
    }

}
}