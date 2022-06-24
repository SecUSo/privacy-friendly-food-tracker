package org.secuso.privacyfriendlyfoodtracker.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.SearchResultAdapter;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFoodFragment extends Fragment {

    SharedStatisticViewModel sharedStatisticViewModel;
    Activity referenceActivity;
    View parentHolder;
    DatabaseFacade databaseFacade;
    private RecyclerView foodList;
    private LinearLayoutManager llm;



    public EditFoodFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_edit_food, container, false);
        sharedStatisticViewModel = ViewModelProviders.of(getActivity()).get(SharedStatisticViewModel.class);
        try {
            databaseFacade = new DatabaseFacade(referenceActivity.getApplicationContext());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        foodList = (RecyclerView) parentHolder.findViewById(R.id.search_results_edit);
        llm = new LinearLayoutManager(referenceActivity.getApplicationContext());


        foodList.setLayoutManager(llm);

        foodList.addItemDecoration(new DividerItemDecoration(referenceActivity.getApplicationContext(), LinearLayoutManager.VERTICAL));

        // final SearchResultAdapter adapter = new SearchResultAdapter(databaseFacade.findMostCommonProducts(), FoodInfosToShow.getFoodInfosShownAsMap(getContext()));
        final EditText search = parentHolder.findViewById(R.id.search_term_edit);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // search in the local db first
                SearchResultAdapter newAdapter = new SearchResultAdapter(
                        databaseFacade.getProductByName(s.toString()),
                        FoodInfosToShow.getFoodInfosShownAsMap(getContext())
                );
                foodList.setAdapter(newAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        foodList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private static final int MAX_CLICK_DURATION = 100;
            private long startClickTime;
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            // click event has occurred
                            CardView childView = (CardView) recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                            if(null == childView){
                                return false;
                            }
                            int id = 0;
                            try {
                                TextView idView = childView.findViewById(R.id.resultId);
                                id = Integer.parseInt(idView.getText().toString());
                            } catch (NullPointerException exc){
                                exc.printStackTrace();
                            }
                            TextView nameView = childView.findViewById(R.id.resultName);
                            String name = nameView.getText().toString();

                            /* this used to be the way of getting the selected products calories etc.
                            I do not understand, why it was done through parsing textViews contents.
                            I changed it to instead use the product's id and getting the product from the adapter.

                            TextView calView = childView.findViewById(R.id.resultCalories);
                            String cal = calView.getText().toString();
                            cal = cal.split(" ")[0];
                            float calories = Float.parseFloat(cal);

                             */
                            Product product=((SearchResultAdapter)foodList.getAdapter()).getProductFromId(id);

                            ((BaseAddFoodActivity) referenceActivity).id = id;
                            ((BaseAddFoodActivity) referenceActivity).name = name;
                            ((BaseAddFoodActivity) referenceActivity).calories = product.energy;
                            ((BaseAddFoodActivity) referenceActivity).selectedProduct = product;
                            ((BaseAddFoodActivity) referenceActivity).productSet = true;

                            switchToAddFoodFragment();
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent e) {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        return parentHolder;
    }

    /***
     * Replaces the current fragment with an instance of the AddFoodFragment suited for editing the
     * selected product. This is probably not the best way to achieve this, since a hardcoded
     * id is used. But it allows to still use the tab layout and to get back to the original
     * EditFoodFragment by pressing the back key.
     */
    private void switchToAddFoodFragment() {
        // Create new fragment and transaction
        Fragment newFragment = AddFoodFragment.newInstance(false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.edit_food_fragment_container_view, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}