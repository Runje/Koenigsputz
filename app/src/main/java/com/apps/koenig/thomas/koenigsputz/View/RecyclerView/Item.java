package com.apps.koenig.thomas.koenigsputz.View.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.communication.model.CleanTask;
import com.example.communication.model.communication.MessageConverter;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 05.02.2017.
 */
public class Item extends ListItem {

    public CleanTask cleanTask;

    public Item(CleanTask cleanTask) {
        this.cleanTask = cleanTask;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_ITEM;
    }


    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in) {
        int n = in.readInt();
        byte[] bytes = new byte[n];
        in.readByteArray(bytes);
        cleanTask = MessageConverter.byteToCleanTask(ByteBuffer.wrap(bytes));
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        byte[] bytes = MessageConverter.cleanTaskToBytes(cleanTask);
        dest.writeInt(bytes.length);
        dest.writeByteArray(bytes);
    }
}
