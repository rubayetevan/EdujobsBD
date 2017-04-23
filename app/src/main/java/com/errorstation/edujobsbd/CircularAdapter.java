package com.errorstation.edujobsbd;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Rubayet on 23-Apr-17.
 */

public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.Holder> {
    private Context context;

    public CircularAdapter(Context context) {

        this.context = context;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circular_list_view, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "Siyamrupali.ttf");
        holder.nameTV.setTypeface(typeFace);
        holder.dateTV.setTypeface(typeFace);
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,PdfViewActivity.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Activity activity = (Activity) context;
                    Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(activity, holder.frameLayout, holder.frameLayout
                            .getTransitionName()).toBundle();
                    context.startActivity(intent, bundle1);
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return 50;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView nameTV, dateTV;
        CardView cardView;
        FrameLayout frameLayout;

        Holder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout);

        }
    }
}
