package com.apps.koenig.thomas.koenigsputz.Presenter;

import android.support.annotation.StringRes;

import com.apps.koenig.thomas.koenigsputz.View.Overview;

/**
 * Created by Thomas on 10.11.2016.
 */

public interface OverviewPresenter {
    void started();

    void enteredName(String name);

    void importFile();

    void clickedFab();

    void attachView(Overview overview);

    void detachView();

    void askForNameOrImportFile();

    void sayHello(String name);

    void showError(@StringRes int enter_name);

    void setConnected(boolean connected);

    void clickedPage(Pages page);
}
