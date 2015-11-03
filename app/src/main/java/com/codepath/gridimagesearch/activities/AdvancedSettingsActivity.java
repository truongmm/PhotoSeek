package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;

public class AdvancedSettingsActivity extends AppCompatActivity {

    Spinner spnrImageSize;
    Spinner spnrImageColor;
    Spinner spnrImageType;
    EditText etSiteFilter;

    String[] VALID_IMAGE_SIZES = {"", "icon", "medium", "xxlarge", "huge"};
    String[] VALID_IMAGE_COLORS = {"", "gray", "color"};
    String[] VALID_IMAGE_TYPES = {"", "face", "photo", "clipart", "lineart"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_settings);

        setUpViews();
    }

    public void setUpViews() {
        Intent intent = getIntent();

        spnrImageSize = (Spinner) findViewById(R.id.spnrImageSize);
        ArrayAdapter<String> imageSizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VALID_IMAGE_SIZES);
        spnrImageSize.setAdapter(imageSizeAdapter);
        String imageSize = intent.getStringExtra("image_size");
        spnrImageSize.setSelection(imageSizeAdapter.getPosition(imageSize));

        spnrImageColor = (Spinner) findViewById(R.id.spnrImageColor);
        ArrayAdapter<String> imageColorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VALID_IMAGE_COLORS);
        spnrImageColor.setAdapter(imageColorAdapter);
        String imageColor = intent.getStringExtra("image_color");
        spnrImageColor.setSelection(imageColorAdapter.getPosition(imageColor));

        spnrImageType = (Spinner) findViewById(R.id.spnrImageType);
        ArrayAdapter<String> imageTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VALID_IMAGE_TYPES);
        spnrImageType.setAdapter(imageTypeAdapter);
        String imageType = intent.getStringExtra("image_type");
        spnrImageType.setSelection(imageTypeAdapter.getPosition(imageType));

        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        String siteFilter = intent.getStringExtra("site_filter");
        etSiteFilter.setText(siteFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_advanced_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        String imageSize = spnrImageSize.getSelectedItem().toString();
        String imageColor = spnrImageColor.getSelectedItem().toString();
        String imageType = spnrImageType.getSelectedItem().toString();
        String siteFilter = etSiteFilter.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("image_size", imageSize);
        intent.putExtra("image_color", imageColor);
        intent.putExtra("image_type", imageType);
        intent.putExtra("site_filter", siteFilter);

        setResult(RESULT_OK, intent);
        finish();
    }
}
