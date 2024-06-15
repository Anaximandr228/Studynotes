package com.example.studynotes.Now;


import android.os.Parcel;
import android.os.Parcelable;

public class LessonData implements Parcelable {
    private String lessonname;

    public String getLessonname() {
        return lessonname;
    }

    public void setLessonname(String lessonname) {
        this.lessonname = lessonname;
    }

    public LessonData() {

    }
    public LessonData(Parcel in) {
        lessonname = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lessonname);

    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<LessonData> CREATOR = new Creator<LessonData>() {
        @Override
        public LessonData createFromParcel(Parcel in) {
            return new LessonData(in);
        }

        @Override
        public LessonData[] newArray(int size) {
            return new LessonData[size];
        }
    };
}
