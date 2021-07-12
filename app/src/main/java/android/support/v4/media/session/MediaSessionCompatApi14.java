/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.media.AudioManager
 *  android.media.RemoteControlClient
 *  android.media.RemoteControlClient$MetadataEditor
 *  android.os.Bundle
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Bundle;

class MediaSessionCompatApi14 {
    private static final long ACTION_FAST_FORWARD = 64L;
    private static final long ACTION_PAUSE = 2L;
    private static final long ACTION_PLAY = 4L;
    private static final long ACTION_PLAY_PAUSE = 512L;
    private static final long ACTION_REWIND = 8L;
    private static final long ACTION_SKIP_TO_NEXT = 32L;
    private static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    private static final long ACTION_STOP = 1L;
    private static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    private static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    private static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    private static final String METADATA_KEY_ART = "android.media.metadata.ART";
    private static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    private static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    private static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    private static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    private static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    private static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    private static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    private static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    private static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    private static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    private static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    static final int RCC_PLAYSTATE_NONE = 0;
    static final int STATE_BUFFERING = 6;
    static final int STATE_CONNECTING = 8;
    static final int STATE_ERROR = 7;
    static final int STATE_FAST_FORWARDING = 4;
    static final int STATE_NONE = 0;
    static final int STATE_PAUSED = 2;
    static final int STATE_PLAYING = 3;
    static final int STATE_REWINDING = 5;
    static final int STATE_SKIPPING_TO_NEXT = 10;
    static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    static final int STATE_STOPPED = 1;

    MediaSessionCompatApi14() {
    }

    /*
     * Enabled aggressive block sorting
     */
    static void buildOldMetadata(Bundle bundle, RemoteControlClient.MetadataEditor metadataEditor) {
        block19: {
            block18: {
                if (bundle == null) break block18;
                if (bundle.containsKey(METADATA_KEY_ART)) {
                    metadataEditor.putBitmap(100, (Bitmap)bundle.getParcelable(METADATA_KEY_ART));
                } else if (bundle.containsKey(METADATA_KEY_ALBUM_ART)) {
                    metadataEditor.putBitmap(100, (Bitmap)bundle.getParcelable(METADATA_KEY_ALBUM_ART));
                }
                if (bundle.containsKey(METADATA_KEY_ALBUM)) {
                    metadataEditor.putString(1, bundle.getString(METADATA_KEY_ALBUM));
                }
                if (bundle.containsKey(METADATA_KEY_ALBUM_ARTIST)) {
                    metadataEditor.putString(13, bundle.getString(METADATA_KEY_ALBUM_ARTIST));
                }
                if (bundle.containsKey(METADATA_KEY_ARTIST)) {
                    metadataEditor.putString(2, bundle.getString(METADATA_KEY_ARTIST));
                }
                if (bundle.containsKey(METADATA_KEY_AUTHOR)) {
                    metadataEditor.putString(3, bundle.getString(METADATA_KEY_AUTHOR));
                }
                if (bundle.containsKey(METADATA_KEY_COMPILATION)) {
                    metadataEditor.putString(15, bundle.getString(METADATA_KEY_COMPILATION));
                }
                if (bundle.containsKey(METADATA_KEY_COMPOSER)) {
                    metadataEditor.putString(4, bundle.getString(METADATA_KEY_COMPOSER));
                }
                if (bundle.containsKey(METADATA_KEY_DATE)) {
                    metadataEditor.putString(5, bundle.getString(METADATA_KEY_DATE));
                }
                if (bundle.containsKey(METADATA_KEY_DISC_NUMBER)) {
                    metadataEditor.putLong(14, bundle.getLong(METADATA_KEY_DISC_NUMBER));
                }
                if (bundle.containsKey(METADATA_KEY_DURATION)) {
                    metadataEditor.putLong(9, bundle.getLong(METADATA_KEY_DURATION));
                }
                if (bundle.containsKey(METADATA_KEY_GENRE)) {
                    metadataEditor.putString(6, bundle.getString(METADATA_KEY_GENRE));
                }
                if (bundle.containsKey(METADATA_KEY_TITLE)) {
                    metadataEditor.putString(7, bundle.getString(METADATA_KEY_TITLE));
                }
                if (bundle.containsKey(METADATA_KEY_TRACK_NUMBER)) {
                    metadataEditor.putLong(0, bundle.getLong(METADATA_KEY_TRACK_NUMBER));
                }
                if (bundle.containsKey(METADATA_KEY_WRITER)) break block19;
            }
            return;
        }
        metadataEditor.putString(11, bundle.getString(METADATA_KEY_WRITER));
    }

    public static Object createRemoteControlClient(PendingIntent pendingIntent) {
        return new RemoteControlClient(pendingIntent);
    }

    static int getRccStateFromState(int n2) {
        switch (n2) {
            default: {
                return -1;
            }
            case 6: 
            case 8: {
                return 8;
            }
            case 7: {
                return 9;
            }
            case 4: {
                return 4;
            }
            case 0: {
                return 0;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
            case 5: {
                return 5;
            }
            case 9: {
                return 7;
            }
            case 10: 
            case 11: {
                return 6;
            }
            case 1: 
        }
        return 1;
    }

    static int getRccTransportControlFlagsFromActions(long l2) {
        int n2 = 0;
        if ((1L & l2) != 0L) {
            n2 = 0 | 0x20;
        }
        int n3 = n2;
        if ((2L & l2) != 0L) {
            n3 = n2 | 0x10;
        }
        n2 = n3;
        if ((4L & l2) != 0L) {
            n2 = n3 | 4;
        }
        n3 = n2;
        if ((8L & l2) != 0L) {
            n3 = n2 | 2;
        }
        n2 = n3;
        if ((0x10L & l2) != 0L) {
            n2 = n3 | 1;
        }
        n3 = n2;
        if ((0x20L & l2) != 0L) {
            n3 = n2 | 0x80;
        }
        n2 = n3;
        if ((0x40L & l2) != 0L) {
            n2 = n3 | 0x40;
        }
        n3 = n2;
        if ((0x200L & l2) != 0L) {
            n3 = n2 | 8;
        }
        return n3;
    }

    public static void registerRemoteControlClient(Context context, Object object) {
        ((AudioManager)context.getSystemService("audio")).registerRemoteControlClient((RemoteControlClient)object);
    }

    public static void setMetadata(Object object, Bundle bundle) {
        object = ((RemoteControlClient)object).editMetadata(true);
        MediaSessionCompatApi14.buildOldMetadata(bundle, (RemoteControlClient.MetadataEditor)object);
        object.apply();
    }

    public static void setState(Object object, int n2) {
        ((RemoteControlClient)object).setPlaybackState(MediaSessionCompatApi14.getRccStateFromState(n2));
    }

    public static void setTransportControlFlags(Object object, long l2) {
        ((RemoteControlClient)object).setTransportControlFlags(MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(l2));
    }

    public static void unregisterRemoteControlClient(Context context, Object object) {
        ((AudioManager)context.getSystemService("audio")).unregisterRemoteControlClient((RemoteControlClient)object);
    }
}

