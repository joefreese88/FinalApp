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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GradeEntryActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    EditText grade;
    TextView headingText;

    String studentID_string;
    String firstName_string;
    String lastName_string;
    String classID_string;
    String className_string;
    int classGrade;
    String letterGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_entry);

        myDB = new DatabaseHelper(this);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        studentID_string = extras.getString("theID");
        firstName_string = extras.getString("theFirst");
        lastName_string = extras.getString("theLast");
        classID_string = extras.getString("theClassID");
        className_string = extras.getString("theClassName");

        // set first name, last name, and class name to uppercase
        final String firstName_Uppercase = firstName_string.substring(0, 1).toUpperCase() + firstName_string.substring(1);
        final String lastName_Uppercase = lastName_string.substring(0, 1).toUpperCase() + lastName_string.substring(1);
        final String class_Uppercase = className_string.substring(0, 1).toUpperCase() + className_string.substring(1);

        grade = (EditText) findViewById(R.id.edit_grade);
        Button submit = (Button) findViewById(R.id.submit_btn);
        Button viewDatabase = (Button) findViewById(R.id.view_btn);
        headingText = (TextView) findViewById(R.id.enter_grade_txt);

        headingText.setText("Enter the grade for "+firstName_Uppercase+" "+lastName_Uppercase+"'s "+className_string+" class then click 'SUBMIT GRADE'!");

        // all fields have data entered so they will be put into the database
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GradeEntryActivity.this, MainActivity.class);

                int convertGrade = Integer.parseInt(grade.getText().toString());
                letterGrade = getGrade(convertGrade); // send to getGrade method to return a letter grade

                // check if grade input is valid other wise continue adding to database
                if ((letterGrade.equals(null)) || (grade.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "invalid input. Enter a value between 0 and 100 for the Grade!", Toast.LENGTH_LONG).show();
                } else {
                    classGrade = Integer.parseInt(grade.getText().toString());
                    boolean isInserted = myDB.insertData(studentID_string, firstName_Uppercase, lastName_Uppercase, classID_string, class_Uppercase, classGrade, letterGrade);

                    if (isInserted == true)
                        Toast.makeText(GradeEntryActivity.this, "Data Added", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(GradeEntryActivity.this, "Data Was Not Added", Toast.LENGTH_LONG).show();

                    startActivity(intent);  // return to main activity
                }
            }
        });

        // click to view the database
        viewDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDB.getAllData();

                if (res.getCount() == 0){
                    // show message
                    showMessage("Error", "Nothing found in Database");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){

                        buffer.append("ID:  " + res.getString(0) + "\n");
                        buffer.append("First Name:  " + res.getString(1) + "\n");
                        buffer.append("Last Name:  " + res.getString(2) + "\n");
                        buffer.append("class ID:  " + res.getString(3) + "\n");
                        buffer.append("class Name:  " + res.getString(4) + "\n");
                        buffer.append("class grade:  " + res.getString(5) + "\n");
                        buffer.append("class Letter:    " + res.getString(6) + "\n\n");
                }

                showMessage("students", buffer.toString());

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

    // a function that takes in a number and returns a letter grade based on the percentage
    public String getGrade(int numberGrade)
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
