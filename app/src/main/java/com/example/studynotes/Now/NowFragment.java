package com.example.studynotes.Now;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.studynotes.DBHelper;
import com.example.studynotes.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class NowFragment extends Fragment {

    TextView taskName;
    TextView taskText;
    TextView taskDate;
    FloatingActionButton fab;
    List<String> lessonName;
    private RecyclerView lessonsRecyclerView;
    private LessonsAdapter lessonsAdapter;
    DBHelper databaseHelper;
    String lessonText;

    public NowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View nowview = inflater.inflate(R.layout.fragment_now, container, false);

        taskName = nowview.findViewById(R.id.tv_taskName);
        taskDate = nowview.findViewById(R.id.tv_taskDate);
        taskText = nowview.findViewById(R.id.tv_taskText);

        List<LessonData> lessonsList = new ArrayList<>();
        lessonsList.addAll(DBHelper.getInstance(requireContext()).getAllData());
        LessonsAdapter.OnLessonsClickListener ClickListener = new LessonsAdapter.OnLessonsClickListener() {
            @Override
            public void onLessonsClick(LessonData lessons, int position) {
                lessonText = lessons.getLessonname();
                databaseHelper = new DBHelper(requireContext());
                databaseHelper.getTasks(lessonText);
                lessonName = databaseHelper.getTasks(lessonText);
                if (lessonName.size() == 0){
                    taskText.setText("По данному предмету нет ни одной заметки");
                    taskName.setText("");
                    taskDate.setText("");
                }
                else {
                    taskName.setText(lessonName.get(0));
                    taskText.setText(lessonName.get(1));
                    taskDate.setText(lessonName.get(2));
                }
            }
        };

        lessonsRecyclerView = nowview.findViewById(R.id.nowRecyclerView);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lessonsAdapter = new LessonsAdapter(requireContext(), lessonsList, ClickListener);
        lessonsRecyclerView.setAdapter(lessonsAdapter);

        fab = nowview.findViewById(R.id.fab_navigation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_now_to_navigation_add_tasks);
            }
        });

        return nowview;
    }

}