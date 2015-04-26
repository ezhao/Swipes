package com.herokuapp.ezhao.swipes;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class TaskListAdapter extends ArrayAdapter<String>{
    private int color_background_neutral;

    public TaskListAdapter(Context context, List<String> tasks) {
        super(context, 0, tasks);
        color_background_neutral = context.getResources().getColor(R.color.background_neutral);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String task = getItem(position);

        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.tvTaskName.setText(task);

        convertView.setOnTouchListener(new OnSwipeListListener() {
            @Override
            public void onMove(float start, float end) {
                float totalX = holder.rlTopLayer.getWidth();
                final double active_threshold = 0.15 * totalX;

                float newX = holder.rlTopLayer.getX() + end - start;

                holder.rlTopLayer.setX(newX);
                if (Math.abs(newX) < active_threshold) {
                    holder.rlBottomLayer.setBackgroundColor(color_background_neutral);
                } else if (newX < 0) {
                    holder.rlBottomLayer.setBackgroundColor(Color.BLUE);
                } else if (newX > 0) {
                    holder.rlBottomLayer.setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onRelease() {
                float totalX = holder.rlTopLayer.getWidth();
                final double active_threshold = 0.15 * totalX;

                float newX = holder.rlTopLayer.getX();

                ObjectAnimator swipeAnimate = null;
                if (Math.abs(newX) < active_threshold) {
                    swipeAnimate = ObjectAnimator.ofFloat(holder.rlTopLayer, "X", newX, 0);
                } else if (newX < 0) {
                    swipeAnimate = ObjectAnimator.ofFloat(holder.rlTopLayer, "X", newX, totalX * -1);
                } else if (newX > 0) {
                    swipeAnimate = ObjectAnimator.ofFloat(holder.rlTopLayer, "X", newX, totalX);
                }
                if (swipeAnimate != null) {
                    swipeAnimate.start();
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.rlBottomLayer) RelativeLayout rlBottomLayer;
        @InjectView(R.id.rlTopLayer) RelativeLayout rlTopLayer;
        @InjectView(R.id.tvTaskName) TextView tvTaskName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
