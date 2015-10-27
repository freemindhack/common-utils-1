package com.kifile.android.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kifile.android.utils.ViewUtils;

/**
 * 界面加载管理器.
 * <p>
 * 负责管理各个界面状态的切换.
 * <p>
 * Created by kifile on 15/10/27.
 */
public class ViewLoaderManager {

    // 界面状态类型.
    // 未定义状态
    protected static final int STATUS_INVALIDATE = -1;
    // 加载状态
    protected static final int STATUS_LOADING = 0;
    // 加载成功状态
    protected static final int STATUS_PRIMARY = 1;
    // 加载失败状态
    protected static final int STATUS_ERROR = 2;

    private ViewGroup mView;

    private View mLoadingView;

    private View mPrimaryView;

    private View mErrorView;

    private final Context mContext;

    private int mStatus = STATUS_INVALIDATE;

    private final Callback mCallback;

    public ViewLoaderManager(Context context, Callback callback) {
        mContext = context;
        if (callback == null) {
            throw new NullPointerException("callback cannot be null.");
        }
        mCallback = callback;
        setStatus(STATUS_INVALIDATE);
    }

    public View inflate() {
        mView = new FrameLayout(mContext);
        mLoadingView = null;
        mPrimaryView = null;
        mErrorView = null;
        return mView;
    }

    public void onResume() {
        if (mView == null) {
            throw new RuntimeException("Should call inflate before onResume.");
        }
        if (!mCallback.isLoadingFinished()) {
            setStatus(STATUS_LOADING);
            mCallback.loadData();
        } else {
            setStatus(STATUS_PRIMARY);
        }
    }

    public void setStatus(int status) {
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
        if (mLoadingView == null) {
            mLoadingView = mCallback.getLoadingView(LayoutInflater.from(mContext), mView);
        }
        mLoadingView.setVisibility(View.VISIBLE);
        ViewUtils.addSingleView(mView, mPrimaryView);
    }

    /**
     * 切换至主页面.
     */
    private void switchToPrimaryView() {
        if (mPrimaryView == null) {
            mPrimaryView = mCallback.getPrimaryView(LayoutInflater.from(mContext), mView);
        }
        mPrimaryView.setVisibility(View.VISIBLE);
        ViewUtils.addSingleView(mView, mPrimaryView);
    }

    /**
     * 切换至失败页面.
     */
    private void switchToErrorView() {
        if (mErrorView == null) {
            mErrorView = mCallback.getErrorView(LayoutInflater.from(mContext), mView);
        }
        mErrorView.setVisibility(View.VISIBLE);
        ViewUtils.addSingleView(mView, mErrorView);
    }

    public interface Callback {

        boolean isLoadingFinished();

        void loadData();

        View getErrorView(LayoutInflater inflater, ViewGroup parent);

        View getLoadingView(LayoutInflater inflater, ViewGroup parent);

        View getPrimaryView(LayoutInflater inflater, ViewGroup parent);
    }
}
