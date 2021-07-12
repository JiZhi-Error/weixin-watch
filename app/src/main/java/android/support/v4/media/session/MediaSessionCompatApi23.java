/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.os.Bundle
 */
package android.support.v4.media.session;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompatApi21;

class MediaSessionCompatApi23 {
    MediaSessionCompatApi23() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static interface Callback
    extends MediaSessionCompatApi21.Callback {
        public void onPlayFromUri(Uri var1, Bundle var2);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaSessionCompatApi21.CallbackProxy<T> {
        public CallbackProxy(T t2) {
            super(t2);
        }

        public void onPlayFromUri(Uri uri, Bundle bundle) {
            ((Callback)this.mCallback).onPlayFromUri(uri, bundle);
        }
    }
}

