package com.codepath.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    private class ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle;
    }

    public ImageResultsAdapter(Context context, ArrayList<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivFullImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.ivImage.setImageResource(0);
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));

        return convertView;
    }
}
