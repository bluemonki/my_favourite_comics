package net.bluemonki.myfavouritecomics.dummy;

import java.util.HashMap;

/**
 * Simple class representing a comic.
 */
public class Comic
{

    /**
     * Create a comic
     *
     * @param a_columnNames String array of the column names from the CSV
     * @param a_columnValues String array of the values of this CSV line
     *
     */
    public Comic(String[] a_columnNames, String[] a_columnValues )
    {
        int i = 0 ;
        for (String key : a_columnNames) { m_record.put(key, a_columnValues[i++]); }
    }

    /**
     * Used for the list pane view
     * @return
     */
    public String toString()
    {
        return this.getTitle() + " (" + this.m_record.get("Date of publication") + ")";
    }

    /**
     * Get the title of the comic
     */
    public String getTitle()
    {
        return m_record.get("Title") ;
    }

    /**
     * get the id of this comic, which is really it's ISBN
     * @return
     */
    public String getId()
    {
        return getIsbn() ;
    }

    /**
     * Get this comic's ISBN
     * @return
     */
    public String getIsbn()
    {
        return m_record.get("ISBN") ;
    }

    /**
     * Get this comic's publisher
     * @return
     */
    public String getPublisher()
    {
        return m_record.get("Publisher") ;
    }

    /**
     * Get details, used in the detail view pane
     * @return
     */
    public String getDetails()
    {
        String details  = "" ;
        details         += "Title: "        + getTitle() + "\n" ;
        details         += "Publisher: " + m_record.get("Publisher") + "\n" ;
        details         += "Published Date: " + m_record.get("Date of publication") + "\n" ;
        details         += "\n" ;
        details         += "There are " + ComicStore.getComicsByThisPublisher(this.getPublisher()) + " titles by this Publisher" ;
        return details ;
    }

    /**
     * Getter for seeing if the comic is a favourite
     *
     * @return
     */
    public boolean isFavorite()
    {
        return m_isFavorite ;
    }

    /**
     * Setter for this comic being a favourite
     *
     * @param a_favourite
     */
    public void setFavorite(boolean a_favourite)
    {
        m_isFavorite = a_favourite ;
    }

    // is a favourite?
    public boolean m_isFavorite = false ;

    // csv line store indexed by column name
    private HashMap<String, String> m_record = new HashMap<String, String>() ;

}
