package com.design.tonic.tonicjsontest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.design.tonic.tonicjsontest.model.People;
import com.design.tonic.tonicjsontest.model.Person;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.people_table);
        new FileLoaderAsyncTask().execute(getString(R.string.the_filename));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private class FileLoaderAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... filename) {
            String result = null;
            try {
                InputStream inputStream = getAssets().open(filename[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String nxtLine;
                StringBuilder stringBuilder = new StringBuilder();
                while ((nxtLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nxtLine);
                }
                result = stringBuilder.toString();
            } catch (IOException ioe) {

            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            People people = gson.fromJson(result, People.class);
            List<Person> personList = people.getPeople();
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.table_header,null);
            tableLayout.addView(view);
            for (Person nxtPerson : personList) {
                view = layoutInflater.inflate(R.layout.table_view, null);
                TextView firstname = (TextView) view.findViewById(R.id.first_name_field);
                firstname.setText(nxtPerson.getFirst_name());
                TextView lastname = (TextView) view.findViewById(R.id.last_name_field);
                lastname.setText(nxtPerson.getLast_name());
                tableLayout.addView(view);
            }
        }
    }
}

