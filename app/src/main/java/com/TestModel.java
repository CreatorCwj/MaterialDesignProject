package com;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cwj on 17/4/21.
 */

public class TestModel implements Parcelable {

    public int a;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.a);
    }

    public TestModel() {
    }

    public TestModel(int a) {
        this.a = a;
    }

    protected TestModel(Parcel in) {
        this.a = in.readInt();
    }

    public static final Creator<TestModel> CREATOR = new Creator<TestModel>() {
        @Override
        public TestModel createFromParcel(Parcel source) {
            return new TestModel(source);
        }

        @Override
        public TestModel[] newArray(int size) {
            return new TestModel[size];
        }
    };
}
