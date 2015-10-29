package net.bluemonki.myfavouritecomics.dummy;

/**
 * Created by john on 21/10/15.
 */
import android.content.Context;
import android.content.res.Resources;

import net.bluemonki.myfavouritecomics.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class ComicStore
{
    public ComicStore(Context a_context)
    {
        try
        {
            // open the records.csv file containing all the information
            Resources res               = a_context.getResources();
            InputStream in_s            = res.openRawResource(R.raw.records);
            m_inputReader               = new BufferedReader(new InputStreamReader(in_s));

            // read the first batch of comics
            readComics() ;

            // store a reference to ourselves so that we can get this class statically
            m_self = this ;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * readComics()
     *
     * Reads a number of comics from the input reader defined by m_lineBatchSize
     *
     * @return true if we read some comics
     */
    private static boolean readComics()
    {
        try
        {
            // how many lines have we read this time
            int linesRead           = 0 ;
            // where did we get to last time
            int previousLinePointer = m_linePointer ;
            // temporary file line storage
            String line             = null;
            // how to split the CSV content
            String split_string     = "\",\"" ;

            // while there are lines to read
            while ((line = m_inputReader.readLine()) != null)
            {
                linesRead++ ;

                // need to catchup to where we were before
                if ( linesRead < m_linePointer ) { continue ; }

                // split the CSV line up
                String[] pieces = line.split( split_string );

                // update our global line pointer
                m_linePointer++;

                // if this is the first line then it contains the column names..
                if (1 == m_linePointer) {
                    // this is the first line
                    m_columnNames = pieces;
                    continue;
                }

                // this must be a record
                // construct a comic out of it
                Comic new_comic = new Comic(m_columnNames, pieces);

                // add the comic to the relevant indexes
                m_comics.add(new_comic);
                addComicByPublisher(new_comic);
                m_comicsByIsbn.put(new_comic.getIsbn(), new_comic);


                // check if we should stop reading
                if (m_lineBatchSize == ( m_linePointer - previousLinePointer)) {
                    // done reading this batch
                   break ;
                }
            }
            // end while

            if ( null == line )
            {
                // dropped out of the while loop because we ran out
                // of lines to read
                return false ;
            }

            // dropped out of the while loop before we hit
            // 50 items, but we still managed to read something
            // so return true
            return true ;
        }
        catch( Exception ioe )
        {
            ioe.printStackTrace();
            return false ;
        }
    }


    /**
     * Add a comic to the store indexed by the publisher
     *
     * @param a_comicToAdd the comic to add
     */
    private static void addComicByPublisher( Comic a_comicToAdd )
    {
        // if we already have a space for this publisher just add the comic
        if (m_comicsByPublisher.containsKey(a_comicToAdd.getPublisher())) {
            m_comicsByPublisher.get(a_comicToAdd.getPublisher()).add(a_comicToAdd);
        }
        // otherwise we need to construct the vector for it
        else {
            Vector comic = new Vector<Comic>() ;
            comic.add(a_comicToAdd) ;
            m_comicsByPublisher.put(a_comicToAdd.getPublisher(), comic) ;
        }
    }

    /**
     * Getter for our indexed comic store
     * @return
     */
    public static Vector<Comic> getComics()
    {
        return m_comics ;
    }

    /**
     * Getter for getting  the comics by this publisher
     *
     * @param a_publisher
     * @return a vector of comics by this publisher
     */
    public static Vector<Comic> getComicsByThisPublisher( String a_publisher )
    {
        return m_comicsByPublisher.get(a_publisher) ;
    }


    /**
     * Find a comic by it's ISBN and return it
     *
     * @param a_isbn
     * @return
     */
    public static Comic getComicByIsbn( String a_isbn )
    {
        return m_comicsByIsbn.get(a_isbn) ;
    }

    /**
     * Static getter for this object
     * @return
     */
    public static ComicStore getComicStore()
    {
        return m_self ;
    }

    /**
     * getComicByPosition
     *
     * used by the ComicArrayAdapter -- performs a simple kind of
     * lazy loading of the next batch (m_lineBatchSize) of comics
     * when we're a certain distance (m_lineLoadPerimeter) from
     * the current end of the list
     *
     * @param a_position
     * @return
     */
    public static Comic getComicByPosition( int a_position )
    {
        if ( Math.abs( m_comics.size() - a_position ) <= m_lineLoadPerimeter )
        {
            readComics() ;
        }

        if ( a_position < m_comics.size() ) {
            return m_comics.get(a_position);
        }
        return null ;
    }

    // reference to ourselves
    private static ComicStore m_self  = null ;

    // members for reading the CSV file
    private static int m_linePointer            = 0 ;
    private static int m_lineBatchSize          = 50 ;
    private static int m_lineLoadPerimeter      = 10 ;
    private static String[] m_columnNames       = null ;
    private static BufferedReader m_inputReader = null ;

    // storage for comics
    private static Vector<Comic> m_comics                               = new Vector<Comic> () ;
    private static HashMap<String, Vector<Comic>> m_comicsByPublisher   = new HashMap<String, Vector<Comic>>() ;
    private static HashMap<String, Comic> m_comicsByIsbn                = new HashMap<String, Comic>() ;

}
