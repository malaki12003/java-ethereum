package org.eth.hexutil;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import static org.eth.hexutil.HexUtil.*;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class HexUtilTest {

    static class MarshalTest {
        Object input;
        String want;

        MarshalTest(Object input, String want) {
            this.input = input;
            this.want = want;
        }
    }

    static class UnmarshalTest {
        String input;
        Object want;
        Exception wantErr;

        UnmarshalTest(String input, Object want, Exception wantErr) {
            this.input = input;
            this.want = want;
            this.wantErr = wantErr;
        }
    }

    private static final MarshalTest[] encodeBytesTests = {
            new MarshalTest(new byte[]{}, "0x"),
            new MarshalTest(new byte[]{0}, "0x00"),
            new MarshalTest(new byte[]{0, 0, 1, 2}, "0x00000102")
    };

    private static final MarshalTest[] encodeBigTests = {
            new MarshalTest(new BigInteger("0"), "0x0"),
            new MarshalTest(new BigInteger("1"), "0x1"),
            new MarshalTest(new BigInteger("ff", 16), "0xff"),
            new MarshalTest(new BigInteger("112233445566778899aabbccddeeff", 16), "0x112233445566778899aabbccddeeff"),
            new MarshalTest(new BigInteger("80a7f2c1bcc396c00", 16), "0x80a7f2c1bcc396c00"),
            new MarshalTest(new BigInteger("-80a7f2c1bcc396c00", 16), "-0x80a7f2c1bcc396c00")
    };

    private static final MarshalTest[] encodeUint64Tests = {
            new MarshalTest(0L, "0x0"),
            new MarshalTest(1L, "0x1"),
            new MarshalTest(0xffL, "0xff"),
            new MarshalTest(0x1122334455667788L, "0x1122334455667788")
    };

    private static final UnmarshalTest[] decodeBytesTests = {
            new UnmarshalTest("", null, new IllegalArgumentException("Empty hex string")),
            new UnmarshalTest("0", null, new IllegalArgumentException("Hex string without 0x prefix")),
            new UnmarshalTest("0x0", null, new IllegalArgumentException("Hex string of odd length")),
            new UnmarshalTest("0x023", null, new IllegalArgumentException("Hex string of odd length")),
            new UnmarshalTest("0xxx", null, new IllegalArgumentException("Invalid hex string")),
            new UnmarshalTest("0x01zz01", null, new IllegalArgumentException("Invalid hex string")),
            new UnmarshalTest("0x", new byte[]{}, null),
            new UnmarshalTest("0x02", new byte[]{0x02}, null),
            new UnmarshalTest("0xffffffffff", new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}, null)
    };

    @Test
    public void testEncodeBytes() {
        for (MarshalTest test : encodeBytesTests) {
            String enc = encode((byte[]) test.input);
            assertEquals( test.want, enc,"Input: " + Arrays.toString((byte[]) test.input));
        }
    }

    @Test
    public void testDecodeBytes() {
        for (UnmarshalTest test : decodeBytesTests) {
            try {
                byte[] dec = decode(test.input);
                assertArrayEquals((byte[]) test.want, dec, "Input: " + test.input);
            } catch (Exception e) {
                assertEquals(test.wantErr.getMessage(), e.getMessage());
            }
        }
    }

    @Test
    public void testEncodeBig() {
        for (MarshalTest test : encodeBigTests) {
            String enc = encodeBig((BigInteger) test.input);
            assertEquals( test.want, enc,"Input: " + test.input);
        }
    }

    @Test
    public void testEncodeUint64() {
        for (MarshalTest test : encodeUint64Tests) {
            String enc = encodeUint64((Long) test.input);
            assertEquals(test.want, enc,"Input: " + test.input);
        }
    }

}