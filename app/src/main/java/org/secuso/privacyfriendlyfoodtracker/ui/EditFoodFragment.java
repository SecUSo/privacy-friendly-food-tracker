package org.secuso.privacyfriendlyfoodtracker.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.DatabaseFacade;
import org.secuso.privacyfriendlyfoodtracker.ui.adapter.SearchResultAdapter;
import org.secuso.privacyfriendlyfoodtracker.ui.viewmodels.SharedStatisticViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFoodFragment extends Fragment {

    SharedStatisticViewModel sharedStatisticViewModel;
    Activity referenceActivity;
    View parentHolder;
    DatabaseFacade databaseFacade;
    private RecyclerView foodList;
    private LinearLayoutManager llm;

    List<Product> products;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFoodFragment newInstance(String param1, String param2) {
        EditFoodFragment fragment = new EditFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        final SearchResultAdapter adapter = new SearchResultAdapter(databaseFacade.findMostCommonProducts(), FoodInfosToShow.getFoodInfosShownAsMap(getContext()));
        final EditText search = parentHolder.findViewById(R.id.search_term_edit);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // search in the local db first
                System.out.println(s.toString());
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

        return parentHolder;
    }
}