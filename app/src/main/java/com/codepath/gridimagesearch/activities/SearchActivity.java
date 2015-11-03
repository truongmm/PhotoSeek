package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.models.ImageResult;
import com.codepath.gridimagesearch.utils.EndlessScrollListener;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int RESULTS_COUNT = 8;

    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private SearchFilters searchFilters;

    private class SearchFilters {
        public String query = "";
        public String imageSize = "";
        public String imageColor = "";
        public String imageType = "";
        public String siteFilter = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpViews();
        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(SearchActivity.this, imageResults);
        gvResults.setAdapter(aImageResults);
        searchFilters = new SearchFilters();
    }

    public void setUpViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageResult imageResult = imageResults.get(position);
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                intent.putExtra("image", imageResult);
                startActivity(intent);
            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchQuery(true);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilters.query = query;
                fetchQuery(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void fetchQuery(final boolean loadMore) {
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + RESULTS_COUNT;
        if (searchFilters.query.length() != 0)
            searchUrl += "&q=" + searchFilters.query;
        if (searchFilters.imageSize != "none")
            searchUrl += "&imgsz=" + searchFilters.imageSize;
        if (searchFilters.imageColor != "none")
            searchUrl += "&imgc=" + searchFilters.imageColor;
        if (searchFilters.imageType != "none")
            searchUrl += "&imgtype=" + searchFilters.imageType;
        if (searchFilters.siteFilter.length() != 0)
            searchUrl += "&as_sitesearch=" + searchFilters.siteFilter;
        if (loadMore)
            searchUrl += "&start=" + imageResults.size();

        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJSON;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    if (!loadMore)
                        imageResults.clear();
                    aImageResults.addAll(ImageResult.fromJSON(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    public void showAdvancedSettings(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, AdvancedSettingsActivity.class);
        intent.putExtra("image_size", searchFilters.imageSize);
        intent.putExtra("image_color", searchFilters.imageColor);
        intent.putExtra("image_type", searchFilters.imageType);
        intent.putExtra("site_filter", searchFilters.siteFilter);

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            searchFilters.imageSize = intent.getStringExtra("image_size");
            searchFilters.imageColor = intent.getStringExtra("image_color");
            searchFilters.imageType = intent.getStringExtra("image_type");
            searchFilters.siteFilter = intent.getStringExtra("site_filter");

            fetchQuery(false);
        }
    }
}
