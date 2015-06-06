package com.example.javier.MaterialDesignApp;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.javier.MaterialDesignApp.WebChrome.VideoEnabledWebChromeClient;
import com.example.javier.MaterialDesignApp.WebChrome.VideoEnabledWebView;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class DetailActivity extends ActionBarActivity {

    final Context context = this;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ScrollView scrollView;
    ActionBarDrawerToggle mDrawerToggle;
    int theme, scrollPositionX = 0, scrollPositionY = -100;
    Intent intent;
    ActivityOptions options;
    TextView textViewName, textViewLink;
    EditText editTextFacebookID;
    ImageView imageViewToogle, imageViewCover, imageViewPicture, imageViewSend;
    ToggleButton toggleButtonDrawer;
    FrameLayout statusBar, frameLayoutSwitch, frameLayoutCheckBox, frameLayoutRadioButton;
    RelativeLayout relativeLayoutDrawerTexts, relativeLayoutChooseTheme;
    LinearLayout linearLayoutMain, linearLayoutSecond, linearLayoutSettings;
//    String urlName = "javiersegoviacordoba";
    String urlName = "nguyenthuongthong";
    String urlProfile = "https://graph.facebook.com/" + urlName;
    String urlPicture = "https://graph.facebook.com/" + urlName + "picture?type=large&redirect=false";
    String urlCover = "https://graph.facebook.com/" + urlName + "cover";
    String name, link, cover, picture, facebookID;
    Dialog dialog;
    Boolean homeButton = false, themeChanged;
    ViewGroup.LayoutParams layoutParamsStatusBar;
    SwitchCompat switchCompat;
    CheckedTextView checkBox, radioButton;
    JSONArray jsonArrayNewsPost;
    String detail,ua,linkurl,title,source,description,pubdate;
   // WebView webViewDetail;
    private VideoEnabledWebView webViewDetail;
    private VideoEnabledWebChromeClient webChromeClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get shared preferences
        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);

        // Select theme saved by user (always before setContentView)
        theme();

        // Set content to the view
        setContentView(R.layout.activity_detail);

        //Setup Status Bar and Toolbar
        toolbarStatusBar();

        // Fix issues for each version and modes (check method at end of this file)
        navigationBarStatusBar();

        // Set elements

        ua = "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        webViewDetail = (VideoEnabledWebView) findViewById(R.id.webViewDetail);
        final WebSettings webSettings = webViewDetail.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setUserAgentString(ua);
        webSettings.setSaveFormData(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);


        title=sharedPreferences.getString("TITLE", "");
        source=sharedPreferences.getString("SOURCE", "");
        pubdate=sharedPreferences.getString("DATE", "");
        description=sharedPreferences.getString("DESCRIPTION", "");
        linkurl = sharedPreferences.getString("LINK", "");


        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webViewDetail) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                }
                else
                {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webViewDetail.setWebChromeClient(webChromeClient);

         new GetData().execute();

       // webViewDetail.loadUrl("http://m.youtube.com");

    }

    public class GetData extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(linkurl).userAgent(ua).get();
                detail = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" "
                        + "content=\"text/html; charset=utf-8\"> </head>"
                        + "<body style=\"background-color:transparent;overflow-x:hidden;height:auto;\">"
                        + "<h3 style = \" color: red \">" + title + "</h2>";
                detail += "<font size=\" 1.8em \" style = \" color: #005500 \"><em>"
                        + source + " - " + pubdate + "</em></font>";
                detail += "<p style = \" color: #666666 \"><b>" + description
                        + "</b></p>";
              //  doc.select("iframe").remove();
                Elements e = doc.select("div.article ");
               // String chuoi=e.toString().replaceAll("<[aA].*?>", "<u>");
               // chuoi=chuoi.replaceAll("</[aA]>", "</u>");

                detail +=e.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                webViewDetail.loadDataWithBaseURL(
                        "",
                        "<style>img{display: inline;height: auto;max-width: 100%;}"
                                + " body{ padding-bottom:0; margin-bottom:0}"
                                + " p {font-family:\"Tangerine\", \"Sans-serif\",  \"Serif\" , color: #000000} </style>"
                                + detail + "</body></html>", "text/html", "UTF-8",
                        "");

            } catch(Exception e){}
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
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

    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    public void toolbarStatusBar() {

        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {

        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                DetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                DetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                DetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                DetailActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    public void settingTheme(int theme) {
        switch (theme) {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }

}
