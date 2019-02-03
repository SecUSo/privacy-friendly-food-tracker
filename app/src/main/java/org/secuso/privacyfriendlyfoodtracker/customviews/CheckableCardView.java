package org.secuso.privacyfriendlyfoodtracker.customviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.widget.Checkable;

/**
 * Makes a card view checkable
 * @author Simon Reinkemeier
 */
public class CheckableCardView extends CardView
        implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked,
    };

    /**
     * merges state values into base drawable states
     * @param extraSpace the extra space
     * @return a drawable state
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState =
                super.onCreateDrawableState(extraSpace + 1);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /**
     * Constructors and view initialization
     * @param t the context
     */
    public CheckableCardView(Context t){
        super(t);
    }

    private boolean isChecked = false;

    /**
     * toggles the state if a long click is performed
     * @return true if the parent method returns true
     */
    @Override
    public boolean performLongClick() {
        toggle();
        return super.performLongClick();
    }

    /**
     * Set the check state to 'checked'
     * @param checked the new 'isChecked' state
     */
    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    /**
     * Returns the state of the check
     * @return true if cardview is checked
     */
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * inverses the check state every time its called.
     */
    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }
}
