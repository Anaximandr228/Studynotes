package com.example.studynotes.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.studynotes.DBHelper;
import com.example.studynotes.R;


public class SettingsFragment extends Fragment {

    Button deletedata;
    DBHelper databaseHelper;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View settingsview = inflater.inflate(R.layout.fragment_settings, container, false);

        deletedata = settingsview.findViewById(R.id.deletedata);
        deletedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper = new DBHelper(requireContext());
                databaseHelper.delAll();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_settings_to_navigation_start);

            }
        });
         return settingsview;
    }
}