package com.design.tonic.tonicjsontest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.design.tonic.tonicjsontest.model.People;
import com.design.tonic.tonicjsontest.model.Person;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.people_table);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // load json file
        new FileLoaderAsyncTask().execute(getString(R.string.the_filename));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Background task to load JSON.
     * Note: If loading JSON over the network, background task would be required for network calls.
     * Since example url is https protocol, the json file was downloaded as a file and stored
     * as an asset. I decided to keep the file parsing in background of AsyncTask anyway and
     * parse JSON and setup GridView on UI thread.
     */
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

        /**
         * Parse JSON and setup GridView
         * @param result - string containing raw JSON dataa
         */
        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            People people = gson.fromJson(result, People.class);
            List<Person> personList = people.getPeople();
            // create new list to easily work with an array adapter
            List<String> adapterList = new ArrayList<String>();
            // setup header
            adapterList.add(getString(R.string.first_name_label));
            adapterList.add(getString(R.string.last_name_label));
            // setup the data
            for (Person nxtPerson : personList) {
                adapterList.add(nxtPerson.getFirst_name());
                adapterList.add(nxtPerson.getLast_name());
            }
            gridView.setAdapter(new PeopleArrayAdapter(MainActivity.this, 0, adapterList));
            progressBar.setVisibility(View.GONE);
        }
    }

}

/**
 * ArrayAdapter to handle populating the GridView with parsed JSON data.
 * Creates a header with values set in first two entries of data list.
 * Uses a ViewHolder for ListView efficiency, typically required with more complicated
 * views within the ListView as well as longer data lists
 */
class PeopleArrayAdapter extends ArrayAdapter<String> {

    public PeopleArrayAdapter(Context context, int resource, List<String> adapterList) {
        super(context, resource, adapterList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            if (position < 2) {
                convertView = inflater.inflate(R.layout.table_header, null);
                viewHolder.nameField = (TextView) convertView.findViewById(R.id.header_field);
            } else {
                convertView = inflater.inflate(R.layout.table_view, null);
                viewHolder.nameField = (TextView) convertView.findViewById(R.id.name_field);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String field = getItem(position);
        viewHolder.nameField.setText(field);
        return convertView;
    }
}

class ViewHolder {
    TextView nameField;
}
