package com.ubuphailin.lenovo.connection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    ImageButton btn_takepicture;
    ImageButton help;
    ImageButton logout;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_takepicture = (ImageButton) findViewById(R.id.btn_takepicture);
        help = (ImageButton) findViewById(R.id.btn_help);
        logout = (ImageButton) findViewById(R.id.btn_exit);

        session = new UserSessionManager(getApplicationContext());


        btn_takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);//เข้าไปหน้าเมนู
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();

            }
        });
    }


    }


