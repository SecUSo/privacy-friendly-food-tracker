/*
This file is part of Privacy friendly food tracker.

Privacy friendly food tracker is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Privacy friendly food tracker is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Privacy friendly food tracker.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.secuso.privacyfriendlyfoodtracker.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import org.secuso.privacyfriendlyfoodtracker.R;
import org.secuso.privacyfriendlyfoodtracker.database.Product;

import java.util.List;
import java.util.Locale;

/**
 * Implementation of SearchResultAdapter that uses a Fragment to manage each page. This class also handles saving and restoring of fragment's state.
 *
 * @author Simon Reinkemeier
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {
    private List<Product> mDataset;



    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;

        /**
         * Holds cards that are added to the seach results
         *
         * @param v a CardView to be added to the search results
         */
        public SearchViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     * @param myDataset the dataset that is used for the item creation
     */
    public SearchResultAdapter(List<Product> myDataset) {
        mDataset = myDataset;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent the ViewGroup that holds this result
     * @param viewType the viewType
     * @return
     */
    @Override
    public SearchResultAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        SearchViewHolder vh = new SearchViewHolder(v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder the holder of the elements of a search result
     * @param position the position of the element
     */
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ((TextView) holder.mCardView.findViewById(R.id.resultName)).setText(mDataset.get(position).name);
        ((TextView) holder.mCardView.findViewById(R.id.resultCalories)).setText(String.format(Locale.ENGLISH,"%.2f kCal", mDataset.get(position).energy));
        ((TextView) holder.mCardView.findViewById(R.id.resultId)).setText(String.format(Locale.ENGLISH, "%d", mDataset.get(position).id));
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return the size of the dataset
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    /**
     * Add additional items to the dataset
     * @param products the Products to be inserted
     */
    public void addItems(List<Product> products) {
        mDataset.addAll(products);
        notifyItemInserted(mDataset.size());
    }
}