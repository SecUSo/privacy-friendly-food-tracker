package org.secuso.privacyfriendlyfoodtracker.ui;

import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfoodtracker.R;

/**
 * A simple {@link Fragment} subclass.
 * This fragment only holds the EditFoodFragment and, when EditFoodFragment decides so, the
 * AddFoodFragment. This extra holder fragment is needed, so switching the tabs in the
 * Tab viewpager still works. There might be a cleaner way to achieve this, though.
 */
public class EditFoodHolderFragment extends Fragment {
    View parentHolder;

    public EditFoodHolderFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder = inflater.inflate(R.layout.fragment_edit_food_holder, container, false);
        getFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.edit_food_fragment_container_view, new EditFoodFragment())
                .commit();
        return parentHolder;

    }
}