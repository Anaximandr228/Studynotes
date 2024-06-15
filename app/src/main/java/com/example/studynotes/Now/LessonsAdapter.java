package com.example.studynotes.Now;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studynotes.R;
import java.util.List;

public class LessonsAdapter extends  RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder>{
    interface OnLessonsClickListener{
        void onLessonsClick(LessonData lessons, int position);
    }
    private final OnLessonsClickListener onClickListener;
    private Context context;
    private List<LessonData> lessonsList;

    LessonsAdapter(Context context, List<LessonData> lessonsList, OnLessonsClickListener onClickListener){
        this.onClickListener = onClickListener;
        this.context = context;
        this.lessonsList = lessonsList;
    }

    @NonNull
    @Override
    public LessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootview = inflater.inflate(R.layout.lessonsitem, parent, false);
        return new LessonsViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsViewHolder holder, int position) {
        LessonData data = lessonsList.get(position);
        holder.lessonname.setText(String.valueOf((data.getLessonname())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onLessonsClick(data, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    public class LessonsViewHolder extends RecyclerView.ViewHolder{
        TextView lessonname;

        public LessonsViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonname = itemView.findViewById(R.id.lessonName);
        }
    }
}