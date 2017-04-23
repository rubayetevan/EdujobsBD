package com.errorstation.edujobsbd;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView circularRV;
    int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circularRV = (RecyclerView) findViewById(R.id.circularRV);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        changeTypeface(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.getMenu().getItem(0).setChecked(true);
        select =1;
        showInformation();
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void changeTypeface(BottomNavigationView navigation) {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");

        MenuItem item;

        item = navigation.getMenu().findItem(R.id.navigation_home);
        item.setTitle(getString(R.string.title_home));
        applyFontToItem(item, typeFace);

        item = navigation.getMenu().findItem(R.id.navigation_dashboard);
        item.setTitle(getString(R.string.title_dashboard));
        applyFontToItem(item, typeFace);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(select!=0)
                    {
                        showInformation();
                        select=0;
                    }

                    return true;
                case R.id.navigation_dashboard:
                    if(select!=1)
                    {
                        circularRV.setVisibility(View.GONE);
                        select=1;
                    }

                    return true;

            }
            return false;
        }

    };

    private void showInformation() {

        if (isInternetAvailable(MainActivity.this)) {

            circularRV.setVisibility(View.VISIBLE);
            CircularAdapter circularAdapter = new CircularAdapter(this);
            LinearLayoutManager layoutManager =
                    new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            circularRV.setLayoutManager(layoutManager);
            circularRV.setAdapter(circularAdapter);
        } else {
            Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(banglaEncode(typeFace, Html.fromHtml("<font color=\"#FF0000\">" + getString(R.string.warning) + "</font>")));
            builder.setMessage(banglaEncode(typeFace, getString(R.string.internet_nai)));
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //do things
                    finish();
                }
            });
            AlertDialog dialog = builder.create();

            dialog.setCancelable(false);
            dialog.show();
        }


    }

    public static SpannableString banglaEncode(Typeface typeface, CharSequence string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new CustomTypefaceSpan("", typeface, 16), 0,
                s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return s;
    }

    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 14), 0,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item;
        item = menu.getItem(0);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");
        item.setTitle(getString(R.string.about_us));
        applyFontToItem(item, typeFace);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_us:
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(banglaEncode(typeFace, Html.fromHtml("<font color=\"#FF0000\">" + getString(R.string.about_us) + "</font>")));
                builder.setMessage(banglaEncode(typeFace, getString(R.string.about_us_description)));
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.setCancelable(false);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
