package com.example.segundocasopratico;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

// Já que iremos trabalhar com os sensores do sistema, não podemos deixar de implementar a classe com o
// SensorEventListener, que irá nos dar resposatas de ações.
public class ClassPlaySensorMovimento extends AppCompatActivity implements SensorEventListener{

    // Declrando as variáveis.
    static SensorManager sensorManager;
    static Sensor sensor;


    public Button reprodAudio;
    public Button fecharlayout;
    public String pathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_play_e_sensor_movimento);

        // Adicionando icone da aplicação em todas as actionBars.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_app);

        // Obtendo um sensorManager para que possamos interagir com o sensor do disposítivo caso tenha.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        // Insntanciando os botões do layout e associando os mesmos aos seus Id´s, para que possamos excutar o método onClick.
        reprodAudio = findViewById(R.id.btnReproduzirGravacao);
        fecharlayout = findViewById(R.id.btnFecharLayout);


        //Utilizando o método para conseguir reproduzir o som armazenado ao clicar no botão REPRODUZIR.
        reprodAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReproducaoAudio();
            }
        });


        // O método abaixo será responsável em levar o usuário a tela principal caso o botão fechar seje selecionado.
        fecharlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void ReproducaoAudio() {
            pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gravavao.mp3";
            MediaPlayer mediaPlayer = new MediaPlayer(); // Criando um novo objeto do tipo MediaPlayer.
            try {
                mediaPlayer.setDataSource(pathFile); // Informado o caminho da origem do aúdio a ser reproduzido.
                mediaPlayer.prepare(); // Preparando para reproduzir
            }catch (IOException e){
                Log.d("Erro","Erro ao executar a gravação");
            }
            mediaPlayer.start(); // Iniciando a reprodução do aúdio
            Toast.makeText(getApplicationContext(),"Reproduzindo Gravação",Toast.LENGTH_SHORT).show();
        }


    // Método que irá responder ao valor do evento, deixei 5 para que precise abanar um pouco mais forte o telemóvel, e não acionar com um movimento tão sensível.
    @Override
    public void onSensorChanged(SensorEvent event) {
            if(event.values[0] > 5){
                ReproducaoAudio();
                //voltarTelaInicial();
                Toast.makeText(getApplicationContext(),"Reproduzindo Aúdio com o movimento",Toast.LENGTH_SHORT).show();

            }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //No método onResume() precismos ligar a “escuta” do sensor,
    // passando o objeto listener e indicando qual o sensor e a velocidade de atualização do mesmo.
    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        super.onStop();
    }

    // Esse método seria utilizado caso assim que for ativado a gravação pelo movimento, voltaria para a tela inicial.
    /*
    public void voltarTelaInicial(){
        Intent i = new Intent();
        i.setClass(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }*/

}
