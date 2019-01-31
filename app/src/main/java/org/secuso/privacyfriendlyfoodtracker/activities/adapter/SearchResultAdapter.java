package org.secuso.privacyfriendlyfoodtracker.activities.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Product;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {
    private List<Product> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;

        public SearchViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchResultAdapter(List<Product> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchResultAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        SearchViewHolder vh = new SearchViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ((TextView) holder.mCardView.findViewById(R.id.resultName)).setText(mDataset.get(position).name);
        ((TextView) holder.mCardView.findViewById(R.id.resultCalories)).setText(Integer.toString(mDataset.get(position).energy) + " kCal");
        ((TextView) holder.mCardView.findViewById(R.id.resultId)).setText(Integer.toString(mDataset.get(position).id));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public List<Product> getProducts() {
        return mDataset;
    }

    public void addItems(List<Product> products) {
        mDataset.addAll(products);
        notifyItemInserted(mDataset.size());
    }

    public void clearItems(){
        mDataset = new ArrayList<>();
        notifyItemInserted(mDataset.size());
    }
}