package com.apps.koenig.thomas.koenigsputz.View.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 05.02.2017.
 */
public class HeaderItem extends ListItem {

    public String header;

    public HeaderItem(String header) {
        this.header = header;
    }
    @Override
    public int getType() {
        return ListItem.TYPE_HEADER;
    }

    public static final Parcelable.Creator<HeaderItem> CREATOR
            = new Parcelable.Creator<HeaderItem>() {
        public HeaderItem createFromParcel(Parcel in) {
            return new HeaderItem(in);
        }

        public HeaderItem[] newArray(int size) {
            return new HeaderItem[size];
        }
    };

    private HeaderItem(Parcel in) {
        header = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
    }
}
