package com.intelligrape.seeme;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Navkrishna on Jan 01, 2015
 */
public class BaseActivity extends ActionBarActivity {

    Toolbar mToolbar;
    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    void setToolbar(int toolbarId) {
        mToolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(mToolbar);
    }

    void setToolbar(int toolbarId, boolean displayHomeAsUpEnabled) {
        setToolbar(toolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
