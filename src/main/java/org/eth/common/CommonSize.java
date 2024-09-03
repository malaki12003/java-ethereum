package org.eth.common;

public class CommonSize {

    // StorageSize is a class that wraps a float64 value and provides user-friendly formatting.
    public static class StorageSize implements java.io.Serializable {

        private final double value;

        public StorageSize(double value) {
            this.value = value;
        }

        // Overrides the toString() method to provide a human-readable representation
        // of the storage size in the most appropriate unit (TiB, GiB, MiB, KiB, or B).
        @Override
        public String toString() {
            if (value > 1099511627776.0) {
                return String.format("%.2f TiB", value / 1099511627776.0);
            } else if (value > 1073741824.0) {
                return String.format("%.2f GiB", value / 1073741824.0);
            } else if (value > 1048576.0) {
                return String.format("%.2f MiB", value / 1048576.0);
            } else if (value > 1024.0) {
                return String.format("%.2f KiB", value / 1024.0);
            } else {
                return String.format("%.2f B", value);
            }
        }

        // Implements a custom method for console output formatting during logging.
        // This method mirrors the functionality of TerminalString in Go.
        public String toTerminalString() {
            if (value > 1099511627776.0) {
                return String.format("%.2fTiB", value / 1099511627776.0);
            } else if (value > 1073741824.0) {
                return String.format("%.2fGiB", value / 1073741824.0);
            } else if (value > 1048576.0) {
                return String.format("%.2fMiB", value / 1048576.0);
            } else if (value > 1024.0) {
                return String.format("%.2fKiB", value / 1024.0);
            } else {
                return String.format("%.2fB", value);
            }
        }

        public double getValue() {
            return value;
        }
    }
}