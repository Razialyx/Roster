package com.jonesclass.singh.roster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class NewMemberActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    Switch activeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        firstNameEditText = findViewById(R.id.editText_firstName);
        lastNameEditText = findViewById(R.id.editText_lastName);
        activeSwitch = findViewById(R.id.switch_active);

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameEditText.getText().toString().length() > 0 &&
                        lastNameEditText.getText().toString().length() > 0) {
                    Member newMember = new Member(-1, firstNameEditText.getText().toString(),
                            lastNameEditText.getText().toString(), activeSwitch.isChecked());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("com.jonesclass.singh.NEW_MEMBER", newMember);
                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();

                } else {
                    Toast.makeText(NewMemberActivity.this,
                            "Please enter a first and last name.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}