package com.apps.koenig.thomas.koenigsputz.View.RecyclerView;

import android.os.Parcelable;

/**
 * Created by Thomas on 05.02.2017.
 */

public abstract class ListItem implements Parcelable {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    abstract public int getType();
}
