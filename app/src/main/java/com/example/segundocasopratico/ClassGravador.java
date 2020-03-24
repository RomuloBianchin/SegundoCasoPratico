package com.example.segundocasopratico;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Objects;

// Já que iremos trabalhar com os sensores do sistema, não podemos deixar de implementar a classe com o SensorEventListener, que irá nos dar resposatas de ações.
public class ClassGravador extends AppCompatActivity implements SensorEventListener {

    // Criando um objeto do tipo SensorManager e Button
    private SensorManager mSensorGravador;
    MediaRecorder gravador;
    public String pathFile = null; // Criamos essa variável public, pois iremos precisar acessar a mesma na classe para reproduzir o aúdio.

    // Objeto do tipo Button, o mesmo será utilizado para fechar o layout caso o usuário náo queira iniciar nenhuma gravação.
    Button btnFecharGravacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gravador);

        // Adicionando icone em todas as actionBars.
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_app);


        // Instanciando o botão, e passando o método onClick, para fechar o layout caso não queira se fazer nenhuma gravação.
        Button btnFecharGravacao = (Button) findViewById(R.id.btnFecharLayoutGravar);
        btnFecharGravacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Obtendo um sensorManager para que possamos interagir com o sensor do disposítivo caso tenha.
        mSensorGravador = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    /*O método onSensorChanged(), será invocado sempre que ocorrer uma alteração no sensor escolhido,
     determinando a ação pretendida, em caso de o sensor for “disparado”.*/

    public void onSensorChanged(SensorEvent event) {

        try {
            // Solicitando permissão ao usuário.
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(ClassGravador.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
            }

            /* Enquanto o sensor estiver ativado, o aúdio permanecerá sendo gravado, e o evento será 0, quando não for mais 0, ou seja, o sensor
            não estará mais ativado, para-se a gravação.*/
            if(event.values[0] == 0) {
                Recorder();
                Toast.makeText(getApplicationContext(),"Gravação iniciada",Toast.LENGTH_SHORT).show();
            }else {
                StopRecorder();
                telaReproduzirAudio();
            }

        }catch (Exception e){
            Log.d("","Erro ao tentar iniciar a gravação");
        }

    }

    private void StopRecorder() {
        gravador.stop();
        gravador.release();
        Toast.makeText(getApplicationContext(),"Gravação finalzada",Toast.LENGTH_SHORT).show();

    }


    public void Recorder() {
        if(gravador == null){
            pathFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gravavao.mp3";
            // Criando um novo objeto do tipo MediaRecorder.
            gravador = new MediaRecorder();
            gravador.setAudioSource(MediaRecorder.AudioSource.MIC); // Informando que o som será capiturado pelo Microfone.
            gravador.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Passando o formato da gravação, embora irá assumir a invformação do path, nesse caso mp3.
            gravador.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB); // Convertendo o aúdio.
            gravador.setOutputFile(pathFile); // Informado o caminho para a gravação do aúdio.

            try {
                gravador.prepare(); // Ao ser invocado, esse Método prepara a gravação
                gravador.start(); // Ao ser invoicado, esse Método inicia a gravação.
                Toast.makeText(getApplicationContext(),"Gravando...",Toast.LENGTH_SHORT).show();

            }catch (IOException e){
            }
        } else if(gravador != null){
            gravador.stop(); // Método que finaliza a gravação
            gravador.release(); // Liberando o sistema, assim evitando o gasto desnecessário dos recursos.
            gravador.reset();
        }
    }


    // Método que será invocado assim que o sensor for liberado, o mesmo irá invocar o layout da classe ClassPlaySensor
    private void telaReproduzirAudio() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),ClassPlaySensorMovimento.class);
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //No método onResume() precismos ligar a “escuta” do sensor,
    // passando o objeto listener e indicando qual o sensor e a velocidade de atualização do mesmo.
    @Override
    protected void onResume() {
        super.onResume();
        mSensorGravador.registerListener(this,mSensorGravador.
                        getDefaultSensor(Sensor.TYPE_PROXIMITY), // Tipo do sensor de Proximidade.
                SensorManager.SENSOR_DELAY_NORMAL); // Velocidade.
    }

    /*Para evitar o uso dos recursos desnecessariamente, caso o mesmo já não esteja mais em uso, devemos
    //desligar a “escuta”, poupando assim recursos e bateria do dispositivo, para isso, utilizamos o método onStop() */
    @Override
    protected void onStop() {
        mSensorGravador.registerListener(this,mSensorGravador.
                        getDefaultSensor(Sensor.TYPE_PROXIMITY), // Tipo do sensor de Proximidade.
                SensorManager.SENSOR_DELAY_NORMAL); // Velocidade.
        super.onStop();
    }


}



