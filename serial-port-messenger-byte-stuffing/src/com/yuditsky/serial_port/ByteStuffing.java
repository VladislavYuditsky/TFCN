package com.yuditsky.serial_port;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class ByteStuffing {
    public final static byte FLAG = 0x7E;
    public final static byte ESC = 0x7D;
    public final static byte FLAG_REPLACEMENT_CODE = 0x5E;
    public final static byte ESC_REPLACEMENT_CODE = 0x5D;


    public static byte[] doStuffing(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(bytes)));

        list.add(0, FLAG);

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) == ESC) {
                list.add(i + 1, ESC_REPLACEMENT_CODE);
            } else if (list.get(i) == FLAG) {
                list.set(i, ESC);
                list.add(i + 1, FLAG_REPLACEMENT_CODE);
            }
        }

        Byte[] returnBytes = new Byte[list.size()];
        list.toArray(returnBytes);
        return ArrayUtils.toPrimitive(returnBytes);
    }

    public static byte[] inject(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(bytes)));

        list.remove(0);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == ESC) {
                if (list.get(i + 1) == FLAG_REPLACEMENT_CODE) {
                    list.set(i, FLAG);
                    list.remove(i + 1);
                } else if (list.get(i + 1) == ESC_REPLACEMENT_CODE) {
                    list.remove(i + 1);
                }
            }
        }

        Byte[] returnBytes = new Byte[list.size()];
        list.toArray(returnBytes);
        return ArrayUtils.toPrimitive(returnBytes);
    }

}
