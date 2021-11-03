package com.example.scroll;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
public class MainActivity extends AppCompatActivity {
    private TextView LeerTexto;
    private EditText GuardarTexto;
    private String Texto;
    private String Linea;
    private static final String Fichero = "fichero.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LeerTexto = findViewById(R.id.textComment);
        GuardarTexto = findViewById(R.id.articleComment);
    }
                //PRIMERO CREAMOS LOS METOSO DE GUARDAR Y LEER FICHERO

    //METODO PARA GUARDAR FICHERO
    private void guardarFichero() {
        //Escribimos encima de guardartexto
        Texto = GuardarTexto.getText().toString();
        FileOutputStream fileOutputStream = null;
        try {
            //ABRIMOS FICHERO
            fileOutputStream = openFileOutput(Fichero, MODE_PRIVATE);
            //ESCRIBIMOS FICHERO
            fileOutputStream.write(Texto.getBytes());
            //IMPRIMIMOS POR PANTALLA
            Log.d("ScrollText", "Fichero guardado en " + getFilesDir() + "/" + Fichero);
            //ENSEÑAMOS LA NOTIFICACION DEL GUARDADO
            Toast.makeText(getBaseContext(), "guardado con exito", Toast.LENGTH_LONG).show();
        }
        //COMPROBACION SI ESTA ABIERTO PARA CERRARLO
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //METODO PARA LEER FICHERO
    public void leerFichero() {
        FileInputStream fileInputStream = null;

        try {
            //ABRIMOS FICHERO
            fileInputStream = openFileInput(Fichero);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            //CREAMOS BUFFERED SOBRE EL INPUTSTREAM
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //CUANDO SE ACABA LA LINEA AÑADIMOS INTRO
            while ((Linea = bufferedReader.readLine()) != null) {
                stringBuilder.append(Linea).append("\n");
            }
            //AÑADIMOS EL TEXTO NUEVO
            LeerTexto.setText(LeerTexto.getText()+stringBuilder.toString());
        }
        //COMPROBAMOS SI ESTA ABIERTO PARA CERRARLO
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void MostrarTexto(View view) {
        Button buttonAddComment = findViewById(R.id.comentario);

        if (!GuardarTexto.isEnabled()) {
            //TEXTO VISBIBLE
            GuardarTexto.setVisibility(View.VISIBLE);
            //CURSOR VISIBLE
            GuardarTexto.setCursorVisible(true);
            GuardarTexto.setHint("Añadir comentario");
            //PONEMOS EN MODO HABILITADO
            GuardarTexto.setEnabled(true);
            //CONFIGURAMOS EL TIPO DE ENTRADA
            GuardarTexto.setInputType(InputType.TYPE_CLASS_TEXT);
            buttonAddComment.setText("Guardar");

        } else {
            GuardarTexto.setEnabled(false);
            buttonAddComment.setText("añadir comentario");
            //METODO DE GUARDAR
            guardarFichero();
            //HACEMOS UN CLEAN
            GuardarTexto.setText("");
            //LEEMOS EL COMENTARIO
            leerFichero();
            //UTILIZAMOS EL TECLADO
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            //ESCONDEMOS EL TECLADO
            inputMethodManager.hideSoftInputFromWindow(this.LeerTexto.getWindowToken(), 0);
        }
    }
}
