package com.jueze.colorpicker;

/*
 * Copyright (C) 2010 Daniel Nilsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import java.util.Locale;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.Gravity;
import android.widget.RelativeLayout;

public class ColorPickerDialog extends AppCompatDialog implements ColorPickerView.OnColorChangedListener,View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private ColorPickerView mColorPicker;

    private ColorPickerPanelView mOldColor;
    private ColorPickerPanelView mNewColor;

    private EditText mHexVal;
    private boolean mHexValueEnabled = false;
    private ColorStateList mHexDefaultTextColor;

    private OnColorChangedListener mListener;
    private int mOrientation;
    private View mLayout;

    private String mTitle;

    @Override
    public void onGlobalLayout() {
        if (getContext().getResources().getConfiguration().orientation != mOrientation) {
            final int oldcolor = mOldColor.getColor();
            final int newcolor = mNewColor.getColor();
            mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            setUp(oldcolor);
            mNewColor.setColor(newcolor);
            mColorPicker.setColor(newcolor);
        }
    }

    public interface OnColorChangedListener {
        public void onColorChanged(int color);
    }
	
	public ColorPickerDialog(Context context){
		this(context,Color.WHITE);
	}
	
	public ColorPickerDialog(Context context, int color){
		this(context,color,"");
	}

    public ColorPickerDialog(Context context, int initialColor, String title) {
        super(context);
        mTitle = title;
        init(initialColor);
		
		setAlphaSliderVisible(true);
		setHexValueEnabled(true);
    }

    private void init(int color) {
        // To fight color banding.
        getWindow().setFormat(PixelFormat.RGBA_8888);

		//setAlphaSliderVisible(true);
		//setHexValueEnabled(true);
        setUp(color);

    }

	private LinearLayout mHexWrapper;
	private TextView mHexCopy, mHexHead;

    private void setUp(int color) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mLayout = inflater.inflate(R.layout.dialog_color_picker, null);
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mOrientation = getContext().getResources().getConfiguration().orientation;
        setContentView(mLayout);

        setTitle(mTitle);

        mColorPicker = (ColorPickerView) mLayout.findViewById(R.id.color_picker_view);
        mOldColor = (ColorPickerPanelView) mLayout.findViewById(R.id.old_color_panel);
        mNewColor = (ColorPickerPanelView) mLayout.findViewById(R.id.new_color_panel);

        mHexVal = (EditText) mLayout.findViewById(R.id.hex_val);
        mHexVal.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mHexDefaultTextColor = mHexVal.getTextColors();

		//
		mHexCopy = findViewById(R.id.hex_copy);
		mHexWrapper = findViewById(R.id.hex_wrapper);
		mHexHead = findViewById(R.id.hex_val_head);
		mNewColor.post(new Runnable(){

				@Override
				public void run() {
					ViewGroup.LayoutParams lp = mHexWrapper.getLayoutParams();
					lp.width = mNewColor.getWidth();
					mHexWrapper.setLayoutParams(lp);
				}
			});



        mHexVal.setOnEditorActionListener(new TextView.OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						String s = mHexVal.getText().toString();
						if (s.length() > 5 || s.length() < 9) {
							try {
								int c = ColorPickerPreference.convertToColorInt(s.toString());
								mColorPicker.setColor(c, true);
								mHexVal.setTextColor(mHexDefaultTextColor);
							} catch (IllegalArgumentException e) {
								mHexVal.setTextColor(Color.RED);
							}
						} else {
							mHexVal.setTextColor(Color.RED);
						}
						return true;
					}
					return false;
				}
			});

        ((LinearLayout) mOldColor.getParent()).setPadding(
			Math.round(mColorPicker.getDrawingOffset()),
			0,
			Math.round(mColorPicker.getDrawingOffset()),
			0
        );

		mHexCopy.setOnClickListener(this);
        mOldColor.setOnClickListener(this);
        mNewColor.setOnClickListener(this);
        mColorPicker.setOnColorChangedListener(this);
        mOldColor.setColor(color);
        mColorPicker.setColor(color, true);
		
		mHexVal.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					
					String s=p1.toString();
					if(s.startsWith("#")){
						mHexVal.setText(s.substring(1));
					}
				}

				@Override
				public void afterTextChanged(Editable p1) {
				
				}
			});

    }


    @Override
    public void onClick(View v) {
		if (v.getId() == R.id.new_color_panel) {
			if (mListener != null) {
				mListener.onColorChanged(mNewColor.getColor());
			}
        } else if (v.getId() == R.id.hex_copy) {
			ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText("#" + mHexVal.getText());
			Toast.makeText(getContext(),"已写入剪切板",0).show();
		}
		dismiss();
	}

    @Override
    public void onColorChanged(int color) {

        mNewColor.setColor(color);

        if (mHexValueEnabled)
            updateHexValue(color);

		/*
		 if (mListener != null) {
		 mListener.onColorChanged(color);
		 }
		 */

    }

    public void setHexValueEnabled(boolean enable) {
        mHexValueEnabled = enable;
        if (enable) {
			mHexCopy.setVisibility(View.VISIBLE);
			mHexHead.setVisibility(View.VISIBLE);
            mHexVal.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else {
			mHexCopy.setVisibility(View.GONE);
			mHexHead.setVisibility(View.GONE);
            mHexVal.setVisibility(View.GONE);
		}
    }

    public boolean getHexValueEnabled() {
        return mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible())
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        else
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            mHexVal.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            mHexVal.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        mHexVal.setTextColor(mHexDefaultTextColor);
    }

    public void setAlphaSliderVisible(boolean visible) {
        mColorPicker.setAlphaSliderVisible(visible);
        if (mHexValueEnabled) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    public boolean getAlphaSliderVisible() {
        return mColorPicker.getAlphaSliderVisible();
    }

    /**
     * Set a OnColorChangedListener to get notified when the color
     * selected by the user has changed.
     *
     * @param listener
     */
    public void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    public int getColor() {
        return mColorPicker.getColor();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("old_color", mOldColor.getColor());
        state.putInt("new_color", mNewColor.getColor());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOldColor.setColor(savedInstanceState.getInt("old_color"));
        mColorPicker.setColor(savedInstanceState.getInt("new_color"), true);
    }
	
}

