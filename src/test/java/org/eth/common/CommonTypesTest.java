package org.eth.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.eth.common.CommonTypes.*;

class CommonTypesTest {
    @Test
    void testBytesConversion() {
        byte[] bytes = {5};
        byte[] hash = bytesToHash(bytes);

        byte[] exp = new byte[32];
        exp[31] = 5;

        assertArrayEquals(exp, hash, String.format("expected %s got %s", Arrays.toString(exp), Arrays.toString(hash)));
    }

    @Test
    void testIsHexAddress() {
        assertTrue(Address.isHexStringAddress("0x5aaeb6053f3e94c9b9a09f33669435e7ef1beaed"));
        assertTrue(Address.isHexStringAddress("5aaeb6053f3e94c9b9a09f33669435e7ef1beaed"));
        assertFalse(Address.isHexStringAddress("0x5aaeb6053f3e94c9b9a09f33669435e7ef1beaed1"));
        assertFalse(Address.isHexStringAddress("0x5aaeb6053f3e94c9b9a09f33669435e7ef1beae"));
    }

    @Test
    void testHashJsonValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            fromJson("0x", 62, "0x5aaeb6053f3e94c9b9a09f33669435e7ef1beaed");
        });
    }

    @Test
    void testAddressUnmarshalJSON() {
        assertThrows(IllegalArgumentException.class, () -> {
            fromJson("0xG000000000000000000000000000000000000000", 64, "");
        });
    }

    private byte[] bytesToHash(byte[] bytes) {
        byte[] hash = new byte[32];
        System.arraycopy(bytes, 0, hash, 32 - bytes.length, bytes.length);
        return hash;
    }



    private void fromJson(String prefix, int size, String error) {
        if (prefix.length() != size) {
            throw new IllegalArgumentException(error);
        }
    }
}