package org.eth.hexutil;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.Arrays;

public class HexUtil {

    // Custom Exceptions for Error Handling
    public static class HexUtilException extends Exception {
        public HexUtilException(String message) {
            super(message);
        }
    }

    public static class ErrEmptyString extends HexUtilException {
        public ErrEmptyString() {
            super("Empty hex string");
        }
    }

    public static class ErrSyntax extends HexUtilException {
        public ErrSyntax() {
            super("Invalid hex string");
        }
    }

    public static class ErrMissingPrefix extends HexUtilException {
        public ErrMissingPrefix() {
            super("Hex string without 0x prefix");
        }
    }

    public static class ErrOddLength extends HexUtilException {
        public ErrOddLength() {
            super("Hex string of odd length");
        }
    }

    public static class ErrEmptyNumber extends HexUtilException {
        public ErrEmptyNumber() {
            super("Hex string \"0x\"");
        }
    }

    public static class ErrLeadingZero extends HexUtilException {
        public ErrLeadingZero() {
            super("Hex number with leading zero digits");
        }
    }

    public static class ErrUint64Range extends HexUtilException {
        public ErrUint64Range() {
            super("Hex number > 64 bits");
        }
    }

    public static class ErrBig256Range extends HexUtilException {
        public ErrBig256Range() {
            super("Hex number > 256 bits");
        }
    }

    // Decode a hex string with 0x prefix to a byte array
    public static byte[] decode(String input) throws HexUtilException {
        if (input == null || input.isEmpty()) {
            throw new ErrEmptyString();
        }
        if (!input.startsWith("0x")) {
            throw new ErrMissingPrefix();
        }
        String hexString = input.substring(2);
        if (hexString.length() % 2 != 0) {
            throw new ErrOddLength();
        }
        try {
            return Hex.decodeHex(hexString);
        } catch (Exception e) {
            throw new ErrSyntax();
        }
    }

    // Encode a byte array to a hex string with 0x prefix
    public static String encode(byte[] bytes) {
        return "0x" + byteArrayToHexString(bytes);
    }

    // Decode a hex string with 0x prefix to a BigInteger
    public static BigInteger decodeBig(String input) throws HexUtilException {
        String raw = checkNumber(input);
        if (raw.length() > 64) {
            throw new ErrBig256Range();
        }
        return new BigInteger(raw, 16);
    }

    // Encode a BigInteger to a hex string with 0x prefix
    public static String encodeBig(BigInteger bigint) {
        String string = bigint.toString(16);
        if(bigint.signum()<0) {
            return "-0x" + string.substring(1);
        }
        return "0x" + string;
    }

    // Decode a hex string with 0x prefix to a long
    public static long decodeUint64(String input) throws HexUtilException {
        String raw = checkNumber(input);
        if (raw.length() > 16) {
            throw new ErrUint64Range();
        }
        try {
            return Long.parseLong(raw, 16);
        } catch (NumberFormatException e) {
            throw new ErrSyntax();
        }
    }

    // Encode a long to a hex string with 0x prefix
    public static String encodeUint64(long value) {
        return "0x" + Long.toHexString(value);
    }

    private static String checkNumber(String input) throws HexUtilException {
        if (input == null || input.isEmpty()) {
            throw new ErrEmptyString();
        }
        if (!input.startsWith("0x")) {
            throw new ErrMissingPrefix();
        }
        input = input.substring(2);
        if (input.isEmpty()) {
            throw new ErrEmptyNumber();
        }
        if (input.length() > 1 && input.startsWith("0")) {
            throw new ErrLeadingZero();
        }
        return input;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            // Example Usage
            byte[] encodedBytes = decode("0x48656c6c6f20576f726c64"); // "Hello World"
            String encodedString = encode(encodedBytes);
            System.out.println("Encoded String: " + encodedString);

            BigInteger bigInt = new BigInteger("112233445566778899aabbccddeeff", 16);
            String bigIntEncoded = encodeBig(bigInt);
            System.out.println("BigInt Encoded: " + bigIntEncoded);

            long value = 123456789L;
            String uint64Encoded = encodeUint64(value);
            System.out.println("Uint64 Encoded: " + uint64Encoded);

        } catch (HexUtilException e) {
            e.printStackTrace();
        }
    }
}
