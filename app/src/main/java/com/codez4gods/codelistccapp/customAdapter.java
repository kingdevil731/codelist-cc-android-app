package com.codez4gods.codelistccapp;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class customAdapter extends ArrayAdapter<model> implements View.OnClickListener{

    private ArrayList<model> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        ImageView info;
    }

    public customAdapter(ArrayList<model> data, Context context) {
        super(context, R.layout.row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position =(Integer) v.getTag();
        Object object = getItem(position);
        model dataModel = (model) object;

        switch (v.getId())
        {
            case R.id.mensagem:
            /*    Snackbar.make(v, "Release date " +dataModel.getContent(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();*/
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        model dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.titulo);
            viewHolder.txtType = convertView.findViewById(R.id.mensagem);
            //viewHolder.info = convertView.findViewById(R.id.image_url);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        assert dataModel != null;
        viewHolder.txtName.setText(dataModel.getTitle());
        viewHolder.txtType.setText(dataModel.getContent());
        //viewHolder.info.setOnClickListener(this);
        viewHolder.txtName.setOnClickListener(this);
        viewHolder.txtName.setTag(position);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}