package com.brainque.cookry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brainque.util.API;
import com.brainque.util.Constant;
import com.brainque.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WelcomeActivity extends AppCompatActivity {
   Button btn_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btn_get=findViewById(R.id.btn_get);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(intent);

            }
        });

    }
}