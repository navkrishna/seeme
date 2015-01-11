package com.intelligrape.seeme;

import android.os.Bundle;

/**
 * Created by Navkrishna on Jan 01, 2015
 */
public class RegisterActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewsAndAddListener();
        setToolbar(R.id.toolbar);
    }

    private void findViewsAndAddListener() {
    }
}
