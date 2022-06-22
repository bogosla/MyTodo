package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    TextView text;
    EditText edit_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        text = (TextView) findViewById(R.id.textView);
        edit_input = (EditText) findViewById(R.id.edit_input);
        String pos = getIntent().getStringExtra("position");
        //get task pos editing
        text.setText("Edit task number # " + pos);
        edit_input.setText(getIntent().getStringExtra("initial"));


        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent r = new Intent();
                r.putExtra("text_edited", edit_input.getText().toString());
                r.putExtra("position", Integer.valueOf(pos));
                setResult(RESULT_OK, r);
                finish();
            }
        });


    }
}