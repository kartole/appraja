package com.example.oshin.epraja;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class Login extends AppCompatActivity {


    CircularProgressButton circularProgressButton;
    Intent launchNextActivity;

    RelativeLayout rellay1, rellay2, rellay3;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout)findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout)findViewById(R.id.rellay2);



        handler.postDelayed(runnable, 2000);

        circularProgressButton = (CircularProgressButton)findViewById(R.id.btnLogin);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<String,String,String> demoDownload = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... voids) {
                        try {
                            Thread.sleep(3000);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")){
                            Toast.makeText(Login.this, "Login com Sucesso!", Toast.LENGTH_SHORT).show();
                            //circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                            //Intent intent = new Intent(Login.this, MainActivity.class);
                            //startActivity(intent);
                            launchNextActivity = new Intent(Login.this, MainActivity.class);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(launchNextActivity);

                        }
                    }
                };
                circularProgressButton.startAnimation();
                demoDownload.execute();
            }
        });

    }


}
