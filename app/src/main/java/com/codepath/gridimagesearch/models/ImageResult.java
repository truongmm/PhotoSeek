package com.codepath.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult implements Parcelable {

    public String fullUrl;
    public String thumbUrl;
    public String title;
    public String displayTitle;

    public ImageResult(JSONObject jsonObject) {
        try {
            this.fullUrl = jsonObject.getString("url");
            this.thumbUrl = jsonObject.getString("tbUrl");
            this.title = jsonObject.getString("title");
            this.displayTitle = jsonObject.getString("titleNoFormatting");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSON(JSONArray jsonArray) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                imageResults.add(new ImageResult(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return imageResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullUrl);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.title);
        dest.writeString(this.displayTitle);
    }

    private ImageResult(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbUrl = in.readString();
        this.title = in.readString();
        this.displayTitle = in.readString();
    }

    public static final Creator<ImageResult> CREATOR = new Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
