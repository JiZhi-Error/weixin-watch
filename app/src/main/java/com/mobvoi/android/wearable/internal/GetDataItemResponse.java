/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.mobvoi.android.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.mobvoi.android.common.internal.safeparcel.SafeParcelable;
import com.mobvoi.android.wearable.internal.DataItemParcelable;
import mobvoiapi.bn;
import mobvoiapi.bo;

public class GetDataItemResponse
implements SafeParcelable {
    public static final Parcelable.Creator<GetDataItemResponse> CREATOR = new a();
    public final int a;
    public final int b;
    public final DataItemParcelable c;

    public GetDataItemResponse(int n2, int n3, DataItemParcelable dataItemParcelable) {
        this.a = n2;
        this.b = n3;
        this.c = dataItemParcelable;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        com.mobvoi.android.wearable.internal.GetDataItemResponse$a.a(this, parcel, n2);
    }

    public static class a
    implements Parcelable.Creator<GetDataItemResponse> {
        public static void a(GetDataItemResponse getDataItemResponse, Parcel parcel, int n2) {
            int n3 = bo.a(parcel);
            bo.a(parcel, 1, getDataItemResponse.a);
            bo.a(parcel, 2, getDataItemResponse.b);
            bo.a(parcel, 3, getDataItemResponse.c, n2, false);
            bo.a(parcel, n3);
        }

        public GetDataItemResponse a(Parcel parcel) {
            int n2 = 0;
            int n3 = bn.b(parcel);
            DataItemParcelable dataItemParcelable = null;
            int n4 = 0;
            block5: while (parcel.dataPosition() < n3) {
                int n5 = bn.a(parcel);
                switch (bn.a(n5)) {
                    default: {
                        bn.b(parcel, n5);
                        continue block5;
                    }
                    case 1: {
                        n4 = bn.c(parcel, n5);
                        continue block5;
                    }
                    case 2: {
                        n2 = bn.c(parcel, n5);
                        continue block5;
                    }
                    case 3: 
                }
                dataItemParcelable = bn.a(parcel, n5, DataItemParcelable.CREATOR);
            }
            if (parcel.dataPosition() != n3) {
                throw new RuntimeException("parcel size exceeded. index = " + n3 + ", parcel = " + parcel);
            }
            return new GetDataItemResponse(n4, n2, dataItemParcelable);
        }

        public GetDataItemResponse[] a(int n2) {
            return new GetDataItemResponse[n2];
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return this.a(parcel);
        }

        public /* synthetic */ Object[] newArray(int n2) {
            return this.a(n2);
        }
    }
}

