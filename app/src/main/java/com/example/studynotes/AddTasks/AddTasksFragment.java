package com.example.studynotes.AddTasks;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.studynotes.DBHelper;
import com.example.studynotes.R;
import java.util.List;

public class AddTasksFragment extends Fragment {

    EditText taskName;
    EditText taskText;
    String savelesson;
    String textText;
    String nameText;
    String dateText;
    DBHelper databaseHelper;
    private Button datesetbutton;
    private Button savetaskbutton;

    public AddTasksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHelper = new DBHelper(requireContext());
        List<String> lessonlist = databaseHelper.getAllLessons();
        View dateview = inflater.inflate(R.layout.fragment_add_tasks, container, false);

        Spinner spinner = dateview.findViewById(R.id.lessons_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lessonlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                savelesson = item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        datesetbutton = dateview.findViewById(R.id.datesetbutton);
        datesetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        taskName = dateview.findViewById(R.id.tasksname);
        taskName.setOnKeyListener(new View.OnKeyListener() {
             public boolean onKey(View v, int keyCode, KeyEvent event)
             {
                 if(event.getAction() == KeyEvent.ACTION_DOWN &&
                         (keyCode == KeyEvent.KEYCODE_ENTER))
                 {
                     // сохраняем текст, введённый до нажатия Enter в переменную
                     nameText = taskName.getText().toString();
                     return true;
                 }
                 return false;
             }
         });

        taskText = dateview.findViewById(R.id.taskstext);
        taskText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // сохраняем текст, введённый до нажатия Enter в переменную
                    textText = taskText.getText().toString();
                    return true;
                }
                return false;
            }
        });

        savetaskbutton = dateview.findViewById(R.id.savetask);
        savetaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.saveTask(dateText, nameText, textText, savelesson);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_now);
            }
        });


        return dateview;
    }

    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateText = (String.valueOf(day)+ "."+String.valueOf(month+1)+ "."+String.valueOf(year)+ ".");
            }
        },2024, 0, 1);
        dialog.show();
    }
}