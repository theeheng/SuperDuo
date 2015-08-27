package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by htan on 27/08/2015.
 */
public class A11yPagerTabStrip extends PagerTabStrip {

    private static String LOG_TAG = "A11yPagerTabStrip";
    private Context mContext;
    public A11yPagerTabStrip(Context context) {
        super(context);
        mContext = context;
    }

    public A11yPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @Override
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {

        final ViewPager viewPager = (ViewPager) this.getParent();
        final int itemIndex = viewPager.getCurrentItem();

        String title = viewPager.getAdapter().getPageTitle(itemIndex).toString();

        Resources res = mContext.getResources();

        child.setContentDescription(res.getString(R.string.tabstrip_content_description,title));

        return super.onRequestSendAccessibilityEvent(child, event);
    }
}

