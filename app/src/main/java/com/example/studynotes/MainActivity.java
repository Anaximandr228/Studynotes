package com.example.studynotes;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.studynotes.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    DBHelper databaseHelper;
    String JsonURLRasp;
    String JsonURLBase = "https://old.rsvpu.ru/contents/api/rasp.php?v_gru=";
    String JsonURLGroups = "https://old.rsvpu.ru/contents/api/groups.php";
    // This string will hold the results
    String datarasp = "";
    String datagroup = "";
    String selectgroup;
    public ArrayList<ArrayList<String>> groups_list;
    public ArrayList<String> lessons_list;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DBHelper(getApplicationContext());
        selectgroup = databaseHelper.getMygroup();
        JsonURLRasp = JsonURLBase + selectgroup;
        NukeSSLCerts.nuke();
        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest arraygroups = new JsonArrayRequest(JsonURLGroups,
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        groups_list = new ArrayList<ArrayList<String>>();
                        try {
                            // Retrieves first JSON object in outer array
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject groupObj = response.getJSONObject(i);
                                String name = groupObj.getString("name");
                                String oid = groupObj.getString("oid");
                                datagroup += "oid: " + oid + "name: " + name;
                                ArrayList<String> a1 = new ArrayList<String>();
                                a1.add(name);
                                a1.add(oid);
                                groups_list.add(a1);
//                                System.out.print(groups_list.get(i).get(0)+":"+groups_list.get(i).get(1));
                            }
                            Log.e("Данные", datagroup);
                        }

                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                        databaseHelper.addGroups(groups_list);
                    }
//
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(arraygroups);


        // Casts results into the TextView found within the main layout XML with id jsonData

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        if (selectgroup == null) {
            Toast.makeText(this, "Выберите группу", Toast.LENGTH_LONG);
        } else {
            JsonArrayRequest arrayrasp = new JsonArrayRequest(JsonURLRasp,
                    new Response.Listener<JSONArray>() {

                        // Takes the response from the JSON request
                        @Override
                        public void onResponse(JSONArray response) {
                            lessons_list = new ArrayList<String>();
                            try {
                                // Retrieves first JSON object in outer array
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject contentObj = response.getJSONObject(i);
                                    JSONObject content = contentObj.getJSONObject("content");
                                    String lecturer = content.getString("lecturer");
                                    String disciplina = content.getString("disciplina");
                                    datarasp += "lecturer: " + lecturer + "disciplina: " + disciplina;
                                    lessons_list.add(disciplina);

                                }
                                Log.e("Данные", datarasp);
                            }

                            // Try and catch are included to handle any errors due to JSON
                            catch (JSONException e) {
                                // If an error occurs, this prints the error to the log
                                e.printStackTrace();
                            }
                            databaseHelper.addLessons(lessons_list);
                        }
                    },
                    // The final parameter overrides the method onErrorResponse() and passes VolleyError
                    //as a parameter
                    new Response.ErrorListener() {
                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error");
                        }
                    }
            );
            requestQueue.add(arrayrasp);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_settings, R.id.navigation_now, R.id.navigation_start, R.id.navigation_add_tasks)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        if (selectgroup == null) {
            navController.navigate(R.id.navigation_start);
        } else {
            navController.navigate(R.id.navigation_now);
        }
    }
}
