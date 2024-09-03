package org.eth.common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

public class CommonByte {

    public static byte[] fromHex(String s) throws DecoderException {
        if (has0xPrefix(s)) {
            s = s.substring(2);
        }
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        return hexToBytes(s);
    }

    public static byte[] copyBytes(byte[] b) {
        if (b == null) {
            return null;
        }
        byte[] copiedBytes = new byte[b.length];
        System.arraycopy(b, 0, copiedBytes, 0, b.length);
        return copiedBytes;
    }

    private static boolean has0xPrefix(String str) {
        return str.length() >= 2 && str.charAt(0) == '0' && (str.charAt(1) == 'x' || str.charAt(1) == 'X');
    }

    private static boolean isHexCharacter(byte c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    static boolean isHex(String str) {
        if (str.length() % 2 != 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!isHexCharacter((byte) c)) {
                return false;
            }
        }
        return true;
    }

    public static String bytesToHex(byte[] d) {
        return new String(Hex.encodeHex(d));
    }

    public static byte[] hexToBytes(String str) throws DecoderException {
        return Hex.decodeHex(str.toCharArray());
    }

    public static byte[] hexToBytesFixed(String str, int flen) throws IllegalArgumentException, DecoderException {
        byte[] h = hexToBytes(str);
        if (h.length == flen) {
            return h;
        } else if (h.length > flen) {
            return Arrays.copyOfRange(h, h.length - flen, h.length);
        } else {
            throw new IllegalArgumentException("Input string length does not match fixed length");
        }
    }

    public static byte[] rightPadBytes(byte[] slice, int l) {
        if (l <= slice.length) {
            return slice;
        }

        byte[] padded = new byte[l];
        System.arraycopy(slice, 0, padded, 0, slice.length);

        return padded;
    }

    public static byte[] leftPadBytes(byte[] slice, int l) {
        if (l <= slice.length) {
            return slice;
        }

        byte[] padded = new byte[l];
        System.arraycopy(slice, 0, padded, l - slice.length, slice.length);

        return padded;
    }

    public static byte[] trimLeftZeroes(byte[] s) {
        int idx = 0;
        while (idx < s.length && s[idx] == 0) {
            idx++;
        }
        return Arrays.copyOfRange(s, idx, s.length);
    }

    public static byte[] trimRightZeroes(byte[] s) {
        int idx = s.length;
        while (idx > 0 && s[idx - 1] == 0) {
            idx--;
        }
        return Arrays.copyOfRange(s, 0, idx);
    }
}