package com.yunxingzh.wireless.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by carey on 2016/7/21 0021.
 */
public final class SelectorUtils {

    /**
     * 设置选中非选中选择器
     *
     * @param context
     * @param idNormal
     * @param iChecked
     * @return
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int iChecked) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable checked = iChecked == -1 ? null : context.getResources().getDrawable(iChecked);
        bg.addState(new int[]{android.R.attr.state_checked}, checked);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 设置背景selector
     *
     * @param context
     * @param idNormal  默认状态，没有传-1
     * @param idPressed 按下状态，没有传-1
     * @param idUnable  不可用状态，没有传递-1
     * @return
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);

        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        bg.addState(new int[]{android.R.attr.state_focused}, pressed);
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 设置背景selector
     *
     * @param context
     * @param idNormal  默认状态，没有传-1
     * @param idPressed 按下状态，没有传-1
     * @param idFocused 焦点状态，没有传-1
     * @param idUnable  不可用状态，没有传递-1
     * @return
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);

        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 设置文字selector
     *
     * @param normal
     * @param pressed
     * @param focused
     * @param unable
     * @return
     */
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
}
