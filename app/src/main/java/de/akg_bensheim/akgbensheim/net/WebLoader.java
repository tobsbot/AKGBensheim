package de.akg_bensheim.akgbensheim.net;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import de.akg_bensheim.akgbensheim.R;

/**
 * Created by tobiaserthal on 10.03.15.
 */
public class WebLoader extends AsyncTaskLoader<WebLoader.Response> {
    private Response data;
    private String url;

    public WebLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Response loadInBackground() {
        Response response = new Response();
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            response.responseCode = connection.getResponseCode();
            response.data = (String) connection.getContent();
            response.lastModified = new Date(connection.getLastModified());

        } catch (IOException e) {
            response.responseCode = -1;
            e.printStackTrace();
        }

        switch (response.responseCode) {
            case -1:
                response.data = readFile(R.raw.err403);
                break;
            case 301:
                response.data = readFile(R.raw.err403);
                break;
            case 404:
                response.data = readFile(R.raw.err403);
                break;
            default:
                response.data = readFile(R.raw.err403);
                break;
        }

        return response;
    }

    @Override
    public void deliverResult(Response data) {
        if(isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            release(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Response oldData = this.data;
        this.data = data;

        if(isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            release(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if(data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if(data != null) {
            release(data);
            data = null;
        }
    }

    @Override
    public void onCanceled(Response data) {
        super.onCanceled(data);
        release(data);
    }

    public void release(Response response) {
        response.responseCode = 0;
        response.data = null;
        response.lastModified = null;
    }

    public class Response {
        public int responseCode;
        public Date lastModified;
        public String data;
    }

    private String readFile(int id) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getContext().getResources().openRawResource(id))
        );
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null)
                text.append(line);
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}