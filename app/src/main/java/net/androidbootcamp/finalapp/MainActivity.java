package net.androidbootcamp.finalapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // edit texts and buttons
    EditText student_id;
    EditText first_name;
    EditText last_name;
    EditText class_id;
    EditText class_name;
    Button add_student;
    Button view_Database;
    Button view_finals;

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        student_id = (EditText) findViewById(R.id.edit_Student_ID);
        first_name = (EditText) findViewById(R.id.edit_First_Name);
        last_name = (EditText) findViewById(R.id.edit_Last_Name);
        class_id = (EditText) findViewById(R.id.edit_Class_ID);
        class_name = (EditText) findViewById(R.id.edit_Class_Name);

        add_student = (Button) findViewById(R.id.Continue_btn);
        view_Database = (Button) findViewById(R.id.view_database_btn);
        view_finals = (Button) findViewById(R.id.final_grades_btn);

        // make sure nothing is showing in the fields each time going back to this activity
        student_id.setText(null);
        first_name.setText(null);
        last_name.setText(null);
        class_id.setText(null);
        class_name.setText(null);

        // when the continue button is pressed, it will go to the next screen for grade entry, unless any fields are missing
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set first name, lastname and id to uppercase
                String checkID = student_id.getText().toString();
                String checkFirst = first_name.getText().toString();
                String checkLast = last_name.getText().toString();

                final String ID_Uppercase = checkID.substring(0, 1).toUpperCase() + checkID.substring(1);
                final String firstName_Uppercase = checkFirst.substring(0, 1).toUpperCase() + checkFirst.substring(1);
                final String lastName_Uppercase = checkLast.substring(0, 1).toUpperCase() + checkLast.substring(1);

                // if all fields are not filled in, then a message will appear prompting user to enter in all fields
                if ((student_id.getText().toString().isEmpty()) || (first_name.getText().toString().isEmpty())
                        || (last_name.getText().toString().isEmpty()) || (class_id.getText().toString().isEmpty()) || class_name.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "error, no fields can be left empty! Please fill in each field above!", Toast.LENGTH_LONG).show();
                }

                else    // all fields are filled in, pass all data in fields to the next screen for Grade entry
                {

                    Intent intent = new Intent(MainActivity.this, GradeEntryActivity.class);
                    intent.putExtra("theID", ID_Uppercase);
                    intent.putExtra("theFirst", firstName_Uppercase);
                    intent.putExtra("theLast", lastName_Uppercase);
                    intent.putExtra("theClassID", class_id.getText().toString());
                    intent.putExtra("theClassName", class_name.getText().toString());
                    startActivity(intent);
                }
            }
        });

        // click to view all data in the database
        view_Database.setOnClickListener(new View.OnClickListener() {
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

                showMessage("Students", buffer.toString());
            }
        });

        // click to view each students overall grade
        view_finals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LetterGradeActivity.class);
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
}
