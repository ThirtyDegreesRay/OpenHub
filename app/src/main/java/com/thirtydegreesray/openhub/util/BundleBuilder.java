/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.util;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/11 18:07:41
 */

public class BundleBuilder {

    private final Bundle bundle;

    private BundleBuilder() {
        bundle = new Bundle();
    }

    public static BundleBuilder builder() {
        return new BundleBuilder();
    }

    public BundleBuilder put(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, boolean[] value) {
        bundle.putBooleanArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, IBinder value) {
        // Uncommment this line if your minimum sdk version is API level 18
        bundle.putBinder(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, int[] value) {
        bundle.putIntArray(key, value);
        return this;
    }

    public BundleBuilder putIntegerArrayList(@NonNull String key, ArrayList<Integer> value) {
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, Bundle value) {
        bundle.putBundle(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, byte[] value) {
        bundle.putByteArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, String[] value) {
        bundle.putStringArray(key, value);
        return this;
    }

    public BundleBuilder putStringArrayList(@NonNull String key, ArrayList<String> value) {
        bundle.putStringArrayList(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, long[] value) {
        bundle.putLongArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, float[] value) {
        bundle.putFloatArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, char value) {
        bundle.putChar(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, char[] value) {
        bundle.putCharArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, CharSequence[] value) {
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    public BundleBuilder putCharSequenceArrayList(@NonNull String key, ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, double[] value) {
        bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, Parcelable[] value) {
        bundle.putParcelableArray(key, value);
        return this;
    }

    public BundleBuilder putParcelableArrayList(@NonNull String key, ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    public BundleBuilder putSparseParcelableArray(@NonNull String key, SparseArray<? extends Parcelable> value) {
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, short value) {
        bundle.putShort(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, short[] value) {
        bundle.putShortArray(key, value);
        return this;
    }

    public BundleBuilder put(@NonNull String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleBuilder putAll(Bundle map) {
        bundle.putAll(map);
        return this;
    }

    /**
     * Get the underlying build.
     */
    public Bundle get() {
        return bundle;
    }

    @NonNull public Bundle build() {
        Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        int size = parcel.dataSize();
        if (size > 500000) {
            bundle.clear();
        }
        return get();
    }

}
