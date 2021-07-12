/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilderHoneycomb;
import android.support.v4.app.TaskStackBuilderJellybean;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder
implements Iterable<Intent> {
    private static final TaskStackBuilderImpl IMPL = Build.VERSION.SDK_INT >= 11 ? new TaskStackBuilderImplHoneycomb() : new TaskStackBuilderImplBase();
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents = new ArrayList();
    private final Context mSourceContext;

    private TaskStackBuilder(Context context) {
        this.mSourceContext = context;
    }

    public static TaskStackBuilder create(Context context) {
        return new TaskStackBuilder(context);
    }

    @Deprecated
    public static TaskStackBuilder from(Context context) {
        return TaskStackBuilder.create(context);
    }

    public TaskStackBuilder addNextIntent(Intent intent) {
        this.mIntents.add(intent);
        return this;
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent intent) {
        ComponentName componentName;
        ComponentName componentName2 = componentName = intent.getComponent();
        if (componentName == null) {
            componentName2 = intent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (componentName2 != null) {
            this.addParentStack(componentName2);
        }
        this.addNextIntent(intent);
        return this;
    }

    public TaskStackBuilder addParentStack(Activity activity) {
        Intent intent = null;
        if (activity instanceof SupportParentable) {
            intent = ((SupportParentable)activity).getSupportParentActivityIntent();
        }
        Intent intent2 = intent;
        if (intent == null) {
            intent2 = NavUtils.getParentActivityIntent(activity);
        }
        if (intent2 != null) {
            intent = intent2.getComponent();
            activity = intent;
            if (intent == null) {
                activity = intent2.resolveActivity(this.mSourceContext.getPackageManager());
            }
            this.addParentStack((ComponentName)activity);
            this.addNextIntent(intent2);
        }
        return this;
    }

    public TaskStackBuilder addParentStack(ComponentName componentName) {
        int n2 = this.mIntents.size();
        componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName);
        while (componentName != null) {
            try {
                this.mIntents.add(n2, (Intent)componentName);
                componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName.getComponent());
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Bad ComponentName while traversing activity parent metadata");
                throw new IllegalArgumentException(nameNotFoundException);
            }
        }
        return this;
    }

    public TaskStackBuilder addParentStack(Class<?> clazz) {
        return this.addParentStack(new ComponentName(this.mSourceContext, clazz));
    }

    public Intent editIntentAt(int n2) {
        return this.mIntents.get(n2);
    }

    @Deprecated
    public Intent getIntent(int n2) {
        return this.editIntentAt(n2);
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    /*
     * Enabled aggressive block sorting
     */
    public Intent[] getIntents() {
        Intent[] intentArray = new Intent[this.mIntents.size()];
        if (intentArray.length != 0) {
            intentArray[0] = new Intent(this.mIntents.get(0)).addFlags(0x1000C000);
            for (int i2 = 1; i2 < intentArray.length; ++i2) {
                intentArray[i2] = new Intent(this.mIntents.get(i2));
            }
        }
        return intentArray;
    }

    public PendingIntent getPendingIntent(int n2, int n3) {
        return this.getPendingIntent(n2, n3, null);
    }

    public PendingIntent getPendingIntent(int n2, int n3, Bundle bundle) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
        }
        Intent[] intentArray = this.mIntents.toArray(new Intent[this.mIntents.size()]);
        intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
        return IMPL.getPendingIntent(this.mSourceContext, intentArray, n2, n3, bundle);
    }

    @Override
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        this.startActivities(null);
    }

    public void startActivities(Bundle bundle) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        Intent[] intentArray = this.mIntents.toArray(new Intent[this.mIntents.size()]);
        intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
        if (!ContextCompat.startActivities(this.mSourceContext, intentArray, bundle)) {
            bundle = new Intent(intentArray[intentArray.length - 1]);
            bundle.addFlags(0x10000000);
            this.mSourceContext.startActivity((Intent)bundle);
        }
    }

    public static interface SupportParentable {
        public Intent getSupportParentActivityIntent();
    }

    static interface TaskStackBuilderImpl {
        public PendingIntent getPendingIntent(Context var1, Intent[] var2, int var3, int var4, Bundle var5);
    }

    static class TaskStackBuilderImplBase
    implements TaskStackBuilderImpl {
        TaskStackBuilderImplBase() {
        }

        @Override
        public PendingIntent getPendingIntent(Context context, Intent[] intent, int n2, int n3, Bundle bundle) {
            intent = new Intent(intent[((Intent[])intent).length - 1]);
            intent.addFlags(0x10000000);
            return PendingIntent.getActivity((Context)context, (int)n2, (Intent)intent, (int)n3);
        }
    }

    static class TaskStackBuilderImplHoneycomb
    implements TaskStackBuilderImpl {
        TaskStackBuilderImplHoneycomb() {
        }

        @Override
        public PendingIntent getPendingIntent(Context context, Intent[] intentArray, int n2, int n3, Bundle bundle) {
            intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
            return TaskStackBuilderHoneycomb.getActivitiesPendingIntent(context, n2, intentArray, n3);
        }
    }

    static class TaskStackBuilderImplJellybean
    implements TaskStackBuilderImpl {
        TaskStackBuilderImplJellybean() {
        }

        @Override
        public PendingIntent getPendingIntent(Context context, Intent[] intentArray, int n2, int n3, Bundle bundle) {
            intentArray[0] = new Intent(intentArray[0]).addFlags(0x1000C000);
            return TaskStackBuilderJellybean.getActivitiesPendingIntent(context, n2, intentArray, n3, bundle);
        }
    }
}

