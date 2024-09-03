package org.eth.common;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFormat {
    // A class representing a duration with a pretty-printed string representation.
    public static class PrettyDuration implements java.lang.CharSequence {

        private final Duration duration;
        private static final Pattern prettyDurationRe = Pattern.compile("\\.[0-9]+");

        public PrettyDuration(Duration duration) {
            this.duration = duration;
        }

        @Override
        public int length() {
            return String.valueOf(duration).length();
        }

        @Override
        public char charAt(int index) {
            return String.valueOf(duration).charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return String.valueOf(duration).subSequence(start, end);
        }

        // Overrides the default toString() method to provide a prettier representation of the duration,
        // removing unnecessary precision beyond three decimal places.
        @Override
        public String toString() {
            String label = duration.toString();
            Matcher matcher = prettyDurationRe.matcher(label);
            if (matcher.find() && matcher.group().length() > 4) {
                label = label.replaceFirst(matcher.group(), matcher.group().substring(0, 4));
            }
            return label;
        }
    }

    // A class representing a timestamp with a pretty-printed string representation.
    public static class PrettyAge implements java.lang.CharSequence {

        private final long timestamp;
        private static final List<Unit> ageUnits = new ArrayList<>();

        static {
            ageUnits.add(new Unit(12 * 30 * 24 * 60 * 60L, "y"));
            ageUnits.add(new Unit(30 * 24 * 60 * 60L, "mo"));
            ageUnits.add(new Unit(7 * 24 * 60 * 60L, "w"));
            ageUnits.add(new Unit(24 * 60 * 60L, "d"));
            ageUnits.add(new Unit(60 * 60L, "h"));
            ageUnits.add(new Unit(60L, "m"));
            ageUnits.add(new Unit(1L, "s"));
        }

        public PrettyAge(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public int length() {
            return String.valueOf(toPrettyAge()).length();
        }

        @Override
        public char charAt(int index) {
            return toPrettyAge().charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return toPrettyAge().subSequence(start, end);
        }

        // Overrides the default toString() method to provide a human-readable representation
        // of the age in the most significant unit (years, months, weeks, etc.).
        @Override
        public String toString() {
            return toPrettyAge();
        }

        private String toPrettyAge() {
            long diff = System.currentTimeMillis() - timestamp;
            if (diff < 1000) {
                return "0";
            }

            StringBuilder result = new StringBuilder();
            int prec = 0;

            for (Unit unit : ageUnits) {
                if (diff > unit.getSize()) {
                    long value = diff / unit.getSize();
                    result.append(String.format("%d%s", value, unit.getSymbol()));
                    diff %= unit.getSize();

                    if (++prec >= 3) {
                        break;
                    }
                }
            }
            return result.toString();
        }

        private static class Unit {
            private final long size;
            private final String symbol;

            public Unit(long size, String symbol) {
                this.size = size;
                this.symbol = symbol;
            }

            public long getSize() {
                return size;
            }

            public String getSymbol() {
                return symbol;
            }
        }
    }
}