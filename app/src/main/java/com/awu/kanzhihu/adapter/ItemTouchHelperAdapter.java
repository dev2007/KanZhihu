package com.awu.kanzhihu.adapter;

/**
 * Created by yoyo on 2016/1/15.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
