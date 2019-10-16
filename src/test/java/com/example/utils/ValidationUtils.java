package com.example.utils;

import com.sun.istack.internal.Nullable;

public class ValidationUtils {
    /**
     * Checks is string is null or empty.
     *
     * @param text text
     * @return empty
     */
    public static boolean isEmpty(@Nullable CharSequence text) {
        return length(text) == 0;
    }

    /**
     * Returns text length.
     *
     * @param text text
     * @return length.
     */
    public static int length(@Nullable CharSequence text) {
        return text == null ? 0 : text.length();
    }


}
