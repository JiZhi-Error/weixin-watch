/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.wearable.internal.zzas;

public class GetFdForAssetResponse
implements SafeParcelable {
    public static final Parcelable.Creator<GetFdForAssetResponse> CREATOR = new zzas();
    public final int statusCode;
    public final int versionCode;
    public final ParcelFileDescriptor zzbsK;

    GetFdForAssetResponse(int n2, int n3, ParcelFileDescriptor parcelFileDescriptor) {
        this.versionCode = n2;
        this.statusCode = n3;
        this.zzbsK = parcelFileDescriptor;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        zzas.zza(this, parcel, n2 | 1);
    }
}

