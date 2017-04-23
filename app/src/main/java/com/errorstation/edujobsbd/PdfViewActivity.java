package com.errorstation.edujobsbd;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static com.errorstation.edujobsbd.MainActivity.banglaEncode;
import static com.errorstation.edujobsbd.MainActivity.isInternetAvailable;

public class PdfViewActivity extends AppCompatActivity {
    WebView webView;
    String pdfLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_pdf_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);


        if (isInternetAvailable(PdfViewActivity.this)) {
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    findViewById(R.id.progress1).setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    findViewById(R.id.progress1).setVisibility(View.GONE);
                }

            });
            pdfLink = "http://www.pdf995.com/samples/pdf.pdf";
            webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfLink);

        } else {
            Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");

            AlertDialog.Builder builder = new AlertDialog.Builder(PdfViewActivity.this);
            builder.setTitle(banglaEncode(typeFace, Html.fromHtml("<font color=\"#FF0000\">" + getString(R.string.warning) + "</font>")));
            builder.setMessage(banglaEncode(typeFace, getString(R.string.internet_nai)));
            builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    finish();
                }
            });
            AlertDialog dialog = builder.create();

            dialog.setCancelable(false);
            dialog.show();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_menu, menu);

        MenuItem item;
        item = menu.getItem(0);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");
        item.setTitle(getString(R.string.download));
        applyFontToItem(item, typeFace);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle item selection
        switch (item.getItemId()) {
            case R.id.download_pdf:
                downloadFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadFile() {

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfLink));
        downloadManager.enqueue(request);

    }

    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 14), 0,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }
}
