package net.bluemonki.myfavouritecomics;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;

import net.bluemonki.myfavouritecomics.dummy.ComicStore;

/**
 * Some simple tests
 */
public class ComicListActivityTest extends ActivityInstrumentationTestCase2<ComicListActivity>
{
    private ComicListActivity m_comicListActivity ;
    private ComicListFragment m_comicListFragment ;

    public ComicListActivityTest() {
        super(ComicListActivity.class);
    }

    public ComicListActivityTest(Class<ComicListActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        m_comicListActivity = getActivity() ;
        m_comicListFragment = (ComicListFragment) m_comicListActivity.getSupportFragmentManager().findFragmentById(R.id.comic_list) ;
    }

    public void testPreconditions() {
        assertNotNull("m_comicListActivity is null", m_comicListActivity);
        assertNotNull("m_comicListFragment is null", m_comicListFragment);
    }

    // test that we get the right number of items to start with
    public void testListAdapterFirstLoad()
    {
        int items_loaded = m_comicListFragment.getListAdapter().getCount() ;
        assertEquals(items_loaded, 49);
    }

    // test the limit of our lazy loading
    public void testListAdapterScrollABit()
    {
        //ComicStore.getComicByPosition(20) ;
        m_comicListFragment.getListAdapter().getItem(20) ;
        int items_loaded = m_comicListFragment.getListAdapter().getCount() ;
        assertEquals(items_loaded, 49);
    }

    // test that scrolling loads more items
    public void testListAdapterScrollForNextLoad()
    {
        // this doesn't seem to force the getView method to be called
        m_comicListFragment.getView().scrollBy(0, 1000);

        // force a view refresh?
        m_comicListActivity.runOnUiThread(new Runnable() {
            public void run() {
                ((ArrayAdapter) m_comicListFragment.getListAdapter()).notifyDataSetChanged();
            }
        });
        int items_loaded = m_comicListFragment.getListAdapter().getCount() ;
        assertEquals(items_loaded, 99);
    }

    // check that we can load more items as required
    public void testComicStoreLazyLoad()
    {
        ComicStore.getComicByPosition(45) ;

        assertEquals(ComicStore.getComics().size(), 99 ) ;
    }
}
