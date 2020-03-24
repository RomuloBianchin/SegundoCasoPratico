package com.example.segundocasopratico;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ClassCorreioDaManha extends AppCompatActivity {

    // Declrando as váriavies.
    ListView meuLeitordeFeeds; // Obejto do tipo ListView, o mesmo irá receber a lista de noticías - Rss
    ArrayList<String> titles;  // Variável do tipo ArrayList de String, o mesmo irá armazenar a informação da tag <title> do XML  da página de Feeds.
    ArrayList<String> links;  // Variável do tipo ArrayList de String, o mesmo irá armazenar a informação da tag <links> do XML da página de Feeds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_correio_da_manha);

        // Adicionando icone em todas as actionBars.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_app);

        meuLeitordeFeeds = (ListView) findViewById(R.id.listaDeFeeds); // Instanciando o objeto do tipo ListView e associando o mesmo com o seu correspondente layout.

        // Instanciando as variáveis do tipo ArrayList
        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        // Esse método será responsável pela ação que irá ocorrer ao clicarmos em um item da lista dos feeds.
        meuLeitordeFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        new ClassCorreioDaManha.ProcessInBackground().execute();

    }

    // Método que irá obter o caminho e realizar a conexão, para isso iremos passar como parâmetro um objeto do tipo URL
    public InputStream getInputStream(URL url)
    {
        try{
            return  url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            Log.d("Erro ao conectar", "Web Site não é válido"); /* Utilizamos o método Log.d para exibir no logCat uma
                                                                    mensagem de alerta caso o link tenha algum problema, e não seja possível conectar*/
            return null;
        }
    }




    // Classe que irá realizar em background o processamento e análise do URL, XML e suas Tags, já que não é permitido realizar esse tipo de ação na Mainthread.
    public class ProcessInBackground extends AsyncTask<Integer, Void, String>
    {
        //
        ProgressDialog progressDialog = new ProgressDialog(ClassCorreioDaManha.this);
        Exception exception = null;

        /* Método obrigatório, utilizei o mesmo  para inserir uma mensagem de alerta ao usuário, enquanto o sistema faz a devida conexão e análise
        dos dados, assim damos um feedback de que a solicitação está sendo executada, pois em alguns casos pode demorar um pouco mais por conta da conexão.
        */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Carregando..."); // Mensagem para o usuário, caso o tempo de processamento demore.
            progressDialog.show(); // Método para exibir a mensagem do processDialog.

        }

        // Método que irá correr por trás do sistema operacional, realizando as devidas tarefas pretendidas.
        // Como análise do arquivo XML e suas tags.
        @Override
        protected String doInBackground(Integer... integers) {
            try{
                URL url = new URL("https://www.cmjornal.pt/rss"); // Passando o caminho do Feed.
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance(); /* Criando uma nova Instancia do XmlPullFactory para utiliza-lo com o Objeto
                                                                                                 xmlPullParser, que será utilizado para recuperar informações do arquivo XML.*/

                xmlPullParserFactory.setNamespaceAware(false); // Especificando que não iremos querer suporte para XML name com espaços.

                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser(); // Instanciando o novo objeto XmlPullParser, esse objeto terá todas as características para extrarir os dados necessários do XML
                xmlPullParser.setInput(getInputStream(url), "ISO-8859-1"); // Informando que o encoding do XML es UTF-8.

                boolean xmlItem = false; /* Será utilizado para informar se já estamos ou não dentro da tag <item> do arquivo XML, por isso foi iniciado com false, pois iniciaremos fora, e
                                         dessa forma ser+a possível saber quando já será possível de extrair as informações das tags colocadas dentro da tag <item>*/

                int eventType = xmlPullParser.getEventType(); // Nesse evento conseguimos retornar o evento atual, se está no início<> da tag ou no fim </>


                // Criando um looping while dentro do eventType, para percorrer as tags do arquivo XML,
                // sendo assim, conseguindo localizar as tags que precisamos recuperar os dados.

                // Com o eventType , passando o xmlPullParser, e as constantes END_DOCUMENT que corresponde a uma </> clossing tag,
                // e a constante START_TAG, que corresponde a uma open tag <>, conseguimos análisar o arquivo XML da página por completo.

                // Após


                while (eventType != XmlPullParser.END_DOCUMENT) { // Verificando quando for uma clossing tag, e caso seja, pulamos para o if, para realizar as demais vereificações.
                    if (eventType == XmlPullParser.START_TAG) // Entrando no If e verifncado a tag inicial, assim conseguiremos pegar os dados das tags (item,title e link).

                    {
                        if (xmlPullParser.getName().equalsIgnoreCase("item")) // Ignorando se temos ou não caracteres maiúsculos, e verificando se a open tag é item.
                        {
                            xmlItem = true;
                        }
                        else if (xmlPullParser.getName().equalsIgnoreCase("title")) // Ignorando se temos ou não caracteres maiúsculos, e verificando se a open tag é title.
                        {
                            if (xmlItem)
                            {
                                titles.add(xmlPullParser.nextText()); // Passando para a próxima tag.
                            }
                        }
                        else if (xmlPullParser.getName().equalsIgnoreCase("link")) // Ignorando se temos ou não caracteres maiúsculos, e verificando se a open tag é link.
                        {
                            if (xmlItem)
                            {
                                links.add(xmlPullParser.nextText()); // Passando para a próxima tag.
                            }
                        }
                    }
                    else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item"))
                    {
                        xmlItem = false;
                    }
                    eventType = xmlPullParser.next(); // Passando para a próxima tag.
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        // Método que será executado após os dois métodos anteriores, o mesmo irá retornar em um objeto listView, as tags que foram econtradas item, link e title.
        // item = corresponde ao item, nesse XML análsado, o mesmo contem as child tag title, description, link, etc...
        // title = corresponde ao nome da notícia.
        // link = caminho da notícia.
        //

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ClassCorreioDaManha.this,R.layout.custom_layout_list_view, titles);
            meuLeitordeFeeds.setAdapter(adapter);

            progressDialog.dismiss();

        }
    }







}
