package com.intelligrape.seeme.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Navkrishna on Feb 06, 2015
 */
public class SMTextWatcher implements TextWatcher {

    private EditText editText;
    private String message;

    public SMTextWatcher(EditText editText, String message) {
        this.editText = editText;
        this.message = message;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before == 0) {
            Utility.clearError(editText, message);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
