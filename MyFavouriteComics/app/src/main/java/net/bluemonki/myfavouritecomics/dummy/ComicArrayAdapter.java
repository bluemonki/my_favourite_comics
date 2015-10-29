package net.bluemonki.myfavouritecomics.dummy;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.bluemonki.myfavouritecomics.ComicListActivity;

import java.util.List;

/**
 * Extends the simple array adapter to deal with comics
 */
public class ComicArrayAdapter extends ArrayAdapter
{
    public ComicArrayAdapter(Context context, int resource, int textViewResourceId, List objects)
    {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView textView = (TextView) super.getView(position, convertView, parent);

        // load the comic at this position (this may also lazy load some more comics)
        Comic comic = ComicStore.getComicByPosition( position ) ;

        // make sure the ArrayAdapter is OK with it's data changing from loading more
        // comics
        notifyDataSetChanged();


        if ( null != comic )
        {
            // mark a favourite if required
            if (true == comic.isFavorite()) {
                textView.setTextColor(Color.YELLOW);
                textView.setBackgroundColor(Color.RED);
            }

            return textView;
        }
        return null ;
    }


}
