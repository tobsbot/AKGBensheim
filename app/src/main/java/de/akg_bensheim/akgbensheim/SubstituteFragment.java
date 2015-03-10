package de.akg_bensheim.akgbensheim;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.akg_bensheim.akgbensheim.net.ConnectionDetector;
import de.akg_bensheim.akgbensheim.net.WebLoader;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubstituteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubstituteFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<WebLoader.Response> {

    protected static final String URL_FIXED= "http://www.akg-bensheim.de/akgweb2011/content/Vertretung/w/%02d/w00000.htm";

    //Key to the number of the week to load
    private static final String ARG_WEEK = "int:week";
    private int week;

    private WebView webView;
    private boolean webViewAvailable;
    private SwipeRefreshLayout swipeRefreshLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param week The week number to load
     * @return A new instance of fragment SubstituteFragment.
     */
    public static SubstituteFragment newInstance(int week) {
        SubstituteFragment fragment = new SubstituteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WEEK, week);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Empty constructor. Use {@link #newInstance(int)}
     * for initialization.
     */
    public SubstituteFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            week = getArguments().getInt(ARG_WEEK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (webView != null) {
            webView.destroy();
        }

        // Inflate the layout for this fragment
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_supply, container, false);
        swipeRefreshLayout.setOnRefreshListener(this);

        webView = (WebView) swipeRefreshLayout.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){

        });
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webViewAvailable = true;
        return swipeRefreshLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        webViewAvailable = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public WebView getWebView() {
        return webViewAvailable ? webView : null;
    }

    @Override
    public void onRefresh() {
        if(ConnectionDetector.getInstance(getActivity().getApplicationContext()).allowedToUseConnection("")) {
            Bundle args = new Bundle();
            args.putString("url", URL_FIXED);
            getLoaderManager().restartLoader(0, args, this);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public Loader<WebLoader.Response> onCreateLoader(int id, Bundle args) {
        return new WebLoader(getActivity(), args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<WebLoader.Response> loader, WebLoader.Response data) {
        webView.loadData(data.data, "text/html", "utf-8");

        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<WebLoader.Response> loader) {
        Log.d("onLoaderReset", "WebLoader for week " + week + " was reset!");
    }
}
