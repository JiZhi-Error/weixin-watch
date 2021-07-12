/*
 * Decompiled with CFR 0.151.
 */
package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mMergedSize;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public SortedList(Class<T> clazz, Callback<T> callback) {
        this(clazz, callback, 10);
    }

    public SortedList(Class<T> clazz, Callback<T> callback, int n2) {
        this.mTClass = clazz;
        this.mData = (Object[])Array.newInstance(clazz, n2);
        this.mCallback = callback;
        this.mSize = 0;
    }

    /*
     * Unable to fully structure code
     */
    private int add(T var1_1, boolean var2_2) {
        var4_3 = this.findIndexOf(var1_1, this.mData, 0, this.mSize, 1);
        if (var4_3 == -1) {
            var3_4 = 0;
lbl4:
            // 3 sources

            while (true) {
                this.addToData(var3_4, var1_1);
                if (var2_2) {
                    this.mCallback.onInserted(var3_4, 1);
                }
                return var3_4;
            }
        }
        var3_4 = var4_3;
        if (var4_3 >= this.mSize) ** GOTO lbl4
        var5_5 = this.mData[var4_3];
        var3_4 = var4_3;
        if (this.mCallback.areItemsTheSame(var5_5, var1_1)) ** break;
        ** while (true)
        if (this.mCallback.areContentsTheSame(var5_5, var1_1)) {
            this.mData[var4_3] = var1_1;
            return var4_3;
        }
        this.mData[var4_3] = var1_1;
        this.mCallback.onChanged(var4_3, 1);
        return var4_3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addAllInternal(T[] TArray) {
        boolean bl2 = !(this.mCallback instanceof BatchedCallback);
        if (bl2) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        Arrays.sort(TArray, this.mCallback);
        int n2 = this.deduplicate(TArray);
        if (this.mSize == 0) {
            this.mData = TArray;
            this.mSize = n2;
            this.mMergedSize = n2;
            this.mCallback.onInserted(0, n2);
        } else {
            this.merge(TArray, n2);
        }
        this.mOldData = null;
        if (bl2) {
            this.endBatchedUpdates();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addToData(int n2, T t2) {
        if (n2 > this.mSize) {
            throw new IndexOutOfBoundsException("cannot add item to " + n2 + " because size is " + this.mSize);
        }
        if (this.mSize == this.mData.length) {
            Object[] objectArray = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, objectArray, 0, n2);
            objectArray[n2] = t2;
            System.arraycopy(this.mData, n2, objectArray, n2 + 1, this.mSize - n2);
            this.mData = objectArray;
        } else {
            System.arraycopy(this.mData, n2, this.mData, n2 + 1, this.mSize - n2);
            this.mData[n2] = t2;
        }
        ++this.mSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int deduplicate(T[] TArray) {
        if (TArray.length == 0) {
            throw new IllegalArgumentException("Input array must be non-empty");
        }
        int n2 = 0;
        int n3 = 1;
        int n4 = 1;
        while (n4 < TArray.length) {
            T t2 = TArray[n4];
            int n5 = this.mCallback.compare(TArray[n2], t2);
            if (n5 > 0) {
                throw new IllegalArgumentException("Input must be sorted in ascending order.");
            }
            if (n5 == 0) {
                n5 = this.findSameItem(t2, TArray, n2, n3);
                if (n5 != -1) {
                    TArray[n5] = t2;
                } else {
                    if (n3 != n4) {
                        TArray[n3] = t2;
                    }
                    ++n3;
                }
            } else {
                if (n3 != n4) {
                    TArray[n3] = t2;
                }
                n2 = n3++;
            }
            ++n4;
        }
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int findIndexOf(T t2, T[] TArray, int n2, int n3, int n4) {
        while (n2 < n3) {
            int n5 = (n2 + n3) / 2;
            T t3 = TArray[n5];
            int n6 = this.mCallback.compare(t3, t2);
            if (n6 < 0) {
                n2 = n5 + 1;
                continue;
            }
            if (n6 == 0) {
                if (this.mCallback.areItemsTheSame(t3, t2)) {
                    return n5;
                }
                n2 = this.linearEqualitySearch(t2, n5, n2, n3);
                if (n4 != 1) return n2;
                if (n2 == -1) return n5;
                return n2;
            }
            n3 = n5;
        }
        if (n4 != 1) return -1;
        return n2;
    }

    private int findSameItem(T t2, T[] TArray, int n2, int n3) {
        while (n2 < n3) {
            if (this.mCallback.areItemsTheSame(TArray[n2], t2)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int linearEqualitySearch(T t2, int n2, int n3, int n4) {
        T t3;
        int n5 = n2 - 1;
        while (true) {
            if (n5 < n3 || this.mCallback.compare(t3 = this.mData[n5], t2) != 0) {
                ++n2;
                break;
            }
            if (this.mCallback.areItemsTheSame(t3, t2)) {
                return n5;
            }
            --n5;
        }
        while (n2 < n4 && this.mCallback.compare(t3 = this.mData[n2], t2) == 0) {
            if (this.mCallback.areItemsTheSame(t3, t2)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    private void merge(T[] TArray, int n2) {
        int n3 = this.mSize;
        this.mData = (Object[])Array.newInstance(this.mTClass, n3 + n2 + 10);
        this.mMergedSize = 0;
        n3 = 0;
        while (true) {
            block8: {
                block7: {
                    if (this.mOldDataStart >= this.mOldDataSize && n3 >= n2) break block7;
                    if (this.mOldDataStart != this.mOldDataSize) break block8;
                    System.arraycopy(TArray, n3, this.mData, this.mMergedSize, n2 -= n3);
                    this.mMergedSize += n2;
                    this.mSize += n2;
                    this.mCallback.onInserted(this.mMergedSize - n2, n2);
                }
                return;
            }
            if (n3 == n2) {
                n2 = this.mOldDataSize - this.mOldDataStart;
                System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, n2);
                this.mMergedSize += n2;
                return;
            }
            Object object = this.mOldData[this.mOldDataStart];
            Object object2 = TArray[n3];
            int n4 = this.mCallback.compare(object, object2);
            if (n4 > 0) {
                object = this.mData;
                n4 = this.mMergedSize;
                this.mMergedSize = n4 + 1;
                object[n4] = object2;
                ++this.mSize;
                ++n3;
                this.mCallback.onInserted(this.mMergedSize - 1, 1);
                continue;
            }
            if (n4 == 0 && this.mCallback.areItemsTheSame(object, object2)) {
                T[] TArray2 = this.mData;
                n4 = this.mMergedSize;
                this.mMergedSize = n4 + 1;
                TArray2[n4] = object2;
                n4 = n3 + 1;
                ++this.mOldDataStart;
                n3 = n4;
                if (this.mCallback.areContentsTheSame(object, object2)) continue;
                this.mCallback.onChanged(this.mMergedSize - 1, 1);
                n3 = n4;
                continue;
            }
            object2 = this.mData;
            n4 = this.mMergedSize;
            this.mMergedSize = n4 + 1;
            object2[n4] = object;
            ++this.mOldDataStart;
        }
    }

    private boolean remove(T t2, boolean bl2) {
        int n2 = this.findIndexOf(t2, this.mData, 0, this.mSize, 2);
        if (n2 == -1) {
            return false;
        }
        this.removeItemAtIndex(n2, bl2);
        return true;
    }

    private void removeItemAtIndex(int n2, boolean bl2) {
        System.arraycopy(this.mData, n2 + 1, this.mData, n2, this.mSize - n2 - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (bl2) {
            this.mCallback.onRemoved(n2, 1);
        }
    }

    private void throwIfMerging() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Cannot call this method from within addAll");
        }
    }

    public int add(T t2) {
        this.throwIfMerging();
        return this.add(t2, true);
    }

    public void addAll(Collection<T> collection) {
        this.addAll(collection.toArray((Object[])Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void addAll(T ... TArray) {
        this.addAll(TArray, false);
    }

    public void addAll(T[] TArray, boolean bl2) {
        this.throwIfMerging();
        if (TArray.length == 0) {
            return;
        }
        if (bl2) {
            this.addAllInternal(TArray);
            return;
        }
        Object[] objectArray = (Object[])Array.newInstance(this.mTClass, TArray.length);
        System.arraycopy(TArray, 0, objectArray, 0, TArray.length);
        this.addAllInternal(objectArray);
    }

    public void beginBatchedUpdates() {
        this.throwIfMerging();
        if (this.mCallback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(this.mCallback);
        }
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        this.throwIfMerging();
        if (this.mSize == 0) {
            return;
        }
        int n2 = this.mSize;
        Arrays.fill(this.mData, 0, n2, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, n2);
    }

    public void endBatchedUpdates() {
        this.throwIfMerging();
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback)this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = this.mBatchedCallback.mWrappedCallback;
        }
    }

    public T get(int n2) throws IndexOutOfBoundsException {
        if (n2 >= this.mSize || n2 < 0) {
            throw new IndexOutOfBoundsException("Asked to get item at " + n2 + " but size is " + this.mSize);
        }
        if (this.mOldData != null && n2 >= this.mMergedSize) {
            return this.mOldData[n2 - this.mMergedSize + this.mOldDataStart];
        }
        return this.mData[n2];
    }

    public int indexOf(T t2) {
        if (this.mOldData != null) {
            int n2 = this.findIndexOf(t2, this.mData, 0, this.mMergedSize, 4);
            if (n2 != -1) {
                return n2;
            }
            n2 = this.findIndexOf(t2, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (n2 != -1) {
                return n2 - this.mOldDataStart + this.mMergedSize;
            }
            return -1;
        }
        return this.findIndexOf(t2, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int n2) {
        this.throwIfMerging();
        T t2 = this.get(n2);
        this.removeItemAtIndex(n2, false);
        int n3 = this.add(t2, false);
        if (n2 != n3) {
            this.mCallback.onMoved(n2, n3);
        }
    }

    public boolean remove(T t2) {
        this.throwIfMerging();
        return this.remove(t2, true);
    }

    public T removeItemAt(int n2) {
        this.throwIfMerging();
        T t2 = this.get(n2);
        this.removeItemAtIndex(n2, true);
        return t2;
    }

    public int size() {
        return this.mSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updateItemAt(int n2, T t2) {
        this.throwIfMerging();
        T t3 = this.get(n2);
        int n3 = t3 == t2 || !this.mCallback.areContentsTheSame(t3, t2) ? 1 : 0;
        if (t3 != t2 && this.mCallback.compare(t3, t2) == 0) {
            this.mData[n2] = t2;
            if (n3 == 0) return;
            this.mCallback.onChanged(n2, 1);
            return;
        }
        if (n3 != 0) {
            this.mCallback.onChanged(n2, 1);
        }
        this.removeItemAtIndex(n2, false);
        n3 = this.add(t2, false);
        if (n2 == n3) return;
        this.mCallback.onMoved(n2, n3);
    }

    public static class BatchedCallback<T2>
    extends Callback<T2> {
        static final int TYPE_ADD = 1;
        static final int TYPE_CHANGE = 3;
        static final int TYPE_MOVE = 4;
        static final int TYPE_NONE = 0;
        static final int TYPE_REMOVE = 2;
        int mLastEventCount = -1;
        int mLastEventPosition = -1;
        int mLastEventType = 0;
        private final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
        }

        @Override
        public boolean areContentsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areContentsTheSame(T2, T22);
        }

        @Override
        public boolean areItemsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areItemsTheSame(T2, T22);
        }

        @Override
        public int compare(T2 T2, T2 T22) {
            return this.mWrappedCallback.compare(T2, T22);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void dispatchLastEvent() {
            if (this.mLastEventType == 0) {
                return;
            }
            switch (this.mLastEventType) {
                case 1: {
                    this.mWrappedCallback.onInserted(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
                case 2: {
                    this.mWrappedCallback.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
                case 3: {
                    this.mWrappedCallback.onChanged(this.mLastEventPosition, this.mLastEventCount);
                    break;
                }
            }
            this.mLastEventType = 0;
        }

        @Override
        public void onChanged(int n2, int n3) {
            if (this.mLastEventType == 3 && n2 <= this.mLastEventPosition + this.mLastEventCount && n2 + n3 >= this.mLastEventPosition) {
                int n4 = this.mLastEventPosition;
                int n5 = this.mLastEventCount;
                this.mLastEventPosition = Math.min(n2, this.mLastEventPosition);
                this.mLastEventCount = Math.max(n4 + n5, n2 + n3) - this.mLastEventPosition;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n2;
            this.mLastEventCount = n3;
            this.mLastEventType = 3;
        }

        @Override
        public void onInserted(int n2, int n3) {
            if (this.mLastEventType == 1 && n2 >= this.mLastEventPosition && n2 <= this.mLastEventPosition + this.mLastEventCount) {
                this.mLastEventCount += n3;
                this.mLastEventPosition = Math.min(n2, this.mLastEventPosition);
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n2;
            this.mLastEventCount = n3;
            this.mLastEventType = 1;
        }

        @Override
        public void onMoved(int n2, int n3) {
            this.dispatchLastEvent();
            this.mWrappedCallback.onMoved(n2, n3);
        }

        @Override
        public void onRemoved(int n2, int n3) {
            if (this.mLastEventType == 2 && this.mLastEventPosition == n2) {
                this.mLastEventCount += n3;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n2;
            this.mLastEventCount = n3;
            this.mLastEventType = 2;
        }
    }

    public static abstract class Callback<T2>
    implements Comparator<T2> {
        public abstract boolean areContentsTheSame(T2 var1, T2 var2);

        public abstract boolean areItemsTheSame(T2 var1, T2 var2);

        @Override
        public abstract int compare(T2 var1, T2 var2);

        public abstract void onChanged(int var1, int var2);

        public abstract void onInserted(int var1, int var2);

        public abstract void onMoved(int var1, int var2);

        public abstract void onRemoved(int var1, int var2);
    }
}

