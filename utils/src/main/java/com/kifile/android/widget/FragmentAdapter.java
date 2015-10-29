package com.kifile.android.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * 用于包裹Fragment的PagerAdapter
 *
 * @author kifile
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    private List<Fragment> mFragments;

    private int[] mTitles;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public void setData(List<Fragment> fragments, int[] titles) {
        mFragments = fragments;
        mTitles = titles;
        if (mFragments == null && mTitles == null) {
            notifyDataSetChanged();
            return;
        } else if (mFragments != null && mTitles != null) {
            if (mFragments.size() == mTitles.length) {
                notifyDataSetChanged();
                return;
            }
        }
        throw new IllegalArgumentException("");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < mTitles.length) {
            return mContext.getResources().getString(mTitles[position]);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof Fragment) {
            if (mFragments != null) {
                int index = mFragments.indexOf(object);
                if (index == -1) {
                    return PagerAdapter.POSITION_NONE;
                }
                return index;
            }
        }
        return PagerAdapter.POSITION_NONE;
    }
}