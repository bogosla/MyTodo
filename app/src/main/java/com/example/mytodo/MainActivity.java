package com.example.mytodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import  org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int GO_EDIT = 11;
    private List<String> tasks = new ArrayList<>();
    private EditText taskInput;
    private TaskViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();

        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_task);
        adapter = new TaskViewAdapter(tasks);

        //set longClick on item
        adapter.setLongClickListener(new TaskViewAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //remove task from list
                tasks.remove(position);
                adapter.notifyDataSetChanged();
                saveItems();
            }
        });

        //set click to Goto page edit
        adapter.setClickListener(new TaskViewAdapter.OnClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent i = new Intent(getApplicationContext(), EditActivity.class);
                i.putExtra("position", String.valueOf(position));
                i.putExtra("initial", ((TextView)view).getText() );
                startActivityForResult(i, GO_EDIT);
            }
        });

        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(this));

        taskInput = (EditText)findViewById(R.id.task_input);

        ((Button)findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = taskInput.getText().toString();

                //Add task to list
                tasks.add(text);

                //Notify adapter
                adapter.notifyItemInserted(tasks.size() - 1);

                //Clear input text
                taskInput.setText("");

                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GO_EDIT) {
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                String text_edited = data.getStringExtra("text_edited");
                if(text_edited != null && text_edited.trim() != "") {
                    tasks.set(position, text_edited);
                    adapter.notifyItemChanged(position);
                    saveItems();
                }
            }
        }
    }

    //Get file
    File getFile() {
        return (new File(getFilesDir(), "/tasks.txt"));
    }

    //Load tasks in DB
    void loadItems() {
        try {
            tasks = FileUtils.readLines(getFile(), Charset.defaultCharset());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Save in DB
    void saveItems(){
        try {
            FileUtils.writeLines(getFile(), tasks);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}