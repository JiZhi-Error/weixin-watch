/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Service
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.RemoteException
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;

public abstract class NotificationCompatSideChannelService
extends Service {
    public abstract void cancel(String var1, int var2, String var3);

    public abstract void cancelAll(String var1);

    void checkPermission(int n2, String string2) {
        String[] stringArray = this.getPackageManager().getPackagesForUid(n2);
        int n3 = stringArray.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (!stringArray[i2].equals(string2)) continue;
            return;
        }
        throw new SecurityException("NotificationSideChannelService: Uid " + n2 + " is not authorized for package " + string2);
    }

    public abstract void notify(String var1, int var2, String var3, Notification var4);

    public IBinder onBind(Intent intent) {
        if (!intent.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL") || Build.VERSION.SDK_INT > 19) {
            return null;
        }
        return new NotificationSideChannelStub();
    }

    private class NotificationSideChannelStub
    extends INotificationSideChannel.Stub {
        NotificationSideChannelStub() {
        }

        @Override
        public void cancel(String string2, int n2, String string3) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l2 = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancel(string2, n2, string3);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l2);
            }
        }

        @Override
        public void cancelAll(String string2) {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l2 = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancelAll(string2);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l2);
            }
        }

        @Override
        public void notify(String string2, int n2, String string3, Notification notification) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l2 = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.notify(string2, n2, string3, notification);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l2);
            }
        }
    }
}

