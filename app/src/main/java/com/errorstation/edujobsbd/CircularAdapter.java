package com.errorstation.edujobsbd;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

/**
 * Created by Rubayet on 23-Apr-17.
 */

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.Holder> {
    private Context context;
    private List<Circular> circular = null;

    public CircularAdapter(Context context, List<Circular> circular) {

        this.context = context;
        this.circular = circular;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circular_list_view, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "Siyamrupali.ttf");
        holder.nameTV.setTypeface(typeFace);
        holder.dateTV.setTypeface(typeFace);
        holder.moneyTV.setTypeface(typeFace);
        holder.typeTV.setTypeface(typeFace);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.cardView.setElevation(100);
        }

        try {
            if(circular.get(position).getFee().matches("")||circular.get(position).getFee().equals("")||circular.get(position).getFee()==null)
            {
                holder.moneyTV.setText("প্র/ন");
            }
            else
            {
                holder.moneyTV.setText(circular.get(position).getFee()+" টাকা");
            }

            holder.nameTV.setText(circular.get(position).getTitle());
            holder.dateTV.setText(circular.get(position).getStartdate()+" হইতে "+circular.get(position).getEnddate());
            String type = circular.get(position).getType();
            String New  = circular.get(position).getNew();
            if(New.equalsIgnoreCase("0"))
            {
                holder.newIMGV.setVisibility(View.GONE);
            }else
            {
                holder.newIMGV.setVisibility(View.VISIBLE);
            }

            if(type.equalsIgnoreCase("Government"))
            {
                holder.typeTV.setText(context.getString(R.string.demo_govt));
                holder.cardView.setCardBackgroundColor(Color.parseColor("#EC407A"));
            }
            else if(type.equalsIgnoreCase("Semi-Government"))
            {
                holder.typeTV.setText(context.getString(R.string.demo_semigovt));
                holder.cardView.setCardBackgroundColor(Color.parseColor("#89929F"));
            }
            else if(type.equalsIgnoreCase("Private"))
            {
                holder.typeTV.setText(context.getString(R.string.demo_private));
                holder.cardView.setCardBackgroundColor(Color.parseColor("#009688"));
            }
            else if(type.equalsIgnoreCase("পাবলিক"))
            {
                holder.typeTV.setText("পাবলিক");
                holder.cardView.setCardBackgroundColor(Color.parseColor("#EC407A"));
            }
            else if(type.equalsIgnoreCase("প্রাইভেট"))
            {
                holder.typeTV.setText("প্রাইভেট");
                holder.cardView.setCardBackgroundColor(Color.parseColor("#009688"));
            }

        } catch (NullPointerException e) {
            FirebaseCrash.report(new Exception(e.getMessage()));
            e.printStackTrace();
        }

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pdfLink = null;
                String pdfTitle = null;
                try {
                    pdfLink = circular.get(position).getLink();
                    pdfTitle = circular.get(position).getTitle();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (pdfLink != null) {
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("pdfLink", pdfLink);
                    intent.putExtra("pdfTitle", pdfTitle);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Activity activity = (Activity) context;
                        Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(activity, holder.frameLayout, holder.frameLayout
                                .getTransitionName()).toBundle();
                        context.startActivity(intent, bundle1);
                    }
                    else {
                        context.startActivity(intent);
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return circular.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView nameTV, dateTV,typeTV,moneyTV;
        CardView cardView;
        FrameLayout frameLayout;
        ImageView newIMGV;

        Holder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            typeTV = (TextView) itemView.findViewById(R.id.typeTV);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            moneyTV = (TextView) itemView.findViewById(R.id.moneyTV);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout);
            newIMGV = (ImageView) itemView.findViewById(R.id.newIMGV);

        }
    }
}
