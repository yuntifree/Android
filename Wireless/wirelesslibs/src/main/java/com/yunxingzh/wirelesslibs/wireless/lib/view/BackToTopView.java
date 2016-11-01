package com.yunxingzh.wirelesslibs.wireless.lib.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.ref.WeakReference;

/**
 * createdbybruce.zhang
 */
public class BackToTopView extends ImageView {
    private View mScrollView;
    private int mVisiblePosition;

    public BackToTopView(Context context) {
        super(context);
    }

    public int getVisiblePosition() {
        return mVisiblePosition;
    }

    public BackToTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollView instanceof ListView) {
                    ((ListView) mScrollView).setSelection(0);
                } else if (mScrollView instanceof RecyclerView) {
                    ((RecyclerView) mScrollView).scrollToPosition(0);
                }
                setVisibility(View.GONE);
            }
        });
    }

    /**
     * @paramrecyclerView
     * @paramvisiblePosition到哪个位置显示回到顶部按钮
     */
    public void setRecyclerView(RecyclerView recyclerView, int visiblePosition) {
        mVisiblePosition = visiblePosition;
        mScrollView = recyclerView;
        recyclerView.addOnScrollListener(onRecyclerViewScrollListener);
    }

    /**
     * @paramlistView
     * @paramvisiblePosition到哪个位置显示回到顶部按钮
     * @paramonScrollListenerlistView外面设置的OnScrollListener
     */
    public void setListView(AbsListView listView, int visiblePosition, AbsListView.OnScrollListener onScrollListener) {
        mVisiblePosition = visiblePosition;
        mScrollView = listView;
        listView.setOnScrollListener(new BackToTopScrollListener(listView, onScrollListener, BackToTopView.this));
    }

    private RecyclerView.OnScrollListener onRecyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem > mVisiblePosition) {
                    setVisibility(View.VISIBLE);
                } else {
                    setVisibility(View.GONE);
                }
            }
        }
    };

    private static class BackToTopScrollListener implements AbsListView.OnScrollListener {
        private final AbsListView.OnScrollListener externalListener;
        private AbsListView mListView;
        private WeakReference mBackToTopView;

        public BackToTopScrollListener(AbsListView interceptListView, AbsListView.OnScrollListener externalListener, BackToTopView backToTopView) {
            this.mListView = interceptListView;
            this.externalListener = externalListener;
            this.mBackToTopView = new WeakReference<>(backToTopView);
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (this.externalListener != null) {
                this.externalListener.onScrollStateChanged(view, scrollState);
            }
            if (scrollState == SCROLL_STATE_IDLE) {
                int firstVisible = mListView.getFirstVisiblePosition();
                BackToTopView backToTopView = (BackToTopView) mBackToTopView.get();
                if (backToTopView != null) {
                    if (firstVisible > backToTopView.getVisiblePosition()) {
                        backToTopView.setVisibility(View.VISIBLE);
                    } else {
                        backToTopView.setVisibility(View.GONE);
                    }
                }
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (this.externalListener != null) {
                this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }
}