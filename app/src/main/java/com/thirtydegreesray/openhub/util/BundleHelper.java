

package com.thirtydegreesray.openhub.util;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 2017/10/30 9:42:40
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

public class BundleHelper {

    private Bundle bundle;

    private BundleHelper() {
        bundle = new Bundle();
    }

    public static BundleHelper builder() {
        return new BundleHelper();
    }
    
    //constant
    public BundleHelper put(@NonNull String key, @Nullable String value){
        bundle.putString(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable char value){
        bundle.putChar(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable int value){
        bundle.putInt(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable boolean value){
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable long value){
        bundle.putLong(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable float value){
        bundle.putFloat(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable short value){
        bundle.putShort(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable double value){
        bundle.putDouble(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable byte value){
        bundle.putByte(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable CharSequence value){
        bundle.putCharSequence(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Parcelable value){
        bundle.putParcelable(key, value);
        return this;
    }

    //array
    public BundleHelper put(@NonNull String key, @Nullable String[] value){
        bundle.putStringArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable char[] value){
        bundle.putCharArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable int[] value){
        bundle.putIntArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable boolean[] value){
        bundle.putBooleanArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable long[] value){
        bundle.putLongArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable float[] value){
        bundle.putFloatArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable short[] value){
        bundle.putShortArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable double[] value){
        bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable byte[] value){
        bundle.putByteArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable CharSequence[] value){
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Parcelable[] value){
        bundle.putParcelableArray(key, value);
        return this;
    }

    //ArrayList
    public  BundleHelper putStringList(@NonNull String key, @Nullable ArrayList<String> value){
        bundle.putStringArrayList(key, value);
        return this;
    }

    public  BundleHelper putIntegerList(@NonNull String key, @Nullable ArrayList<Integer> value){
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public  BundleHelper putParcelableList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value){
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    public  BundleHelper putCharSequenceList(@NonNull String key, @Nullable ArrayList<CharSequence> value){
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable android.util.Size value){
        bundle.putSize(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable SizeF value){
        bundle.putSizeF(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable IBinder value){
        bundle.putBinder(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Serializable value){
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value){
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public Bundle build(){
        Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        if (parcel.dataSize() > 512 * 1024) {
            bundle.clear();
            throw new IllegalArgumentException("bundle data is too large, please reduce date size to avoid Exception");
        }
        return bundle;
    }

}
