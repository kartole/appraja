package com.example.oshin.epraja;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TermsAndConditions extends AppCompatActivity {

    WebView webHtmlCss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);



        webHtmlCss = (WebView)findViewById(R.id.wbvTeC);
        WebSettings ws = webHtmlCss.getSettings();
        ws.setJavaScriptEnabled(true);

        webHtmlCss.loadUrl("file:///android_asset/tec.html");

    }

}
