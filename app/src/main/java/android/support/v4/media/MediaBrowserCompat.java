/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.Binder
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Message
 *  android.os.Messenger
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaBrowserCompatApi21;
import android.support.v4.media.MediaBrowserCompatApi23;
import android.support.v4.media.MediaBrowserCompatApi24;
import android.support.v4.media.MediaBrowserCompatUtils;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MediaBrowserCompat {
    static final boolean DEBUG = Log.isLoggable((String)"MediaBrowserCompat", (int)3);
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;

    public MediaBrowserCompat(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 24 || BuildCompat.isAtLeastN()) {
            this.mImpl = new MediaBrowserImplApi24(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaBrowserImplApi23(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaBrowserImplApi21(context, componentName, connectionCallback, bundle);
            return;
        }
        this.mImpl = new MediaBrowserImplBase(context, componentName, connectionCallback, bundle);
    }

    public void connect() {
        this.mImpl.connect();
    }

    public void disconnect() {
        this.mImpl.disconnect();
    }

    @Nullable
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    public void getItem(@NonNull String string2, @NonNull ItemCallback itemCallback) {
        this.mImpl.getItem(string2, itemCallback);
    }

    @NonNull
    public String getRoot() {
        return this.mImpl.getRoot();
    }

    @NonNull
    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }

    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isConnected() {
        return this.mImpl.isConnected();
    }

    public void subscribe(@NonNull String string2, @NonNull Bundle bundle, @NonNull SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("options are null");
        }
        this.mImpl.subscribe(string2, bundle, subscriptionCallback);
    }

    public void subscribe(@NonNull String string2, @NonNull SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        this.mImpl.subscribe(string2, null, subscriptionCallback);
    }

    public void unsubscribe(@NonNull String string2) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        this.mImpl.unsubscribe(string2, null);
    }

    public void unsubscribe(@NonNull String string2, @NonNull SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        this.mImpl.unsubscribe(string2, subscriptionCallback);
    }

    private static class CallbackHandler
    extends Handler {
        private final WeakReference<MediaBrowserServiceCallbackImpl> mCallbackImplRef;
        private WeakReference<Messenger> mCallbacksMessengerRef;

        CallbackHandler(MediaBrowserServiceCallbackImpl mediaBrowserServiceCallbackImpl) {
            this.mCallbackImplRef = new WeakReference<MediaBrowserServiceCallbackImpl>(mediaBrowserServiceCallbackImpl);
        }

        public void handleMessage(Message message) {
            if (this.mCallbacksMessengerRef == null || this.mCallbacksMessengerRef.get() == null || this.mCallbackImplRef.get() == null) {
                return;
            }
            Bundle bundle = message.getData();
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
            switch (message.what) {
                default: {
                    Log.w((String)MediaBrowserCompat.TAG, (String)("Unhandled message: " + message + "\n  Client version: " + 1 + "\n  Service version: " + message.arg1));
                    return;
                }
                case 1: {
                    ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onServiceConnected((Messenger)this.mCallbacksMessengerRef.get(), bundle.getString("data_media_item_id"), (MediaSessionCompat.Token)bundle.getParcelable("data_media_session_token"), bundle.getBundle("data_root_hints"));
                    return;
                }
                case 2: {
                    ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onConnectionFailed((Messenger)this.mCallbacksMessengerRef.get());
                    return;
                }
                case 3: 
            }
            ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onLoadChildren((Messenger)this.mCallbacksMessengerRef.get(), bundle.getString("data_media_item_id"), bundle.getParcelableArrayList("data_media_item_list"), bundle.getBundle("data_options"));
        }

        void setCallbacksMessenger(Messenger messenger) {
            this.mCallbacksMessengerRef = new WeakReference<Messenger>(messenger);
        }
    }

    public static class ConnectionCallback {
        ConnectionCallbackInternal mConnectionCallbackInternal;
        final Object mConnectionCallbackObj;

        public ConnectionCallback() {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback(new StubApi21());
                return;
            }
            this.mConnectionCallbackObj = null;
        }

        public void onConnected() {
        }

        public void onConnectionFailed() {
        }

        public void onConnectionSuspended() {
        }

        void setInternalConnectionCallback(ConnectionCallbackInternal connectionCallbackInternal) {
            this.mConnectionCallbackInternal = connectionCallbackInternal;
        }

        static interface ConnectionCallbackInternal {
            public void onConnected();

            public void onConnectionFailed();

            public void onConnectionSuspended();
        }

        private class StubApi21
        implements MediaBrowserCompatApi21.ConnectionCallback {
            StubApi21() {
            }

            @Override
            public void onConnected() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
                }
                ConnectionCallback.this.onConnected();
            }

            @Override
            public void onConnectionFailed() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
                }
                ConnectionCallback.this.onConnectionFailed();
            }

            @Override
            public void onConnectionSuspended() {
                if (ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
                }
                ConnectionCallback.this.onConnectionSuspended();
            }
        }
    }

    public static abstract class ItemCallback {
        final Object mItemCallbackObj;

        public ItemCallback() {
            if (Build.VERSION.SDK_INT >= 23) {
                this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback(new StubApi23());
                return;
            }
            this.mItemCallbackObj = null;
        }

        public void onError(@NonNull String string2) {
        }

        public void onItemLoaded(MediaItem mediaItem) {
        }

        private class StubApi23
        implements MediaBrowserCompatApi23.ItemCallback {
            StubApi23() {
            }

            @Override
            public void onError(@NonNull String string2) {
                ItemCallback.this.onError(string2);
            }

            @Override
            public void onItemLoaded(Parcel parcel) {
                parcel.setDataPosition(0);
                MediaItem mediaItem = (MediaItem)MediaItem.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                ItemCallback.this.onItemLoaded(mediaItem);
            }
        }
    }

    private static class ItemReceiver
    extends ResultReceiver {
        private final ItemCallback mCallback;
        private final String mMediaId;

        ItemReceiver(String string2, ItemCallback itemCallback, Handler handler) {
            super(handler);
            this.mMediaId = string2;
            this.mCallback = itemCallback;
        }

        @Override
        protected void onReceiveResult(int n2, Bundle bundle) {
            bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            if (n2 != 0 || bundle == null || !bundle.containsKey("media_item")) {
                this.mCallback.onError(this.mMediaId);
                return;
            }
            if ((bundle = bundle.getParcelable("media_item")) == null || bundle instanceof MediaItem) {
                this.mCallback.onItemLoaded((MediaItem)bundle);
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }

    static interface MediaBrowserImpl {
        public void connect();

        public void disconnect();

        @Nullable
        public Bundle getExtras();

        public void getItem(@NonNull String var1, @NonNull ItemCallback var2);

        @NonNull
        public String getRoot();

        public ComponentName getServiceComponent();

        @NonNull
        public MediaSessionCompat.Token getSessionToken();

        public boolean isConnected();

        public void subscribe(@NonNull String var1, Bundle var2, @NonNull SubscriptionCallback var3);

        public void unsubscribe(@NonNull String var1, SubscriptionCallback var2);
    }

    static class MediaBrowserImplApi21
    implements MediaBrowserImpl,
    MediaBrowserServiceCallbackImpl,
    ConnectionCallback.ConnectionCallbackInternal {
        protected final Object mBrowserObj;
        protected Messenger mCallbacksMessenger;
        protected final CallbackHandler mHandler = new CallbackHandler(this);
        protected final Bundle mRootHints;
        protected ServiceBinderWrapper mServiceBinderWrapper;
        private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();

        /*
         * Enabled aggressive block sorting
         */
        public MediaBrowserImplApi21(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle object) {
            if (Build.VERSION.SDK_INT < 25) {
                Bundle bundle = object;
                if (object == null) {
                    bundle = new Bundle();
                }
                bundle.putInt("extra_client_version", 1);
                this.mRootHints = new Bundle(bundle);
            } else {
                object = object == null ? null : new Bundle(object);
                this.mRootHints = object;
            }
            connectionCallback.setInternalConnectionCallback(this);
            this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(context, componentName, connectionCallback.mConnectionCallbackObj, this.mRootHints);
        }

        @Override
        public void connect() {
            MediaBrowserCompatApi21.connect(this.mBrowserObj);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void disconnect() {
            if (this.mServiceBinderWrapper != null && this.mCallbacksMessenger != null) {
                try {
                    this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
                }
                catch (RemoteException remoteException) {
                    Log.i((String)MediaBrowserCompat.TAG, (String)"Remote error unregistering client messenger.");
                }
            }
            MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
        }

        @Override
        @Nullable
        public Bundle getExtras() {
            return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
        }

        @Override
        public void getItem(final @NonNull String string2, final @NonNull ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)string2)) {
                throw new IllegalArgumentException("mediaId is empty");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null");
            }
            if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                Log.i((String)MediaBrowserCompat.TAG, (String)"Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
            if (this.mServiceBinderWrapper == null) {
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
            ItemReceiver itemReceiver = new ItemReceiver(string2, itemCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.getMediaItem(string2, itemReceiver, this.mCallbacksMessenger);
                return;
            }
            catch (RemoteException remoteException) {
                Log.i((String)MediaBrowserCompat.TAG, (String)("Remote error getting media item: " + string2));
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
        }

        @Override
        @NonNull
        public String getRoot() {
            return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
        }

        @Override
        public ComponentName getServiceComponent() {
            return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
        }

        @Override
        @NonNull
        public MediaSessionCompat.Token getSessionToken() {
            return MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
        }

        @Override
        public boolean isConnected() {
            return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void onConnected() {
            Bundle bundle = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
            if (bundle == null || (bundle = BundleCompat.getBinder(bundle, "extra_messenger")) == null) {
                return;
            }
            this.mServiceBinderWrapper = new ServiceBinderWrapper((IBinder)bundle, this.mRootHints);
            this.mCallbacksMessenger = new Messenger((Handler)this.mHandler);
            this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
            try {
                this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
                return;
            }
            catch (RemoteException remoteException) {
                Log.i((String)MediaBrowserCompat.TAG, (String)"Remote error registering client messenger.");
                return;
            }
        }

        @Override
        public void onConnectionFailed() {
        }

        @Override
        public void onConnectionFailed(Messenger messenger) {
        }

        @Override
        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onLoadChildren(Messenger object, String string2, List list, Bundle bundle) {
            if (this.mCallbacksMessenger != object) return;
            object = (Subscription)this.mSubscriptions.get(string2);
            if (object == null) {
                if (!DEBUG) return;
                Log.d((String)MediaBrowserCompat.TAG, (String)("onLoadChildren for id that isn't subscribed id=" + string2));
                return;
            }
            if ((object = ((Subscription)object).getCallback(bundle)) == null) {
                return;
            }
            if (bundle == null) {
                ((SubscriptionCallback)object).onChildrenLoaded(string2, list);
                return;
            }
            ((SubscriptionCallback)object).onChildrenLoaded(string2, list, bundle);
        }

        @Override
        public void onServiceConnected(Messenger messenger, String string2, MediaSessionCompat.Token token, Bundle bundle) {
        }

        @Override
        public void subscribe(@NonNull String string2, Bundle bundle, @NonNull SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            Subscription subscription2 = subscription = (Subscription)this.mSubscriptions.get(string2);
            if (subscription == null) {
                subscription2 = new Subscription();
                this.mSubscriptions.put(string2, subscription2);
            }
            subscriptionCallback.setSubscription(subscription2);
            subscription2.putCallback(bundle, subscriptionCallback);
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, string2, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            try {
                this.mServiceBinderWrapper.addSubscription(string2, subscriptionCallback.mToken, bundle, this.mCallbacksMessenger);
                return;
            }
            catch (RemoteException remoteException) {
                Log.i((String)MediaBrowserCompat.TAG, (String)("Remote error subscribing media item: " + string2));
                return;
            }
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void unsubscribe(@NonNull String var1_1, SubscriptionCallback var2_2) {
            block10: {
                block11: {
                    var4_3 = (Subscription)this.mSubscriptions.get(var1_1);
                    if (var4_3 == null) ** GOTO lbl8
                    if (this.mServiceBinderWrapper != null) break block10;
                    if (var2_2 != null) break block11;
                    MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1_1);
lbl6:
                    // 6 sources

                    while (true) {
                        block12: {
                            if (var4_3.isEmpty() || var2_2 == null) break block12;
lbl8:
                            // 2 sources

                            return;
                        }
                        this.mSubscriptions.remove(var1_1);
                        return;
                    }
                }
                var5_4 = var4_3.getCallbacks();
                var6_7 = var4_3.getOptionsList();
                for (var3_9 = var5_4.size() - 1; var3_9 >= 0; --var3_9) {
                    if (var5_4.get(var3_9) != var2_2) continue;
                    var5_4.remove(var3_9);
                    var6_7.remove(var3_9);
                }
                if (var5_4.size() != 0) ** GOTO lbl6
                MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, var1_1);
                ** GOTO lbl6
            }
            if (var2_2 != null) ** GOTO lbl-1000
            try {
                this.mServiceBinderWrapper.removeSubscription(var1_1, null, this.mCallbacksMessenger);
            }
            catch (RemoteException var5_5) {
                Log.d((String)"MediaBrowserCompat", (String)("removeSubscription failed with RemoteException parentId=" + var1_1));
            }
            ** GOTO lbl6
lbl-1000:
            // 1 sources

            {
                var5_6 = var4_3.getCallbacks();
                var6_8 = var4_3.getOptionsList();
                var3_10 = var5_6.size() - 1;
                while (true) {
                    if (var3_10 >= 0) ** break;
                    ** continue;
                    if (var5_6.get(var3_10) == var2_2) {
                        this.mServiceBinderWrapper.removeSubscription(var1_1, SubscriptionCallback.access$000(var2_2), this.mCallbacksMessenger);
                        var5_6.remove(var3_10);
                        var6_8.remove(var3_10);
                    }
                    --var3_10;
                }
            }
        }
    }

    static class MediaBrowserImplApi23
    extends MediaBrowserImplApi21 {
        public MediaBrowserImplApi23(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }

        @Override
        public void getItem(@NonNull String string2, @NonNull ItemCallback itemCallback) {
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi23.getItem(this.mBrowserObj, string2, itemCallback.mItemCallbackObj);
                return;
            }
            super.getItem(string2, itemCallback);
        }
    }

    static class MediaBrowserImplApi24
    extends MediaBrowserImplApi23 {
        public MediaBrowserImplApi24(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }

        @Override
        public void subscribe(@NonNull String string2, @NonNull Bundle bundle, @NonNull SubscriptionCallback subscriptionCallback) {
            if (bundle == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, string2, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            MediaBrowserCompatApi24.subscribe(this.mBrowserObj, string2, bundle, subscriptionCallback.mSubscriptionCallbackObj);
        }

        @Override
        public void unsubscribe(@NonNull String string2, SubscriptionCallback subscriptionCallback) {
            if (subscriptionCallback == null) {
                MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, string2);
                return;
            }
            MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, string2, subscriptionCallback.mSubscriptionCallbackObj);
        }
    }

    static class MediaBrowserImplBase
    implements MediaBrowserImpl,
    MediaBrowserServiceCallbackImpl {
        private static final int CONNECT_STATE_CONNECTED = 2;
        static final int CONNECT_STATE_CONNECTING = 1;
        static final int CONNECT_STATE_DISCONNECTED = 0;
        static final int CONNECT_STATE_SUSPENDED = 3;
        final ConnectionCallback mCallback;
        Messenger mCallbacksMessenger;
        final Context mContext;
        private Bundle mExtras;
        final CallbackHandler mHandler = new CallbackHandler(this);
        private MediaSessionCompat.Token mMediaSessionToken;
        final Bundle mRootHints;
        private String mRootId;
        ServiceBinderWrapper mServiceBinderWrapper;
        final ComponentName mServiceComponent;
        MediaServiceConnection mServiceConnection;
        int mState = 0;
        private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();

        /*
         * Enabled aggressive block sorting
         */
        public MediaBrowserImplBase(Context object, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
            if (object == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (componentName == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (connectionCallback == null) {
                throw new IllegalArgumentException("connection callback must not be null");
            }
            this.mContext = object;
            this.mServiceComponent = componentName;
            this.mCallback = connectionCallback;
            object = bundle == null ? null : new Bundle(bundle);
            this.mRootHints = object;
        }

        private static String getStateLabel(int n2) {
            switch (n2) {
                default: {
                    return "UNKNOWN/" + n2;
                }
                case 0: {
                    return "CONNECT_STATE_DISCONNECTED";
                }
                case 1: {
                    return "CONNECT_STATE_CONNECTING";
                }
                case 2: {
                    return "CONNECT_STATE_CONNECTED";
                }
                case 3: 
            }
            return "CONNECT_STATE_SUSPENDED";
        }

        private boolean isCurrent(Messenger messenger, String string2) {
            if (this.mCallbacksMessenger != messenger) {
                if (this.mState != 0) {
                    Log.i((String)MediaBrowserCompat.TAG, (String)(string2 + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this));
                }
                return false;
            }
            return true;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void connect() {
            MediaServiceConnection mediaServiceConnection;
            if (this.mState != 0) {
                throw new IllegalStateException("connect() called while not disconnected (state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            if (DEBUG && this.mServiceConnection != null) {
                throw new RuntimeException("mServiceConnection should be null. Instead it is " + this.mServiceConnection);
            }
            if (this.mServiceBinderWrapper != null) {
                throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + this.mServiceBinderWrapper);
            }
            if (this.mCallbacksMessenger != null) {
                throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + this.mCallbacksMessenger);
            }
            this.mState = 1;
            Intent intent = new Intent("android.media.browse.MediaBrowserService");
            intent.setComponent(this.mServiceComponent);
            this.mServiceConnection = mediaServiceConnection = new MediaServiceConnection();
            boolean bl2 = false;
            try {
                boolean bl3;
                bl2 = bl3 = this.mContext.bindService(intent, (ServiceConnection)this.mServiceConnection, 1);
            }
            catch (Exception exception) {
                Log.e((String)MediaBrowserCompat.TAG, (String)("Failed binding to service " + this.mServiceComponent));
            }
            if (!bl2) {
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        if (mediaServiceConnection == MediaBrowserImplBase.this.mServiceConnection) {
                            MediaBrowserImplBase.this.forceCloseConnection();
                            MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                        }
                    }
                });
            }
            if (DEBUG) {
                Log.d((String)MediaBrowserCompat.TAG, (String)"connect...");
                this.dump();
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void disconnect() {
            if (this.mCallbacksMessenger != null) {
                try {
                    this.mServiceBinderWrapper.disconnect(this.mCallbacksMessenger);
                }
                catch (RemoteException remoteException) {
                    Log.w((String)MediaBrowserCompat.TAG, (String)("RemoteException during connect for " + this.mServiceComponent));
                }
            }
            this.forceCloseConnection();
            if (DEBUG) {
                Log.d((String)MediaBrowserCompat.TAG, (String)"disconnect...");
                this.dump();
            }
        }

        void dump() {
            Log.d((String)MediaBrowserCompat.TAG, (String)"MediaBrowserCompat...");
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mServiceComponent=" + this.mServiceComponent));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mCallback=" + this.mCallback));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mRootHints=" + this.mRootHints));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mState=" + MediaBrowserImplBase.getStateLabel(this.mState)));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mServiceConnection=" + this.mServiceConnection));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mServiceBinderWrapper=" + this.mServiceBinderWrapper));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mCallbacksMessenger=" + this.mCallbacksMessenger));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mRootId=" + this.mRootId));
            Log.d((String)MediaBrowserCompat.TAG, (String)("  mMediaSessionToken=" + this.mMediaSessionToken));
        }

        void forceCloseConnection() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService((ServiceConnection)this.mServiceConnection);
            }
            this.mState = 0;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }

        @Override
        @Nullable
        public Bundle getExtras() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getExtras() called while not connected (state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            return this.mExtras;
        }

        @Override
        public void getItem(final @NonNull String string2, final @NonNull ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)string2)) {
                throw new IllegalArgumentException("mediaId is empty");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null");
            }
            if (this.mState != 2) {
                Log.i((String)MediaBrowserCompat.TAG, (String)"Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
            ItemReceiver itemReceiver = new ItemReceiver(string2, itemCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.getMediaItem(string2, itemReceiver, this.mCallbacksMessenger);
                return;
            }
            catch (RemoteException remoteException) {
                Log.i((String)MediaBrowserCompat.TAG, (String)"Remote error getting media item.");
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
        }

        @Override
        @NonNull
        public String getRoot() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getRoot() called while not connected(state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            return this.mRootId;
        }

        @Override
        @NonNull
        public ComponentName getServiceComponent() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
            }
            return this.mServiceComponent;
        }

        @Override
        @NonNull
        public MediaSessionCompat.Token getSessionToken() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
            }
            return this.mMediaSessionToken;
        }

        @Override
        public boolean isConnected() {
            return this.mState == 2;
        }

        @Override
        public void onConnectionFailed(Messenger messenger) {
            Log.e((String)MediaBrowserCompat.TAG, (String)("onConnectFailed for " + this.mServiceComponent));
            if (!this.isCurrent(messenger, "onConnectFailed")) {
                return;
            }
            if (this.mState != 1) {
                Log.w((String)MediaBrowserCompat.TAG, (String)("onConnect from service while mState=" + MediaBrowserImplBase.getStateLabel(this.mState) + "... ignoring"));
                return;
            }
            this.forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onLoadChildren(Messenger object, String string2, List list, Bundle bundle) {
            if (!this.isCurrent((Messenger)object, "onLoadChildren")) return;
            if (DEBUG) {
                Log.d((String)MediaBrowserCompat.TAG, (String)("onLoadChildren for " + this.mServiceComponent + " id=" + string2));
            }
            if ((object = (Subscription)this.mSubscriptions.get(string2)) == null) {
                if (!DEBUG) return;
                Log.d((String)MediaBrowserCompat.TAG, (String)("onLoadChildren for id that isn't subscribed id=" + string2));
                return;
            }
            if ((object = ((Subscription)object).getCallback(bundle)) == null) {
                return;
            }
            if (bundle == null) {
                ((SubscriptionCallback)object).onChildrenLoaded(string2, list);
                return;
            }
            ((SubscriptionCallback)object).onChildrenLoaded(string2, list, bundle);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void onServiceConnected(Messenger object, String string2, MediaSessionCompat.Token list2, Bundle object2) {
            if (!this.isCurrent((Messenger)object, "onConnect")) return;
            if (this.mState != 1) {
                Log.w((String)MediaBrowserCompat.TAG, (String)("onConnect from service while mState=" + MediaBrowserImplBase.getStateLabel(this.mState) + "... ignoring"));
                return;
            }
            this.mRootId = string2;
            this.mMediaSessionToken = list2;
            this.mExtras = object2;
            this.mState = 2;
            if (DEBUG) {
                Log.d((String)MediaBrowserCompat.TAG, (String)"ServiceCallbacks.onConnect...");
                this.dump();
            }
            this.mCallback.onConnected();
            try {
                for (List<SubscriptionCallback> list2 : this.mSubscriptions.entrySet()) {
                    string2 = (String)list2.getKey();
                    object2 = (Subscription)list2.getValue();
                    list2 = ((Subscription)object2).getCallbacks();
                    object2 = ((Subscription)object2).getOptionsList();
                    for (int i2 = 0; i2 < list2.size(); ++i2) {
                        this.mServiceBinderWrapper.addSubscription(string2, list2.get(i2).mToken, (Bundle)object2.get(i2), this.mCallbacksMessenger);
                    }
                }
                return;
            }
            catch (RemoteException remoteException) {
                Log.d((String)MediaBrowserCompat.TAG, (String)"addSubscription failed with RemoteException.");
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void subscribe(@NonNull String string2, Bundle bundle, @NonNull SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            Subscription subscription2 = subscription = (Subscription)this.mSubscriptions.get(string2);
            if (subscription == null) {
                subscription2 = new Subscription();
                this.mSubscriptions.put(string2, subscription2);
            }
            subscription2.putCallback(bundle, subscriptionCallback);
            if (this.mState != 2) return;
            try {
                this.mServiceBinderWrapper.addSubscription(string2, subscriptionCallback.mToken, bundle, this.mCallbacksMessenger);
                return;
            }
            catch (RemoteException remoteException) {
                Log.d((String)MediaBrowserCompat.TAG, (String)("addSubscription failed with RemoteException parentId=" + string2));
                return;
            }
        }

        /*
         * Unable to fully structure code
         */
        @Override
        public void unsubscribe(@NonNull String var1_1, SubscriptionCallback var2_2) {
            var4_3 = (Subscription)this.mSubscriptions.get(var1_1);
            if (var4_3 == null) {
                return;
            }
            if (var2_2 == null) {
                block11: {
                    if (this.mState != 2) break block11;
                    this.mServiceBinderWrapper.removeSubscription(var1_1, null, this.mCallbacksMessenger);
                }
lbl10:
                // 3 sources

                while (true) {
                    if (!var4_3.isEmpty() && var2_2 != null) ** continue;
                    this.mSubscriptions.remove(var1_1);
                    return;
                }
            }
            try {
                var5_4 = var4_3.getCallbacks();
                var6_6 = var4_3.getOptionsList();
                var3_7 = var5_4.size() - 1;
            }
            catch (RemoteException var5_5) {
                Log.d((String)"MediaBrowserCompat", (String)("removeSubscription failed with RemoteException parentId=" + var1_1));
                ** continue;
            }
            while (true) {
                if (var3_7 < 0) ** GOTO lbl10
                if (var5_4.get(var3_7) == var2_2) {
                    if (this.mState == 2) {
                        this.mServiceBinderWrapper.removeSubscription(var1_1, SubscriptionCallback.access$000(var2_2), this.mCallbacksMessenger);
                    }
                    var5_4.remove(var3_7);
                    var6_6.remove(var3_7);
                }
                --var3_7;
                continue;
                break;
            }
        }

        private class MediaServiceConnection
        implements ServiceConnection {
            MediaServiceConnection() {
            }

            private void postOrRun(Runnable runnable) {
                if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
                    runnable.run();
                    return;
                }
                MediaBrowserImplBase.this.mHandler.post(runnable);
            }

            boolean isCurrent(String string2) {
                if (MediaBrowserImplBase.this.mServiceConnection != this) {
                    if (MediaBrowserImplBase.this.mState != 0) {
                        Log.i((String)MediaBrowserCompat.TAG, (String)(string2 + " for " + MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection + " this=" + this));
                    }
                    return false;
                }
                return true;
            }

            public void onServiceConnected(final ComponentName componentName, final IBinder iBinder) {
                this.postOrRun(new Runnable(){

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    @Override
                    public void run() {
                        block5: {
                            if (DEBUG) {
                                Log.d((String)MediaBrowserCompat.TAG, (String)("MediaServiceConnection.onServiceConnected name=" + componentName + " binder=" + iBinder));
                                MediaBrowserImplBase.this.dump();
                            }
                            if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                                MediaBrowserImplBase.this.mServiceBinderWrapper = new ServiceBinderWrapper(iBinder, MediaBrowserImplBase.this.mRootHints);
                                MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger((Handler)MediaBrowserImplBase.this.mHandler);
                                MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                                MediaBrowserImplBase.this.mState = 1;
                                try {
                                    if (DEBUG) {
                                        Log.d((String)MediaBrowserCompat.TAG, (String)"ServiceCallbacks.onConnect...");
                                        MediaBrowserImplBase.this.dump();
                                    }
                                    MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                                    return;
                                }
                                catch (RemoteException remoteException) {
                                    Log.w((String)MediaBrowserCompat.TAG, (String)("RemoteException during connect for " + MediaBrowserImplBase.this.mServiceComponent));
                                    if (!DEBUG) break block5;
                                    Log.d((String)MediaBrowserCompat.TAG, (String)"ServiceCallbacks.onConnect...");
                                    MediaBrowserImplBase.this.dump();
                                    return;
                                }
                            }
                        }
                    }
                });
            }

            public void onServiceDisconnected(final ComponentName componentName) {
                this.postOrRun(new Runnable(){

                    @Override
                    public void run() {
                        if (DEBUG) {
                            Log.d((String)MediaBrowserCompat.TAG, (String)("MediaServiceConnection.onServiceDisconnected name=" + componentName + " this=" + this + " mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection));
                            MediaBrowserImplBase.this.dump();
                        }
                        if (!MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                            return;
                        }
                        MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                        MediaBrowserImplBase.this.mCallbacksMessenger = null;
                        MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(null);
                        MediaBrowserImplBase.this.mState = 3;
                        MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                    }
                });
            }
        }
    }

    static interface MediaBrowserServiceCallbackImpl {
        public void onConnectionFailed(Messenger var1);

        public void onLoadChildren(Messenger var1, String var2, List var3, Bundle var4);

        public void onServiceConnected(Messenger var1, String var2, MediaSessionCompat.Token var3, Bundle var4);
    }

    public static class MediaItem
    implements Parcelable {
        public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>(){

            public MediaItem createFromParcel(Parcel parcel) {
                return new MediaItem(parcel);
            }

            public MediaItem[] newArray(int n2) {
                return new MediaItem[n2];
            }
        };
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;

        MediaItem(Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }

        public MediaItem(@NonNull MediaDescriptionCompat mediaDescriptionCompat, int n2) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (TextUtils.isEmpty((CharSequence)mediaDescriptionCompat.getMediaId())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            }
            this.mFlags = n2;
            this.mDescription = mediaDescriptionCompat;
        }

        public static MediaItem fromMediaItem(Object object) {
            if (object == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            int n2 = MediaBrowserCompatApi21.MediaItem.getFlags(object);
            return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(object)), n2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public static List<MediaItem> fromMediaItemList(List<?> list) {
            if (list == null) return null;
            if (Build.VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList<MediaItem> arrayList = new ArrayList<MediaItem>(list.size());
            Iterator<MediaItem> iterator = list.iterator();
            while (true) {
                list = arrayList;
                if (!iterator.hasNext()) return list;
                arrayList.add(MediaItem.fromMediaItem(iterator.next()));
            }
        }

        public int describeContents() {
            return 0;
        }

        @NonNull
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public int getFlags() {
            return this.mFlags;
        }

        @NonNull
        public String getMediaId() {
            return this.mDescription.getMediaId();
        }

        public boolean isBrowsable() {
            return (this.mFlags & 1) != 0;
        }

        public boolean isPlayable() {
            return (this.mFlags & 2) != 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("MediaItem{");
            stringBuilder.append("mFlags=").append(this.mFlags);
            stringBuilder.append(", mDescription=").append(this.mDescription);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int n2) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n2);
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface Flags {
        }
    }

    private static class ServiceBinderWrapper {
        private Messenger mMessenger;
        private Bundle mRootHints;

        public ServiceBinderWrapper(IBinder iBinder, Bundle bundle) {
            this.mMessenger = new Messenger(iBinder);
            this.mRootHints = bundle;
        }

        private void sendRequest(int n2, Bundle bundle, Messenger messenger) throws RemoteException {
            Message message = Message.obtain();
            message.what = n2;
            message.arg1 = 1;
            message.setData(bundle);
            message.replyTo = messenger;
            this.mMessenger.send(message);
        }

        void addSubscription(String string2, IBinder iBinder, Bundle bundle, Messenger messenger) throws RemoteException {
            Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", string2);
            BundleCompat.putBinder(bundle2, "data_callback_token", iBinder);
            bundle2.putBundle("data_options", bundle);
            this.sendRequest(3, bundle2, messenger);
        }

        void connect(Context context, Messenger messenger) throws RemoteException {
            Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(1, bundle, messenger);
        }

        void disconnect(Messenger messenger) throws RemoteException {
            this.sendRequest(2, null, messenger);
        }

        void getMediaItem(String string2, ResultReceiver resultReceiver, Messenger messenger) throws RemoteException {
            Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", string2);
            bundle.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(5, bundle, messenger);
        }

        void registerCallbackMessenger(Messenger messenger) throws RemoteException {
            Bundle bundle = new Bundle();
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(6, bundle, messenger);
        }

        void removeSubscription(String string2, IBinder iBinder, Messenger messenger) throws RemoteException {
            Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", string2);
            BundleCompat.putBinder(bundle, "data_callback_token", iBinder);
            this.sendRequest(4, bundle, messenger);
        }

        void unregisterCallbackMessenger(Messenger messenger) throws RemoteException {
            this.sendRequest(7, null, messenger);
        }
    }

    private static class Subscription {
        private final List<SubscriptionCallback> mCallbacks = new ArrayList<SubscriptionCallback>();
        private final List<Bundle> mOptionsList = new ArrayList<Bundle>();

        public SubscriptionCallback getCallback(Bundle bundle) {
            for (int i2 = 0; i2 < this.mOptionsList.size(); ++i2) {
                if (!MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i2), bundle)) continue;
                return this.mCallbacks.get(i2);
            }
            return null;
        }

        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }

        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }

        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }

        public void putCallback(Bundle bundle, SubscriptionCallback subscriptionCallback) {
            for (int i2 = 0; i2 < this.mOptionsList.size(); ++i2) {
                if (!MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i2), bundle)) continue;
                this.mCallbacks.set(i2, subscriptionCallback);
                return;
            }
            this.mCallbacks.add(subscriptionCallback);
            this.mOptionsList.add(bundle);
        }
    }

    public static abstract class SubscriptionCallback {
        private final Object mSubscriptionCallbackObj;
        WeakReference<Subscription> mSubscriptionRef;
        private final IBinder mToken;

        public SubscriptionCallback() {
            if (Build.VERSION.SDK_INT >= 24 || BuildCompat.isAtLeastN()) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback(new StubApi24());
                this.mToken = null;
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback(new StubApi21());
                this.mToken = new Binder();
                return;
            }
            this.mSubscriptionCallbackObj = null;
            this.mToken = new Binder();
        }

        private void setSubscription(Subscription subscription) {
            this.mSubscriptionRef = new WeakReference<Subscription>(subscription);
        }

        public void onChildrenLoaded(@NonNull String string2, List<MediaItem> list) {
        }

        public void onChildrenLoaded(@NonNull String string2, List<MediaItem> list, @NonNull Bundle bundle) {
        }

        public void onError(@NonNull String string2) {
        }

        public void onError(@NonNull String string2, @NonNull Bundle bundle) {
        }

        private class StubApi21
        implements MediaBrowserCompatApi21.SubscriptionCallback {
            StubApi21() {
            }

            /*
             * WARNING - void declaration
             * Enabled aggressive block sorting
             */
            List<MediaItem> applyOptions(List<MediaItem> list, Bundle object) {
                void var2_4;
                if (list == null) {
                    return var2_4;
                }
                int n2 = object.getInt(MediaBrowserCompat.EXTRA_PAGE, -1);
                int n3 = object.getInt(MediaBrowserCompat.EXTRA_PAGE_SIZE, -1);
                if (n2 == -1) {
                    List<MediaItem> list2 = list;
                    if (n3 == -1) {
                        return var2_4;
                    }
                }
                int n4 = n3 * n2;
                int n5 = n4 + n3;
                if (n2 < 0 || n3 < 1 || n4 >= list.size()) {
                    return Collections.EMPTY_LIST;
                }
                n2 = n5;
                if (n5 > list.size()) {
                    n2 = list.size();
                }
                return list.subList(n4, n2);
            }

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void onChildrenLoaded(@NonNull String string2, List<?> list) {
                Object object = SubscriptionCallback.this.mSubscriptionRef == null ? null : (Subscription)SubscriptionCallback.this.mSubscriptionRef.get();
                if (object == null) {
                    SubscriptionCallback.this.onChildrenLoaded(string2, MediaItem.fromMediaItemList(list));
                    return;
                }
                list = MediaItem.fromMediaItemList(list);
                List<SubscriptionCallback> list2 = ((Subscription)object).getCallbacks();
                object = ((Subscription)object).getOptionsList();
                int n2 = 0;
                while (n2 < list2.size()) {
                    Bundle bundle = (Bundle)object.get(n2);
                    if (bundle == null) {
                        SubscriptionCallback.this.onChildrenLoaded(string2, list);
                    } else {
                        SubscriptionCallback.this.onChildrenLoaded(string2, this.applyOptions(list, bundle), bundle);
                    }
                    ++n2;
                }
                return;
            }

            @Override
            public void onError(@NonNull String string2) {
                SubscriptionCallback.this.onError(string2);
            }
        }

        private class StubApi24
        extends StubApi21
        implements MediaBrowserCompatApi24.SubscriptionCallback {
            StubApi24() {
            }

            @Override
            public void onChildrenLoaded(@NonNull String string2, List<?> list, @NonNull Bundle bundle) {
                SubscriptionCallback.this.onChildrenLoaded(string2, MediaItem.fromMediaItemList(list), bundle);
            }

            @Override
            public void onError(@NonNull String string2, @NonNull Bundle bundle) {
                SubscriptionCallback.this.onError(string2, bundle);
            }
        }
    }
}

