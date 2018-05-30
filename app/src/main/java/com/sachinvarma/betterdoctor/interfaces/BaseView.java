package com.sachinvarma.betterdoctor.interfaces;


import android.support.annotation.NonNull;

/**
 * Base class for View interfaces, contains functions which should be added to add views
 */
public interface BaseView {

    /**
     * Function to return error.
     * @param error String containing error
     */
    void errorOccurred(@NonNull final String error);

}
