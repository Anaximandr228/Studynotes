package com.example.studynotes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.studynotes.Now.LessonData;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper noddDataHelper;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "study.db";
    private static final String TABLE_TASKS = "Tasks";
    private static final String TASKS_ID = "tasksid";
    private static final String TASKS_NAME = "tasksname";
    private static final String TASKS_TEXT = "taskstext";
    private static final String LESSONS_ID_KEY = "lessons_id_key";
    private static final String TASKS_DATE = "tasksdate";
    private static final String TABLE_GROUP = "Groups";
    private static final String GROUP_ID = "groupid";
    private static final String GROUP_NAME = "groupname";
    private static final String TABLE_MYGROUP = "Mygroups";
    private static final String MY_GROUP_KEY = "name";
    private static final String MY_GROUP_ID = "mygroupid";
    private static final String MY_GROUP_NAME = "mygroupname";
    private static final String TABLE_LESSONS = "Lessons";
    private static final String LESSONS_ID = "LESSONS_ID";
    private static final String LESSON_NAME = "LESSON_NAME";
    private static final String TEACHER_ID = "TEACHER_ID";
    String nameGroup;
    String idGroup;
    String idLesson;

    public static DBHelper getInstance(final Context context) {
        if (noddDataHelper == null) {
            noddDataHelper = new DBHelper(context);
        }
        return noddDataHelper;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LESSONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LESSONS + "("
                +LESSON_NAME +" TEXT UNIQUE,"
                + LESSONS_ID +"  INTEGER  NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
                + TEACHER_ID +" INTEGER"+")";
        String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + "("
                +GROUP_ID +" INTEGER UNIQUE,"
                + GROUP_NAME +" TEXT"+")";
        String CREATE_MYGROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MYGROUP + "("
                + MY_GROUP_KEY +" INTEGER,"
                + MY_GROUP_ID +" INTEGER,"
                + MY_GROUP_NAME +" TEXT"+")";
        String CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "("
                +TASKS_NAME +" TEXT,"
                +TASKS_TEXT +" TEXT,"
                + TASKS_ID +" INTEGER  NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
                + TASKS_DATE +" TEXT,"
                + LESSONS_ID_KEY +" INTEGER,"+" FOREIGN KEY ("+LESSONS_ID_KEY+") REFERENCES "
                +TABLE_LESSONS+"("+LESSONS_ID+") ON UPDATE CASCADE)";

        db.execSQL(CREATE_LESSONS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_MYGROUP_TABLE);

        String insertQuery = "INSERT INTO "+TABLE_MYGROUP+" (name) VALUES ('group')";
        db.execSQL(insertQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        onCreate(db);
    }

    public List<LessonData> getAllData() {
        List<LessonData> subjectList = new ArrayList<LessonData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LESSONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LessonData lesson = new LessonData();
                lesson.setLessonname(cursor.getString(cursor.getColumnIndex(LESSON_NAME)));
                subjectList.add(lesson);
            } while (cursor.moveToNext());
        }

        // return contact list
        return subjectList;
    }
    public void delAll() {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYGROUP);
        onCreate(db);
    }

    public List<String> getAllGroups() {
        List<String> groupsList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT "+ GROUP_NAME+" FROM " + TABLE_GROUP;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                groupsList.add(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
            } while (cursor.moveToNext());
        }
        // return contact list
        return groupsList;
    }

    public void addGroups(ArrayList<ArrayList<String>> groups_list) {
        final SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < groups_list.size(); i++){
            String insertQuery = "INSERT INTO "+TABLE_GROUP+" (groupname,groupid) VALUES ('"+groups_list.get(i).get(0)+"',"+groups_list.get(i).get(1)+")";
            try {
                db.execSQL(insertQuery);
                // пишем обработку исключения при закрытии потока чтения
            } catch (SQLException e) {
//                String updateQuery = "INSERT INTO "+TABLE_GROUP+" (groupname,groupid) VALUES ('"+groups_list.get(i).get(0)+"',"+groups_list.get(i).get(1)+")";
//                db.execSQL(updateQuery);
                System.out.println(e.getMessage());
            }
        }
    }

    public void saveGroup(String item) {
        final SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+ GROUP_ID +" FROM "+ TABLE_GROUP +" WHERE "+ GROUP_NAME +" = '"+ item +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                nameGroup = cursor.getString(cursor.getColumnIndex(GROUP_ID));
            } while (cursor.moveToNext());
        }

        String selectQuery1 = "UPDATE "+TABLE_MYGROUP+" SET mygroupid = '"+nameGroup+"', mygroupname = '"+item+"', name = 'group' WHERE name = 'group'";
        db.execSQL(selectQuery1);
    }

    public String getMygroup() {
        final SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+ MY_GROUP_ID +" FROM "+ TABLE_MYGROUP +" WHERE name = 'group'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                idGroup = cursor.getString(cursor.getColumnIndex(MY_GROUP_ID));
            } while (cursor.moveToNext());
        }
        return idGroup;
    }

    public void addLessons(ArrayList<String> lessons_list) {
        final SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < lessons_list.size(); i++){
            String insertQuery = "INSERT INTO "+TABLE_LESSONS+" (LESSON_NAME) VALUES ('"+lessons_list.get(i)+"')";
            try {
                db.execSQL(insertQuery);
                // пишем обработку исключения при закрытии потока чтения
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<String> getAllLessons() {
        List<String> lessonList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT "+ LESSON_NAME+" FROM " + TABLE_LESSONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lessonList.add(cursor.getString(cursor.getColumnIndex(LESSON_NAME)));
            } while (cursor.moveToNext());
        }

        // return contact list
        return lessonList;
    }

    public void saveTask(String date_text, String tasks_name, String tasks_text, String lesson_name) {
        final SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+ LESSONS_ID +" FROM "+ TABLE_LESSONS +" WHERE "+ LESSON_NAME +" = '"+ lesson_name +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                idLesson = cursor.getString(cursor.getColumnIndex(LESSONS_ID));
            } while (cursor.moveToNext());
        }
        String insertQuery = "INSERT INTO "+TABLE_TASKS+" (tasksdate, tasksname, taskstext, lessons_id_key) VALUES ('"+date_text+"','"+tasks_name +"', '"+tasks_text+"', '"+idLesson+"')";
        try {
            db.execSQL(insertQuery);
            // пишем обработку исключения при закрытии потока чтения
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<String> getTasks(String lesson_name) {
        final SQLiteDatabase db = this.getWritableDatabase();
        List<String> tasksList = new ArrayList<String>();
        String selectQuery = "SELECT Tasks.tasksname, Tasks.taskstext, Tasks.tasksdate FROM Lessons JOIN Tasks ON Lessons.LESSONS_ID = Tasks.lessons_id_key WHERE Lessons.LESSON_NAME = '"+lesson_name+"' ORDER BY tasksdate DESC LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

//         looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tasksList.add(cursor.getString(cursor.getColumnIndex(TASKS_NAME)));
                tasksList.add(cursor.getString(cursor.getColumnIndex(TASKS_TEXT)));
                tasksList.add(cursor.getString(cursor.getColumnIndex(TASKS_DATE)));
            } while (cursor.moveToNext());
        }
        // return contact list
        return tasksList;
    }

}