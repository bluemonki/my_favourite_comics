package net.bluemonki.myfavouritecomics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.bluemonki.myfavouritecomics.dummy.ComicStore ;
import net.bluemonki.myfavouritecomics.dummy.Comic;

/**
 * A fragment representing a single Comic detail screen.
 * This fragment is either contained in a {@link ComicListActivity}
 * in two-pane mode (on tablets) or a {@link ComicDetailActivity}
 * on handsets.
 * Implements the OnCheckedChangeListener for the "favourite" checkbox
 */
public class ComicDetailFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Comic mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComicDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ComicStore.getComicByIsbn(getArguments().getString(ARG_ITEM_ID)) ;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.comic_detail)).setText(mItem.getDetails());
            CheckBox favouriteCheckbox = ((CheckBox) rootView.findViewById(R.id.comic_favorite)) ;
            favouriteCheckbox.setChecked(mItem.isFavorite());

            favouriteCheckbox.setOnCheckedChangeListener(ComicDetailFragment.this) ;
        }

        return rootView;
    }


    @Override
    /**
     * Set or Unset a favourite
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mItem.setFavorite( isChecked );
    }
}
