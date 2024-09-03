package org.eth.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.eth.common.CommonSize.*;

public class CommonSizeTest {

    @Test
    public void testStorageSizeString() {
        // An array of test cases for StorageSize string formatting
        StorageSize[] tests = {
                new StorageSize(2381273),
                new StorageSize(2192),
                new StorageSize(12)
        };
        String[] expectedStrings = {"2.27 MiB", "2.14 KiB", "12.00 B"};

        for (int i = 0; i < tests.length; i++) {
            if (!tests[i].toString().equals(expectedStrings[i])) {
                // Use assertEquals for JUnit testing framework
               Assertions.assertEquals(String.format("%.2f", (double) tests[i].getValue()), expectedStrings[i]);
            }
        }
    }
}