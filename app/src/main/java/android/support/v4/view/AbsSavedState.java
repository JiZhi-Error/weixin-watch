/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.support.v4.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

public abstract class AbsSavedState
implements Parcelable {
    public static final Parcelable.Creator<AbsSavedState> CREATOR;
    public static final AbsSavedState EMPTY_STATE;
    private final Parcelable mSuperState;

    static {
        EMPTY_STATE = new AbsSavedState(){};
        CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<AbsSavedState>(){

            @Override
            public AbsSavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                if (parcel.readParcelable(classLoader) != null) {
                    throw new IllegalStateException("superState must be null");
                }
                return EMPTY_STATE;
            }

            public AbsSavedState[] newArray(int n2) {
                return new AbsSavedState[n2];
            }
        });
    }

    private AbsSavedState() {
        this.mSuperState = null;
    }

    protected AbsSavedState(Parcel parcel) {
        this(parcel, null);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    protected AbsSavedState(Parcel object, ClassLoader classLoader) {
        void var1_3;
        void var2_5;
        Parcelable parcelable = object.readParcelable((ClassLoader)var2_5);
        if (parcelable == null) {
            AbsSavedState absSavedState = EMPTY_STATE;
        }
        this.mSuperState = var1_3;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected AbsSavedState(Parcelable parcelable) {
        if (parcelable == null) {
            throw new IllegalArgumentException("superState must not be null");
        }
        if (parcelable == EMPTY_STATE) {
            parcelable = null;
        }
        this.mSuperState = parcelable;
    }

    public int describeContents() {
        return 0;
    }

    public final Parcelable getSuperState() {
        return this.mSuperState;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeParcelable(this.mSuperState, n2);
    }
}

