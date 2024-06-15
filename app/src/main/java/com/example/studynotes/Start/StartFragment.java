package com.example.studynotes.Start;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studynotes.DBHelper;
import com.example.studynotes.R;

import java.util.List;


public class StartFragment extends Fragment {
    String savegroup;
    DBHelper databaseHelper;
    Button datesetbutton;
    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHelper = new DBHelper(requireContext());
        List<String> grouplist = databaseHelper.getAllGroups();
        View startview = inflater.inflate(R.layout.fragment_start, container, false);
        // Inflate the layout for this fragment
        Spinner spinner = startview.findViewById(R.id.groups_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grouplist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                savegroup = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        datesetbutton = startview.findViewById(R.id.datesetbutton);
        datesetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.saveGroup(savegroup);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_now);

                Toast.makeText(requireContext(), "Перезагрузите приложение для обновления данных",
                        Toast.LENGTH_LONG).show();
            }
        });

        return startview;
    }
}