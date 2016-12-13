package com.serious.yolostripe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Card card = new Card("4242424242424242", 9, 2017, "657");

        Stripe stripe = null;
        try {
            stripe = new Stripe("pk_test_Z7Jygw96IdsAouhSoJbMTDsG");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        if (!card.validateCard()) {
            Log.d("", "CARD PAS OK");
        }

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        sendRequest(token);

                        Log.d("", "STRIPE OK");

                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Log.d("", "STRIPE PAS OK");
                        Log.e("MYAPP", "exception", error);
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void sendRequest(Token token) {
        Log.d("", "DEBUT REQUEST");
        final String TOKEN = "stripeToken";
        final String EMAIL = "stripeEmail";
        final String finalToken = token.getId();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://10.32.2.79:3000/charges";

        Log.d("", finalToken);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("", "API OK");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("", "API PAS OK");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(TOKEN, finalToken);
                params.put(EMAIL, "thomas.victoria@hetic.net");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
