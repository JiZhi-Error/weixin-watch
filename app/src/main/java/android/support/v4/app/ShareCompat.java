/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  android.text.Html
 *  android.text.Spanned
 *  android.util.Log
 *  android.view.Menu
 *  android.view.MenuItem
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.app.ShareCompatICS;
import android.support.v4.app.ShareCompatJB;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public final class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    static ShareCompatImpl IMPL = Build.VERSION.SDK_INT >= 16 ? new ShareCompatImplJB() : (Build.VERSION.SDK_INT >= 14 ? new ShareCompatImplICS() : new ShareCompatImplBase());

    private ShareCompat() {
    }

    public static void configureMenuItem(Menu menu, int n2, IntentBuilder intentBuilder) {
        if ((menu = menu.findItem(n2)) == null) {
            throw new IllegalArgumentException("Could not find menu item with id " + n2 + " in the supplied menu");
        }
        ShareCompat.configureMenuItem((MenuItem)menu, intentBuilder);
    }

    public static void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
        IMPL.configureMenuItem(menuItem, intentBuilder);
    }

    public static ComponentName getCallingActivity(Activity activity) {
        ComponentName componentName;
        ComponentName componentName2 = componentName = activity.getCallingActivity();
        if (componentName == null) {
            componentName2 = (ComponentName)activity.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY);
        }
        return componentName2;
    }

    public static String getCallingPackage(Activity activity) {
        String string2;
        String string3 = string2 = activity.getCallingPackage();
        if (string2 == null) {
            string3 = activity.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE);
        }
        return string3;
    }

    public static class IntentBuilder {
        private Activity mActivity;
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;

        private IntentBuilder(Activity activity) {
            this.mActivity = activity;
            this.mIntent = new Intent().setAction("android.intent.action.SEND");
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, activity.getPackageName());
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY, (Parcelable)activity.getComponentName());
            this.mIntent.addFlags(524288);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void combineArrayExtra(String string2, ArrayList<String> arrayList) {
            String[] stringArray = this.mIntent.getStringArrayExtra(string2);
            int n2 = stringArray != null ? stringArray.length : 0;
            String[] stringArray2 = new String[arrayList.size() + n2];
            arrayList.toArray(stringArray2);
            if (stringArray != null) {
                System.arraycopy(stringArray, 0, stringArray2, arrayList.size(), n2);
            }
            this.mIntent.putExtra(string2, stringArray2);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void combineArrayExtra(String string2, String[] stringArray) {
            Intent intent = this.getIntent();
            String[] stringArray2 = intent.getStringArrayExtra(string2);
            int n2 = stringArray2 != null ? stringArray2.length : 0;
            String[] stringArray3 = new String[stringArray.length + n2];
            if (stringArray2 != null) {
                System.arraycopy(stringArray2, 0, stringArray3, 0, n2);
            }
            System.arraycopy(stringArray, 0, stringArray3, n2, stringArray.length);
            intent.putExtra(string2, stringArray3);
        }

        public static IntentBuilder from(Activity activity) {
            return new IntentBuilder(activity);
        }

        public IntentBuilder addEmailBcc(String string2) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList();
            }
            this.mBccAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailBcc(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.BCC", stringArray);
            return this;
        }

        public IntentBuilder addEmailCc(String string2) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList();
            }
            this.mCcAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailCc(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.CC", stringArray);
            return this;
        }

        public IntentBuilder addEmailTo(String string2) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList();
            }
            this.mToAddresses.add(string2);
            return this;
        }

        public IntentBuilder addEmailTo(String[] stringArray) {
            this.combineArrayExtra("android.intent.extra.EMAIL", stringArray);
            return this;
        }

        public IntentBuilder addStream(Uri uri) {
            Uri uri2 = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            if (this.mStreams == null && uri2 == null) {
                return this.setStream(uri);
            }
            if (this.mStreams == null) {
                this.mStreams = new ArrayList();
            }
            if (uri2 != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                this.mStreams.add(uri2);
            }
            this.mStreams.add(uri);
            return this;
        }

        public Intent createChooserIntent() {
            return Intent.createChooser((Intent)this.getIntent(), (CharSequence)this.mChooserTitle);
        }

        Activity getActivity() {
            return this.mActivity;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Intent getIntent() {
            boolean bl2 = true;
            if (this.mToAddresses != null) {
                this.combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
                this.mToAddresses = null;
            }
            if (this.mCcAddresses != null) {
                this.combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
                this.mCcAddresses = null;
            }
            if (this.mBccAddresses != null) {
                this.combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
                this.mBccAddresses = null;
            }
            if (this.mStreams == null || this.mStreams.size() <= 1) {
                bl2 = false;
            }
            boolean bl3 = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
            if (!bl2 && bl3) {
                this.mIntent.setAction("android.intent.action.SEND");
                if (this.mStreams != null && !this.mStreams.isEmpty()) {
                    this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)this.mStreams.get(0));
                } else {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                }
                this.mStreams = null;
            }
            if (!bl2) return this.mIntent;
            if (bl3) return this.mIntent;
            this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
            if (this.mStreams != null && !this.mStreams.isEmpty()) {
                this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
                return this.mIntent;
            }
            this.mIntent.removeExtra("android.intent.extra.STREAM");
            return this.mIntent;
        }

        public IntentBuilder setChooserTitle(@StringRes int n2) {
            return this.setChooserTitle(this.mActivity.getText(n2));
        }

        public IntentBuilder setChooserTitle(CharSequence charSequence) {
            this.mChooserTitle = charSequence;
            return this;
        }

        public IntentBuilder setEmailBcc(String[] stringArray) {
            this.mIntent.putExtra("android.intent.extra.BCC", stringArray);
            return this;
        }

        public IntentBuilder setEmailCc(String[] stringArray) {
            this.mIntent.putExtra("android.intent.extra.CC", stringArray);
            return this;
        }

        public IntentBuilder setEmailTo(String[] stringArray) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", stringArray);
            return this;
        }

        public IntentBuilder setHtmlText(String string2) {
            this.mIntent.putExtra("android.intent.extra.HTML_TEXT", string2);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                this.setText((CharSequence)Html.fromHtml((String)string2));
            }
            return this;
        }

        public IntentBuilder setStream(Uri uri) {
            if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
                this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable)uri);
            return this;
        }

        public IntentBuilder setSubject(String string2) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", string2);
            return this;
        }

        public IntentBuilder setText(CharSequence charSequence) {
            this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }

        public IntentBuilder setType(String string2) {
            this.mIntent.setType(string2);
            return this;
        }

        public void startChooser() {
            this.mActivity.startActivity(this.createChooserIntent());
        }
    }

    public static class IntentReader {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;

        private IntentReader(Activity activity) {
            this.mActivity = activity;
            this.mIntent = activity.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(activity);
            this.mCallingActivity = ShareCompat.getCallingActivity(activity);
        }

        public static IntentReader from(Activity activity) {
            return new IntentReader(activity);
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                packageManager = packageManager.getActivityIcon(this.mCallingActivity);
                return packageManager;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve icon for calling activity", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                packageManager = packageManager.getApplicationIcon(this.mCallingPackage);
                return packageManager;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve icon for calling application", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            Object object = this.mActivity.getPackageManager();
            try {
                object = object.getApplicationLabel(object.getApplicationInfo(this.mCallingPackage, 0));
                return object;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)TAG, (String)"Could not retrieve label for calling application", (Throwable)nameNotFoundException);
                return null;
            }
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public String getHtmlText() {
            String string2;
            String string3 = string2 = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
            if (string2 != null) return string3;
            CharSequence charSequence = this.getText();
            if (charSequence instanceof Spanned) {
                return Html.toHtml((Spanned)((Spanned)charSequence));
            }
            string3 = string2;
            if (charSequence == null) return string3;
            return IMPL.escapeHtml(charSequence);
        }

        public Uri getStream() {
            return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int n2) {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return this.mStreams.get(n2);
            }
            if (n2 == 0) {
                return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            throw new IndexOutOfBoundsException("Stream items available: " + this.getStreamCount() + " index requested: " + n2);
        }

        public int getStreamCount() {
            if (this.mStreams == null && this.isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return this.mStreams.size();
            }
            if (this.mIntent.hasExtra("android.intent.extra.STREAM")) {
                return 1;
            }
            return 0;
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }

        public boolean isShareIntent() {
            String string2 = this.mIntent.getAction();
            return "android.intent.action.SEND".equals(string2) || "android.intent.action.SEND_MULTIPLE".equals(string2);
        }

        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }

    static interface ShareCompatImpl {
        public void configureMenuItem(MenuItem var1, IntentBuilder var2);

        public String escapeHtml(CharSequence var1);
    }

    static class ShareCompatImplBase
    implements ShareCompatImpl {
        ShareCompatImplBase() {
        }

        /*
         * Enabled aggressive block sorting
         */
        private static void withinStyle(StringBuilder stringBuilder, CharSequence charSequence, int n2, int n3) {
            while (n2 < n3) {
                char c2 = charSequence.charAt(n2);
                if (c2 == '<') {
                    stringBuilder.append("&lt;");
                } else if (c2 == '>') {
                    stringBuilder.append("&gt;");
                } else if (c2 == '&') {
                    stringBuilder.append("&amp;");
                } else if (c2 > '~' || c2 < ' ') {
                    stringBuilder.append("&#" + c2 + ";");
                } else if (c2 == ' ') {
                    while (n2 + 1 < n3 && charSequence.charAt(n2 + 1) == ' ') {
                        stringBuilder.append("&nbsp;");
                        ++n2;
                    }
                    stringBuilder.append(' ');
                } else {
                    stringBuilder.append(c2);
                }
                ++n2;
            }
            return;
        }

        @Override
        public void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
            menuItem.setIntent(intentBuilder.createChooserIntent());
        }

        @Override
        public String escapeHtml(CharSequence charSequence) {
            StringBuilder stringBuilder = new StringBuilder();
            ShareCompatImplBase.withinStyle(stringBuilder, charSequence, 0, charSequence.length());
            return stringBuilder.toString();
        }
    }

    static class ShareCompatImplICS
    extends ShareCompatImplBase {
        ShareCompatImplICS() {
        }

        @Override
        public void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
            ShareCompatICS.configureMenuItem(menuItem, intentBuilder.getActivity(), intentBuilder.getIntent());
            if (this.shouldAddChooserIntent(menuItem)) {
                menuItem.setIntent(intentBuilder.createChooserIntent());
            }
        }

        boolean shouldAddChooserIntent(MenuItem menuItem) {
            return !menuItem.hasSubMenu();
        }
    }

    static class ShareCompatImplJB
    extends ShareCompatImplICS {
        ShareCompatImplJB() {
        }

        @Override
        public String escapeHtml(CharSequence charSequence) {
            return ShareCompatJB.escapeHtml(charSequence);
        }

        @Override
        boolean shouldAddChooserIntent(MenuItem menuItem) {
            return false;
        }
    }
}

