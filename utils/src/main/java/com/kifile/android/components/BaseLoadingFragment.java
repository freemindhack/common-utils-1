package com.kifile.android.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.kifile.android.utils.R;

/**
 * 支持加载状态展示的Fragment.
 *
 * @author kifile
 */
public abstract class BaseLoadingFragment extends BasePagerFragment {

    // 界面状态类型.
    // 未定义状态
    protected static final int STATUS_INVALIDATE = -1;
    // 加载状态
    protected static final int STATUS_LOADING = 0;
    // 加载成功状态
    protected static final int STATUS_PRIMARY = 1;
    // 加载失败状态
    protected static final int STATUS_ERROR = 2;

    private int mStatus = STATUS_INVALIDATE;

    private ViewStub mErrorStub;

    private View mLoadingView;

    private View mPrimaryView;

    private View mErrorView;

    protected void setStatus(int status) {
        if (mStatus != status) {
            mStatus = status;
            switch (mStatus) {
                case STATUS_LOADING:
                    switchToLoadingView();
                    break;
                case STATUS_PRIMARY:
                    switchToPrimaryView();
                    break;
                case STATUS_ERROR:
                    switchToErrorView();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 切换至加载页面.
     */
    private void switchToLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        if (mPrimaryView != null) {
            mPrimaryView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    /**
     * 切换至主页面.
     */
    private void switchToPrimaryView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mPrimaryView == null && getActivity() != null) {
            ViewGroup group = (ViewGroup) getView();
            mPrimaryView = onCreatePrimaryView(LayoutInflater.from(getActivity()), group);
            group.addView(mPrimaryView);
            onPrimaryViewCreated(mPrimaryView);
            mPrimaryView.setVisibility(View.VISIBLE);

        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    /**
     * 切换至失败页面.
     */
    private void switchToErrorView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mPrimaryView != null) {
            mPrimaryView.setVisibility(View.GONE);
        }
        if (mErrorView == null) {
            mErrorView = mErrorStub.inflate();
            mErrorView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loadDataIfNecessary();
                }
            });
        }
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setStatus(STATUS_INVALIDATE);
        return inflater.inflate(R.layout.layout_loading, container, false);
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingView = view.findViewById(R.id.loading);
        mErrorStub = (ViewStub) view.findViewById(R.id.loading_error_stub);
        mPrimaryView = null;
        mErrorView = null;
    }

    @Override
    protected final void loadDataIfNecessary() {
        // 再次进入时，根据当前数据是否加载成功，选择显示界面
        if (!isLoadingFinished()) {
            setStatus(STATUS_LOADING);
            loadData();
        } else {
            setStatus(STATUS_PRIMARY);
        }
    }

    protected abstract void loadData();

    protected abstract boolean isLoadingFinished();

    protected abstract View onCreatePrimaryView(LayoutInflater inflater, ViewGroup container);

    protected abstract void onPrimaryViewCreated(View view);
}
