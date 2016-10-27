package net.androidbootcamp.finalapp;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class LetterGradeActivity extends AppCompatActivity {


    ArrayList<String> listFromDatabase = new ArrayList<>(); // store each name along with their grades
    ArrayList<String> finalGrades = new ArrayList<>();  // store each grade to be calculated in final overall grade

    String nameFromIndex;
    String name = "";
    String numberGrade = "";
    int totalGrade;
    String letterGrade;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_grade);

        myDB = new DatabaseHelper(this);

        Button goBack = (Button) findViewById(R.id.backMain_btn);
        Button viewAgain = (Button) findViewById(R.id.viewAgain_btn);

        Cursor res = myDB.getAllData();

        if (res.getCount() == 0) {  // if there is nothing in the database, show a message

            showMessage("Error", "Nothing found in Database");
            return;
        }

        while (res.moveToNext()) {  // go through database and add each name and grade to the listFromDatabase arrayList

            String ID = res.getString(0);
            //  add the name and number grade to listFromDatabase
            name = res.getString(1) + " " + res.getString(2);
            numberGrade = res.getString(5);
            listFromDatabase.add(name);
            listFromDatabase.add(numberGrade);
        }

        // get each name from listFromDatabase and add the name followed by all grades to finalGrades arrayList
        // the finalGrades arrayList will be used to calculate final overall grades for each student
        for (int i = 0; i < listFromDatabase.size(); i++) {

            String name = listFromDatabase.get(i);
            String grade = listFromDatabase.get(i + 1);
            finalGrades.add(name);
            finalGrades.add(grade);
            listFromDatabase.remove(0);
            listFromDatabase.remove(0);

            for (int i2 = 0; i2 < listFromDatabase.size(); i2++) {

                if (listFromDatabase.get(i2).equals(name)) {

                    finalGrades.add(listFromDatabase.get(i2 + 1));
                    listFromDatabase.remove(i2);
                    listFromDatabase.remove(i2);
                    i2--;
                }

            }
            if (listFromDatabase.size() != 0) {
                finalGrades.add(",");
            }

            i--;
        }

        StringBuffer buffer = new StringBuffer();

        nameFromIndex = finalGrades.get(0);
        buffer.append("Student: " + nameFromIndex + "\n");

        int gradeCount = 0;
        for (int i = 1; i < finalGrades.size(); i++)
        {
            if (!finalGrades.get(i).equals(","))    // if it's not a comma...
            {
                int parseInteger = Integer.parseInt(finalGrades.get(i));
                totalGrade = totalGrade + parseInteger;
                gradeCount++;

                if (i == finalGrades.size() - 1)
                {
                    totalGrade = totalGrade / gradeCount;
                    letterGrade = getGrade(totalGrade);
                    buffer.append("Final Grade: " + letterGrade + "\n\n");
                }
            }

            else    // we reached a comma
            {
                totalGrade = totalGrade / gradeCount;
                letterGrade = getGrade(totalGrade);
                buffer.append("Final Grade: " + letterGrade + "\n\n");
                i++;
                buffer.append("Student: " + finalGrades.get(i) + "\n");
                totalGrade = 0;
                gradeCount = 0;

            }
        }
        showMessage("Final Grades", buffer.toString());

        // click to go back to main screen
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LetterGradeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // click for the option to view grades again
        viewAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LetterGradeActivity.this, LetterGradeActivity.class);
                startActivity(intent);
            }
        });
    }

    // A function to display a message if nothing is in the database. Otherwise displays the title of the database
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    // A function that takes in a number grade and returns a letter grade based on the percentage
    public String getGrade(double numberGrade)
    {
        String Letter;
        if ((numberGrade >= 0) && (numberGrade < 60))
        {Letter = "F";}
        else if ((numberGrade >= 60) && (numberGrade < 70))
        {Letter = "D";}
        else if ((numberGrade >= 70) && (numberGrade < 80))
        {Letter = "C";}
        else if ((numberGrade >= 80) && (numberGrade < 90))
        {Letter = "B";}
        else if ((numberGrade >= 90) && (numberGrade < 101))
        {Letter = "A";}
        else
        {
            Letter = null;
        }

        return Letter;
    }
}
