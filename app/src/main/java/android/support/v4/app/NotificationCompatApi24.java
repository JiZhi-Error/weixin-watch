/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Action$Builder
 *  android.app.Notification$Builder
 *  android.app.Notification$MessagingStyle
 *  android.app.Notification$MessagingStyle$Message
 *  android.app.PendingIntent
 *  android.app.RemoteInput
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Bundle
 *  android.widget.RemoteViews
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationBuilderWithActions;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatApi20;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;

class NotificationCompatApi24 {
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";

    NotificationCompatApi24() {
    }

    public static void addMessagingStyle(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, CharSequence charSequence, CharSequence charSequence2, List<CharSequence> list, List<Long> list2, List<CharSequence> list3, List<String> list4, List<Uri> list5) {
        charSequence = new Notification.MessagingStyle(charSequence).setConversationTitle(charSequence2);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            charSequence2 = new Notification.MessagingStyle.Message(list.get(i2), list2.get(i2).longValue(), list3.get(i2));
            if (list4.get(i2) != null) {
                charSequence2.setData(list4.get(i2), list5.get(i2));
            }
            charSequence.addMessage((Notification.MessagingStyle.Message)charSequence2);
        }
        charSequence.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
    }

    public static class Builder
    implements NotificationBuilderWithBuilderAccessor,
    NotificationBuilderWithActions {
        private Notification.Builder b;

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         */
        public Builder(Context object, Notification object2, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n2, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n3, int n4, boolean bl2, boolean bl3, boolean bl4, int n5, CharSequence charSequence4, boolean bl5, String string2, ArrayList<String> arrayList, Bundle bundle, int n6, int n7, Notification notification, String string3, boolean bl6, String string4, CharSequence[] charSequenceArray, RemoteViews remoteViews2, RemoteViews remoteViews3, RemoteViews remoteViews4) {
            void var20_22;
            void var31_33;
            void var30_32;
            void var29_31;
            void var28_30;
            void var24_26;
            void var23_25;
            void var22_24;
            void var19_21;
            void var27_29;
            void var26_28;
            void var25_27;
            void var21_23;
            void var18_20;
            void var13_15;
            void var12_14;
            void var11_13;
            void var16_18;
            void var15_17;
            void var7_9;
            void var10_12;
            void var9_11;
            void var8_10;
            void var5_7;
            void var17_19;
            void var4_6;
            void var3_5;
            void var6_8;
            boolean bl7;
            object = new Notification.Builder((Context)object).setWhen(object2.when).setShowWhen(bl7).setSmallIcon(object2.icon, object2.iconLevel).setContent(object2.contentView).setTicker(object2.tickerText, (RemoteViews)var6_8).setSound(object2.sound, object2.audioStreamType).setVibrate(object2.vibrate).setLights(object2.ledARGB, object2.ledOnMS, object2.ledOffMS);
            bl7 = (object2.flags & 2) != 0;
            object = object.setOngoing(bl7);
            bl7 = (object2.flags & 8) != 0;
            object = object.setOnlyAlertOnce(bl7);
            bl7 = (object2.flags & 0x10) != 0;
            object = object.setAutoCancel(bl7).setDefaults(object2.defaults).setContentTitle((CharSequence)var3_5).setContentText((CharSequence)var4_6).setSubText((CharSequence)var17_19).setContentInfo((CharSequence)var5_7).setContentIntent((PendingIntent)var8_10).setDeleteIntent(object2.deleteIntent);
            bl7 = (object2.flags & 0x80) != 0;
            this.b = object.setFullScreenIntent((PendingIntent)var9_11, bl7).setLargeIcon((Bitmap)var10_12).setNumber((int)var7_9).setUsesChronometer((boolean)var15_17).setPriority((int)var16_18).setProgress((int)var11_13, (int)var12_14, (boolean)var13_15).setLocalOnly((boolean)var18_20).setExtras((Bundle)var21_23).setGroup((String)var25_27).setGroupSummary((boolean)var26_28).setSortKey((String)var27_29).setCategory((String)var19_21).setColor((int)var22_24).setVisibility((int)var23_25).setPublicVersion((Notification)var24_26).setRemoteInputHistory((CharSequence[])var28_30);
            if (var29_31 != null) {
                this.b.setCustomContentView((RemoteViews)var29_31);
            }
            if (var30_32 != null) {
                this.b.setCustomBigContentView((RemoteViews)var30_32);
            }
            if (var31_33 != null) {
                this.b.setCustomHeadsUpContentView((RemoteViews)var31_33);
            }
            object = var20_22.iterator();
            while (object.hasNext()) {
                String string5 = (String)object.next();
                this.b.addPerson(string5);
            }
            return;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void addAction(NotificationCompatBase.Action action) {
            Bundle bundle;
            Notification.Action.Builder builder = new Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            if (action.getRemoteInputs() != null) {
                bundle = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
                int n2 = ((RemoteInput[])bundle).length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    builder.addRemoteInput((RemoteInput)bundle[i2]);
                }
            }
            bundle = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
            bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            builder.addExtras(bundle);
            builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            this.b.addAction(builder.build());
        }

        @Override
        public Notification build() {
            return this.b.build();
        }

        @Override
        public Notification.Builder getBuilder() {
            return this.b;
        }
    }
}

