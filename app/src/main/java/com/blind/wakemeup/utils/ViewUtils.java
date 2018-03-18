package com.blind.wakemeup.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ViewUtils {

    /**
     * Get all the views which matches the given Tag recursively
     *
     * @param root parent view. for e.g. Layouts
     * @param tag  tag to look for
     * @return List of views
     */
    public static List<View> findViewsWithTagRecursively(ViewGroup root, Object tag) {
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup) {
                allViews.addAll(findViewsWithTagRecursively((ViewGroup) childView,
                                                            tag));
            } else {
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag)) {
                    allViews.add(childView);
                }
            }
        }

        return allViews;
    }

    /**
     * Get all the views which matches the given Tag recursively
     *
     * @param root parent view. for e.g. Layouts
     * @param tag  tag to look for
     * @return List of views
     */
    public static View findViewWithTagRecursively(ViewGroup root, Object tag) {

        View result = null;
        final int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup) {
                result = findViewWithTagRecursively((ViewGroup) childView,
                                                    tag);
            }

            if(result == null) {
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag)) {
                    return childView;
                }
            } else {
                return result;
            }
        }

        return result;
    }

    @NonNull
    public static Optional<View> findViewByIdRecursively(@NonNull View rootView,
                                                         @IdRes int viewId) {
        List<ViewGroup> searchList = new ArrayList<>();

        if(rootView instanceof ViewGroup) {
            searchList.add((ViewGroup) rootView);
        }

        while(searchList.size() != 0) {
            ViewGroup groupToCheck = searchList.get(0);
            int childCount = groupToCheck.getChildCount();
            for(int i = 0; i < childCount; i++) {
                View child = groupToCheck.getChildAt(i);
                if(child.getId() == viewId) {
                    return Optional.of(child);
                } else if(child instanceof ViewGroup) {
                    searchList.add((ViewGroup) child);
                }
            }
            searchList.remove(0);
        }

        return Optional.empty();
    }

}
