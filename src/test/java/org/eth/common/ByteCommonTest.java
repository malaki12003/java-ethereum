package org.eth.common;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ByteCommonTest {

    @Test
    public void testCopyBytes() {
        byte[] input = {1, 2, 3, 4};

        byte[] v = CommonByte.copyBytes(input);
        assertTrue(Arrays.equals(v, input));
        v[0] = 99;
        assertFalse(Arrays.equals(v, input));
    }

    @Test
    public void testLeftPadBytes() {
        byte[] val = {1, 2, 3, 4};
        byte[] padded = {0, 0, 0, 0, 1, 2, 3, 4};

        assertArrayEquals(CommonByte.leftPadBytes(val, 8), padded);
        assertArrayEquals(CommonByte.leftPadBytes(val, 2), val);
    }

    @Test
    public void testRightPadBytes() {
        byte[] val = {1, 2, 3, 4};
        byte[] padded = {1, 2, 3, 4, 0, 0, 0, 0};

        assertArrayEquals(CommonByte.rightPadBytes(val, 8), padded);
        assertArrayEquals(CommonByte.rightPadBytes(val, 2), val);
    }

    @Test
    public void testFromHex() throws DecoderException {
        String input = "0x01";
        byte[] expected = {1};
        assertArrayEquals(CommonByte.fromHex(input), expected);
    }

    @Test
    public void testIsHex() {
        String[] tests = {
                "", "0", "00", "a9e67e", "A9E67E", "0xa9e67e", "a9e67e001", "0xHELLO_MY_NAME_IS_STEVEN_@#$^&*"
        };
        boolean[] expected = {true, false, true, true, true, false, false, false};

        for (int i = 0; i < tests.length; i++) {
            assertEquals(CommonByte.isHex(tests[i]), expected[i]);
        }
    }

    @Test
    public void testFromHexOddLength() throws DecoderException {
        String input = "0x1";
        byte[] expected = {1};
        assertArrayEquals(CommonByte.fromHex(input), expected);
    }

    @Test
    public void testNoPrefixShortHexOddLength() throws DecoderException {
        String input = "1";
        byte[] expected = {1};
        assertArrayEquals(CommonByte.fromHex(input), expected);
    }
//
    @Test
    public void testTrimRightZeroes() {
        byte[][] tests = {
                {1, 0, -1, -1, 0, -1, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {-1}, {}, {0, 0, -1, -1, -1, -1, -1, -1}
        };
        byte[][] expected = {
                {1, 0, -1, -1, 0, -1}, {}, {-1}, {}, {0, 0, -1, -1, -1, -1, -1, -1}
        };

        for (int i = 0; i < tests.length; i++) {
            assertArrayEquals(CommonByte.trimRightZeroes(tests[i]), expected[i]);
        }
    }
}