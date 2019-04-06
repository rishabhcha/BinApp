package com.example.rishabhchandel.agriapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1, button2, button3;
    private boolean isButton1Clicked, isButton2Clicked, isButton3Clicked;
    private WebView webView;
    private LinearLayout ll2;
    private TextView tv1, tv2, tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        ll2 = findViewById(R.id.ll2);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        showBinIFrame();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button1:
                showBinIFrame();
                break;
            case R.id.button2:
                showAirQualityIFrame();
                break;
            case R.id.button3:
                showWasteStatus();
                break;
            default:
                break;
        }

    }

    private void showBinIFrame() {
        if (!isButton1Clicked) {

            webView.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);

            String html = "<iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/channels/750300/charts/2?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15\"></iframe>";
            webView.loadData(html, "text/html", null);
            isButton1Clicked = true;
            isButton2Clicked = false;
            isButton3Clicked = false;
        }
    }

    private void showAirQualityIFrame() {
        if (!isButton2Clicked) {

            webView.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);

            String html = "<iframe width=\"450\" height=\"260\" style=\"border: 1px solid #cccccc;\" src=\"https://thingspeak.com/channels/750300/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15\"></iframe>";
            webView.loadData(html, "text/html", null);
            isButton2Clicked = true;
            isButton1Clicked = false;
            isButton3Clicked = false;
        }
    }

    private void showWasteStatus() {
        if (!isButton3Clicked) {

            webView.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);

            fetchValuesFromAPI();

            isButton3Clicked = true;
            isButton1Clicked = false;
            isButton2Clicked = false;
        }
    }

    private void fetchValuesFromAPI() {

        String url1 = "https://api.thingspeak.com/channels/750301/fields/1.json?results=1";
        String url2 = "https://api.thingspeak.com/channels/750314/fields/1.json?results=1";
        String url3 = "https://api.thingspeak.com/channels/750316/fields/1.json?results=1";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tv1.setText(response.getJSONArray("feeds").getJSONObject(0).getString("field1"));
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong!! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tv2.setText(response.getJSONArray("feeds").getJSONObject(0).getString("field1"));
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong!! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest
                (Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tv3.setText(response.getJSONArray("feeds").getJSONObject(0).getString("field1"));
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong!! Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest1);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest2);
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest3);

    }
}
