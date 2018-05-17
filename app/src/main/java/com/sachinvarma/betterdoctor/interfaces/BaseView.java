package com.sachinvarma.betterdoctor.interfaces;

public interface BaseView {

    void init();

    /**
     * Dismiss the Progressbar
     */
    void dismissPb();

    /**
     * Showing the Progressbar
     */

    void showPb();

    /**
     * Common function for setting toast message
     * @param message message to show
     */
    void showToast(String message);

    /**
     * Function to pass data to base function when there is any failure
     * @param call Call<?> eg - Call<PreferenceModel>
     * @param throwable Throwable
     * @param callback CallBack object to make retry call again
     * @param additionalData Object containing any additional data to be passed back to the view
     */
    void onFailure(
      final Object call, final Object throwable, final Object callback, final Object additionalData
    );

    /**
     * Initialisation of components
     */


    /**
     *  Retry fialog in case of failure of API call
     * @param object1
     * @param object3
     * @param message
     */
    void showRetryDialog(final Object object1, final Object object3, final String message);
}
