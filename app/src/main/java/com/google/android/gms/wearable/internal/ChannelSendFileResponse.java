/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.wearable.internal.zzs;

public class ChannelSendFileResponse
implements SafeParcelable {
    public static final Parcelable.Creator<ChannelSendFileResponse> CREATOR = new zzs();
    public final int statusCode;
    public final int versionCode;

    ChannelSendFileResponse(int n2, int n3) {
        this.versionCode = n2;
        this.statusCode = n3;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        zzs.zza(this, parcel, n2);
    }
}

