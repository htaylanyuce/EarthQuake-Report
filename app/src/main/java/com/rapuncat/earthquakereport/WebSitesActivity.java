package com.rapuncat.earthquakereport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by taylan on 24.11.2017.
 */

public class WebSitesActivity extends AppCompatActivity {

    private Bundle b;
    private WebView wv1;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website_activity);

        b = getIntent().getExtras();
        url = b.getString("url");
        getSupportActionBar().setTitle(b.getString("title"));

        Log.d("URL: ", url);

        wv1 = (WebView) findViewById(R.id.webView);


        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);
    }

}



