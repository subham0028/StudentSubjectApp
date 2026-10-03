package com.example.studentsubjectapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Spinner semesterSpinner;
    ListView subjectListView;
    EditText searchSubject;

    String[] semesters = {
            "All",
            "Semester 1",
            "Semester 2",
            "Semester 3",
            "Semester 4",
            "Semester 5"
    };

    String[][] subjects = {
            {"Mathematics", "Programming", "Physics", "English"},
            {"Data Structure", "Database", "Web Development", "Computer Network"},
            {"Java", "Operating System", "Software Engineering", "Computer Graphics"},
            {"Python", "Artificial Intelligence", "Cyber Security", "Mobile Application"},
            {"Business Analytics", "Software Testing", "Information Security", "Computer Hardware"}
    };

    String[][] descriptions = {
            {
                    "Basic mathematics and problem solving.",
                    "Introduction to programming concepts.",
                    "Basic concepts of physics.",
                    "English communication and language."
            },
            {
                    "Study of data structures and algorithms.",
                    "Introduction to database management.",
                    "Basic web development concepts.",
                    "Fundamentals of computer networking."
            },
            {
                    "Java programming and object-oriented concepts.",
                    "Study of operating systems and their functions.",
                    "Software development and engineering concepts.",
                    "Introduction to computer graphics."
            },
            {
                    "Python programming and applications.",
                    "Introduction to Artificial Intelligence.",
                    "Fundamentals of Cyber Security.",
                    "Android mobile application development."
            },
            {
                    "Analysis of business data and decision making.",
                    "Software testing methods and techniques.",
                    "Information security concepts.",
                    "Computer hardware and system maintenance."
            }
    };

    ArrayAdapter<String> subjectAdapter;
    ArrayList<String> currentSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        semesterSpinner = findViewById(R.id.semesterSpinner);
        subjectListView = findViewById(R.id.subjectListView);
        searchSubject = findViewById(R.id.searchSubject);

        // Spinner Adapter
        ArrayAdapter<String> semesterAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        semesters
                );

        semesterAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        semesterSpinner.setAdapter(semesterAdapter);

        // Show all subjects initially
        showAllSubjects();

        // Spinner Event
        semesterSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        if (position == 0) {
                            showAllSubjects();
                        } else {
                            showSubjects(position - 1);
                        }

                        searchSubject.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        // Search Event
        searchSubject.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                if (subjectAdapter != null) {
                    subjectAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // ListView Event
        subjectListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    String selectedSubject =
                            subjectAdapter.getItem(position);

                    if (selectedSubject != null) {
                        showSubjectDetails(selectedSubject);
                    }
                }
        );
    }

    // Show subjects of selected semester
    private void showSubjects(int semesterPosition) {

        currentSubjects = new ArrayList<>(
                Arrays.asList(subjects[semesterPosition])
        );

        subjectAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        currentSubjects
                );

        subjectListView.setAdapter(subjectAdapter);
    }

    // Show all subjects
    private void showAllSubjects() {

        currentSubjects = new ArrayList<>();

        for (String[] semesterSubjects : subjects) {
            currentSubjects.addAll(
                    Arrays.asList(semesterSubjects)
            );
        }

        subjectAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        currentSubjects
                );

        subjectListView.setAdapter(subjectAdapter);
    }

    // Subject Details
    private void showSubjectDetails(String subject) {

        int semesterPosition =
                semesterSpinner.getSelectedItemPosition();

        // All selected
        if (semesterPosition == 0) {

            for (int i = 0; i < subjects.length; i++) {

                for (int j = 0; j < subjects[i].length; j++) {

                    if (subjects[i][j].equals(subject)) {

                        showDetails(
                                subject,
                                i,
                                j
                        );

                        return;
                    }
                }
            }

        } else {

            int actualSemester = semesterPosition - 1;

            for (int i = 0;
                 i < subjects[actualSemester].length;
                 i++) {

                if (subjects[actualSemester][i].equals(subject)) {

                    showDetails(
                            subject,
                            actualSemester,
                            i
                    );

                    return;
                }
            }
        }
    }

    // Display details dialog
    private void showDetails(
            String subject,
            int semesterPosition,
            int subjectPosition) {

        String description =
                descriptions[semesterPosition][subjectPosition];

        new AlertDialog.Builder(this)
                .setTitle("Subject Details")
                .setMessage(
                        "Subject: " + subject +
                                "\n\nSemester: " +
                                semesters[semesterPosition + 1] +
                                "\n\nDescription:\n" +
                                description
                )
                .setPositiveButton("OK", null)
                .show();
    }
}