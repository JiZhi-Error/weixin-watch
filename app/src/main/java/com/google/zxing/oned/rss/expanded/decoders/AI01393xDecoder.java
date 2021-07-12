/*
 * Decompiled with CFR 0.151.
 */
package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01decoder;

final class AI01393xDecoder
extends AI01decoder {
    private static final int FIRST_THREE_DIGITS_SIZE = 10;
    private static final int HEADER_SIZE = 8;
    private static final int LAST_DIGIT_SIZE = 2;

    AI01393xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    @Override
    public String parseInformation() throws NotFoundException, FormatException {
        if (this.getInformation().getSize() < 48) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuilder stringBuilder = new StringBuilder();
        this.encodeCompressedGtin(stringBuilder, 8);
        int n2 = this.getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
        stringBuilder.append("(393");
        stringBuilder.append(n2);
        stringBuilder.append(')');
        n2 = this.getGeneralDecoder().extractNumericValueFromBitArray(50, 10);
        if (n2 / 100 == 0) {
            stringBuilder.append('0');
        }
        if (n2 / 10 == 0) {
            stringBuilder.append('0');
        }
        stringBuilder.append(n2);
        stringBuilder.append(this.getGeneralDecoder().decodeGeneralPurposeField(60, null).getNewString());
        return stringBuilder.toString();
    }
}

