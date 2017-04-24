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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView circularRV;
    int select;
    Toolbar toolbar;
    TextView headingTV;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        circularRV = (RecyclerView) findViewById(R.id.circularRV);
        progressBar = (ProgressBar) findViewById(R.id.mainPB);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        changeTypeface(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.getMenu().getItem(0).setChecked(true);
        select = 0;
        showInformation(getString(R.string.category_university));
    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.mainTB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        headingTV = (TextView) findViewById(R.id.headingTV);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");
        headingTV.setTypeface(typeFace);

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
                    if (select != 0) {
                        showInformation(getString(R.string.category_university));
                        select = 0;
                    }

                    return true;
                case R.id.navigation_dashboard:
                    if (select != 1) {
                        showInformation(getString(R.string.category_job));
                        select = 1;
                    }

                    return true;

            }
            return false;
        }

    };

    private void showInformation(String category) {

        if (isInternetAvailable(MainActivity.this)) {

            circularRV.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            API.Factory.getInstance().getCriculars(category).enqueue(new Callback<CricularModel>() {
                @Override
                public void onResponse(Call<CricularModel> call, Response<CricularModel> response) {
                    try {

                        CircularAdapter circularAdapter = new CircularAdapter(MainActivity.this, response.body().getCircular());
                        LinearLayoutManager layoutManager =
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        circularRV.setLayoutManager(layoutManager);
                        circularRV.setAdapter(circularAdapter);
                        circularRV.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CricularModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Typeface typeFace = Typeface.createFromAsset(getAssets(), "Siyamrupali.ttf");

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(banglaEncode(typeFace, Html.fromHtml("<font color=\"#FF0000\">" + getString(R.string.warning) + "</font>")));
            builder.setMessage(banglaEncode(typeFace, getString(R.string.internet_nai)));
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

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
