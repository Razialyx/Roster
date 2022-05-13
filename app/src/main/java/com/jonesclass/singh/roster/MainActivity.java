package com.jonesclass.singh.roster;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityTAG";
    private List<Member> memberList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView_members);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        setAdapter();

        Button addMember = findViewById(R.id.button_addMember);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getResultsIntent = new Intent(getApplicationContext(), NewMemberActivity.class);
                activityResultLaunch.launch(getResultsIntent);
            }
        });

        Button clearRosterButton = findViewById(R.id.button_clearRoster);
        clearRosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Data Deletion Warning!");
                alertDialog.setMessage("Delete All Members?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        databaseHelper.removeAll();
                        memberList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Success: " + databaseHelper.getMembers().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapter();
    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Member newMember = (Member) result.getData().getSerializableExtra("com.jonesclass.singh.NEW_MEMBER");
                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                        if (databaseHelper.addMember(newMember)) {
                            memberList.add(newMember);
                            adapter.notifyItemInserted(memberList.size());
                            Toast.makeText(MainActivity.this, "Success: " + databaseHelper.getMembers().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED){
                        Log.d("MainActivityTag", "it canceled");
                    }

                }
            });

    public void setAdapter() {
        memberList = databaseHelper.getMembers();
        adapter = new RecyclerAdapter(getApplicationContext(), memberList, databaseHelper);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}