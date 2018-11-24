package org.secuso.privacyfriendlyfoodtracker.customviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.Checkable;
public class CheckableCardView extends CardView
        implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked,
    };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState =
                super.onCreateDrawableState(extraSpace + 1);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    // Constructors and view initialization
    public CheckableCardView(Context t){
        super(t);
    }

    private boolean isChecked = false;

    @Override
    public boolean performLongClick() {
        toggle();
        return super.performLongClick();
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }
}
