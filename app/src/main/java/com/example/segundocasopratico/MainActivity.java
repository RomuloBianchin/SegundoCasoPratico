package com.example.segundocasopratico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurando a cor do fundo da barra do actionBar.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_app);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#222222")));

    }

    // Método responsável pela criação do menu da actionBar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // Com essa linha de código trazemos os itens para a actionBar.
        return true;
    }

    // Método que será utilizado para reagir de acordo com cada item selecionado da actionBar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId(); // Criando um obejto do tipo inteiro para que possamos apanhar os itens pelo seu id.

        // Quando o icone GRAVADOR da actionBar for pressionado, uma ação será disparada, nesse caso, iremos invocar o layout que desejamos que seja exíbido
        if (id == R.id.microfone) {
            // Abaixo estamos invocando o layout pretendito, para isso, utilizamos um Intent.
            Intent intent = new Intent(this, ClassGravador.class); // Criando um objeto do tipo Intent, e passando como contexto, a classe pretentida.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        // Quando o icone PÚBLICO da actionBar for pressionado, uma ação será disparada, nesse caso, iremos invocar o layout que desejamos que seja exíbido
        if (id == R.id.publico_logo) {
            // Abaixo estamos invocando o layout pretendito, para isso, utilizamos um Intent.
            Intent intent = new Intent(this, ClassPublicoPt.class); // Criando um objeto do tipo Intent, e passando como contexto, a classe pretentida.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        // Quando o icone TSF da actionBar for pressionado, uma ação será disparada, nesse caso, iremos invocar o layout que desejamos que seja exíbido
        if (id == R.id.tsf) {
            // Abaixo estamos invocando o layout pretendito, para isso, utilizamos um Intent.
            Intent intent = new Intent(this, ClassTsf.class); // Criando um objeto do tipo Intent, e passando como contexto, a classe pretentida.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        // Quando o icone Correio Da Manhã, da actionBar for pressionado, uma ação será disparada, nesse caso, iremos invocar o layout que desejamos que seja exíbido
        if (id == R.id.cm_tv) {
            // Abaixo estamos invocando o layout pretendito, para isso, utilizamos um Intent.
            Intent intent = new Intent(this, ClassCorreioDaManha.class); // Criando um objeto do tipo Intent, e passando como contexto, a classe pretentida.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
