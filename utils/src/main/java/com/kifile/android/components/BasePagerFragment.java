package com.kifile.android.components;

import android.support.v4.app.Fragment;

/**
 * 用于在ViewPager中使用的Fragment，主要作用是在Fragment真正加载到界面上才进行数据加载.
 *
 * @author kifile
 */
public abstract class BasePagerFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            loadDataIfNecessary();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            loadDataIfNecessary();
        }
    }

    /**
     * 在Fragment真正显示到界面上时进行数据加载.
     */
    protected abstract void loadDataIfNecessary();
}
