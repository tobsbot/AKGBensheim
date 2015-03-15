package de.akg_bensheim.akgbensheim;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.akg_bensheim.akgbensheim.adapter.ToolBarSpinnerAdapter;
import de.akg_bensheim.akgbensheim.preferences.SettingsActivity;
import de.akg_bensheim.akgbensheim.utils.ConnectionDetector;

public class MainActivity extends ActionBarActivity
        implements Spinner.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String KEY_SELECTED_INDEX = "selected_index";
    private static final String URL_FIXED = "http://www.akg-bensheim.de/akgweb2011/content/Vertretung/w/%02d/w00000.htm";

    private int selectedIndex = 0;
    private boolean fromSavedInstanceState;
    private int week;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinner;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* State check */
        if(savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0);
            fromSavedInstanceState = true;
        }

        /* View id lookup */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        webView = (WebView) findViewById(R.id.webView);

        /* Toolbar setup */
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Inflate spinner layout and add it to the toolbar*/
        View spinnerContainer = LayoutInflater.from(this)
                .inflate(R.layout.toolbar_spinner, toolbar, false);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        toolbar.addView(spinnerContainer, layoutParams);

        /* Set up Spinner data */
        ToolBarSpinnerAdapter adapter = new ToolBarSpinnerAdapter(getResources().getString(R.string.title_substitute));
        adapter.addItems(getResources().getStringArray(R.array.toolbar_spinner_items));

        /* Set up the Spinner */
        spinner = (Spinner) spinnerContainer.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        if(!fromSavedInstanceState)
            spinner.setSelection(selectedIndex);
        spinner.setOnItemSelectedListener(this);

        /* Set up swipeToRefreshLayout */
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent, R.color.primaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        /* Set up webView */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
                spinner.setEnabled(true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !url.startsWith("#");
            }
        });

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
            webView.getSettings().setDisplayZoomControls(false);

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.zoomOut();
            }
        });
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(
                        new Intent(MainActivity.this, SettingsActivity.class)
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            webView.onPause();
        else
            webView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            webView.onResume();
        else
            webView.resumeTimers();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle inState) {
        super.onRestoreInstanceState(inState);
        webView.restoreState(inState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("MainActivity", "Spinner item at index: " + position + " selected.");

        if(fromSavedInstanceState) {
            fromSavedInstanceState = false;
            return;
        }
        selectedIndex = position;

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        switch (selectedIndex) {
            case 0:
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                break;
            case 1:
                calendar.add(Calendar.DATE, 7);
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                break;
        }

        if(ConnectionDetector.getInstance(getApplicationContext())
                .allowedToUseConnection("pref_key_only_wifi"))
            new Loader().execute(
                    String.format(URL_FIXED, week)
            );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("MainActivity", "No spinner item item selected.");
    }

    @Override
    public void onRefresh() {
        if(ConnectionDetector.getInstance(getApplicationContext())
                .allowedToUseConnection("pref_key_only_wifi"))
           new Loader().execute(
                   String.format(URL_FIXED, week)
           );
        else
            swipeRefreshLayout.setRefreshing(false);
    }

    protected class Loader extends AsyncTask<String, Void, Loader.Response> {
        private String url;
        class Response {
            int code;
            long lastModified;

            @Override
            public String toString() {
                return getClass().getSimpleName()
                        + " [code=" + code
                        + ", lastModified=" + lastModified
                        + "]";
            }
        }

        private static final String CODE_301 = "file:///android_asset/error/301.html";
        private static final String CODE_404 = "file:///android_asset/error/404.html";
        private static final String CODE_1 = "file:///android_asset/error/offline.html";
        private static final String CODE_UNKNOWN = "file:///android_asset/error/unknown.html";

        @Override
        protected void onPreExecute() {
            Log.d("MainActivity", "Starting Loader...");
            swipeRefreshLayout.setRefreshing(true);
            spinner.setEnabled(false);
        }

        @Override
        protected Loader.Response doInBackground(String... params) {
            url = params[0];
            Loader.Response response = new Loader.Response();

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(1000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(false);
                connection.connect();

                response.code = connection.getResponseCode();
                response.lastModified = connection.getLastModified();
                connection.disconnect();
            } catch (IOException e) {
                Log.e("Loader", "IOException occurred while connecting to: \"" + url + "\"", e);
                response.code = 1;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Loader.Response response) {
            Log.d("MainActivity", "Loader finished with result: " + response.toString());
            switch (response.code) {
                case 200:
                    webView.loadUrl(url);

                    Toast.makeText(
                            MainActivity.this,
                            new SimpleDateFormat(getResources().getString(R.string.last_modification), Locale.getDefault())
                                    .format(new Date(response.lastModified)),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case 301:
                    webView.loadUrl(CODE_301);
                    break;
                case 404:
                    webView.loadUrl(CODE_404);
                    break;
                case 1:
                    webView.loadUrl(CODE_1);
                    break;
                default:
                    webView.loadUrl(CODE_UNKNOWN);
                    break;
            }
        }
    }
}
