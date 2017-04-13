package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Liu Qing on 2015/9/10.
 */
public class KeyboardDetectorLinearLayout extends LinearLayout {

    private boolean isKeyboardShowing = false;
    private ArrayList<IKeyboardChanged> keyboardListener = new ArrayList<>();

    public KeyboardDetectorLinearLayout(Context context) {
        super(context);
    }

    public KeyboardDetectorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardDetectorLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addKeyboardStateChangedListener(IKeyboardChanged listener) {
        keyboardListener.add(listener);
    }

    public void removeKeyboardStateChangedListener(IKeyboardChanged listener) {
        keyboardListener.remove(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();

        if (actualHeight > proposedHeight) {
            notifyKeyboardShown();
            isKeyboardShowing = true;
        } else if (actualHeight < proposedHeight) {
            notifyKeyboardHidden();
            isKeyboardShowing = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void notifyKeyboardHidden() {
        for (IKeyboardChanged listener : keyboardListener) {
            listener.onKeyboardHidden();
        }
    }

    private void notifyKeyboardShown() {
        for (IKeyboardChanged listener : keyboardListener) {
            listener.onKeyboardShown();
        }
    }

    public boolean isKeyboardShowing() {
        return isKeyboardShowing;
    }

    public interface IKeyboardChanged {
        void onKeyboardShown();

        void onKeyboardHidden();
    }
}
