package org.eth.common;

import org.eth.hexutil.HexUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

import static org.eth.common.CommonByte.isHex;

// Lengths of hashes and addresses in bytes.
public final class CommonTypes {
    public static final int HASH_LENGTH = 32;
    public static final int ADDRESS_LENGTH = 20;

    // Hash represents the 32 byte Keccak256 hash of arbitrary data.
    public static class Hash {
        private final byte[] bytes;

        private Hash(byte[] bytes) {
            if (bytes.length != HASH_LENGTH) {
                throw new IllegalArgumentException("Hash must be " + HASH_LENGTH + " bytes long");
            }
            this.bytes = Arrays.copyOf(bytes, bytes.length);
        }

        public static Hash fromBytes(byte[] bytes) {
            return new Hash(bytes);
        }

        public static Hash fromBigInteger(BigInteger bigInteger) {
            return fromBytes(bigInteger.toByteArray());
        }

        public static Hash fromHexString(String hexString) throws HexUtil.HexUtilException {
            return fromBytes(HexUtil.decode(hexString));
        }

        public byte[] getBytes() {
            return Arrays.copyOf(bytes, bytes.length);
        }

        public BigInteger toBigInteger() {
            return new BigInteger(1, bytes);
        }

        public String toHexString() {
            return HexUtil.encode(bytes);
        }

        @Override
        public String toString() {
            return toHexString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Hash hash = (Hash) o;
            return Arrays.equals(bytes, hash.bytes);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }

        public static final class Formatter extends Format {
            @Override
            public StringBuffer format(Object obj, StringBuffer buffer, FieldPosition fieldPosition) {
                if (obj instanceof Hash) {
                    Hash hash = (Hash) obj;
                    String prefix = "0x";
                    buffer.append(prefix);
                    buffer.append(HexUtil.encode(hash.getBytes()));
                } else {
                    throw new IllegalArgumentException("Object must be of type Hash");
                }
                return buffer;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        }

        // Similar implementations for Scan, Value, ImplementsGraphQLType and UnmarshalGraphQL
        // can be defined here using corresponding Java libraries like JdbcTemplate or Jackson.
    }

    // Address represents the 20 byte address of an Ethereum account.
    public static class Address {
        private final byte[] bytes;

        private Address(byte[] bytes) {
            if (bytes.length != ADDRESS_LENGTH) {
                throw new IllegalArgumentException("Address must be " + ADDRESS_LENGTH + " bytes long");
            }
            this.bytes = Arrays.copyOf(bytes, bytes.length);
        }

        public static Address fromBytes(byte[] bytes) {
            return new Address(bytes);
        }

        public static Address fromBigInteger(BigInteger bigInteger) {
            return fromBytes(bigInteger.toByteArray());
        }

        public static Address fromHexString(String hexString) throws HexUtil.HexUtilException {
            return fromBytes(HexUtil.decode(hexString));
        }

        public static boolean isHexStringAddress(String hexString) {
            if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
                hexString = hexString.substring(2);
            }
            return hexString.length() == 2 * ADDRESS_LENGTH && isHex(hexString);
        }

        public byte[] getBytes() {
            return Arrays.copyOf(bytes, bytes.length);
        }

        public Hash toHash() {
            return Hash.fromBytes(bytes);
        }

        public String toHexString() {
            return checksumAddress(bytes);
        }

        @Override
        public String toString() {
            return toHexString();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Address address = (Address)
            o;
            return Arrays.equals(bytes, address.bytes);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);

        }

        public static final class Formatter extends Format {
            @Override
            public StringBuffer format(Object obj, StringBuffer buffer, FieldPosition fieldPosition) {
                if (obj instanceof Address) {
                    Address address = (Address) obj;
                    String prefix = "0x";
                    buffer.append(prefix);
                    buffer.append(address.toHexString());
                } else {
                    throw new IllegalArgumentException("Object must be of type Address");
                }
                return buffer;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        }

        // Similar implementations for Scan, Value, ImplementsGraphQLType and UnmarshalGraphQL
        // can be defined here using corresponding Java libraries like JdbcTemplate or Jackson.

        private static String checksumAddress(byte[] addressBytes) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA3-256");
                digest.update(addressBytes);
                byte[] hash = digest.digest();

                StringBuilder hexString = new StringBuilder("0x");
                for (int i = 2; i < addressBytes.length; i++) {
                    byte hashByte = hash[(i - 2) / 2];
                    if (i % 2 == 0) {
                        hashByte >>= 4;
                    } else {
                        hashByte &= 0xf;
                    }
                    if (addressBytes[i] > '9' && hashByte > 7) {
                        addressBytes[i] -= 32;
                    }
                    hexString.append(String.format("%02x", addressBytes[i]));
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // UnprefixedHash allows marshaling a Hash without 0x prefix.
    public static class UnprefixedHash extends Hash {
        public UnprefixedHash(byte[] bytes) {
            super(bytes);
        }

        public static UnprefixedHash fromBytes(byte[] bytes) {
            return new UnprefixedHash(bytes);
        }

        public static UnprefixedHash fromBigInteger(BigInteger bigInteger) {
            return new UnprefixedHash(bigInteger.toByteArray());
        }

        public static UnprefixedHash fromHexString(String hexString) throws HexUtil.HexUtilException {
            return new UnprefixedHash(HexUtil.decode(hexString));
        }

        @Override
        public String toHexString() {
            return super.toHexString().substring(2);
        }
    }

    // UnprefixedAddress allows marshaling an Address without 0x prefix.
    public static class UnprefixedAddress extends Address {
        public UnprefixedAddress(byte[] bytes) {
            super(bytes);
        }

        public static UnprefixedAddress fromBytes(byte[] bytes) {
            return new UnprefixedAddress(bytes);
        }

        public static UnprefixedAddress fromBigInteger(BigInteger bigInteger) {
            return new UnprefixedAddress(bigInteger.toByteArray());
        }

        public static UnprefixedAddress fromHexString(String hexString) throws HexUtil.HexUtilException {
            return new UnprefixedAddress(HexUtil.decode(hexString));
        }

        @Override
        public String toHexString() {
            return super.toHexString().substring(2);
        }
    }

    // MixedcaseAddress retains the original string, which may or may not be
    // correctly checksummed
    public static class MixedcaseAddress {
        private final Address address;
        private final String original;

        public MixedcaseAddress(Address address, String original) {
            this.address = address;
            this.original = original;
        }

        public static MixedcaseAddress fromAddress(Address address) {
            return new MixedcaseAddress(address, address.toHexString());
        }

        public static MixedcaseAddress fromHexString(String hexString) throws HexUtil.HexUtilException {
            if (!Address.isHexStringAddress(hexString)) {
                throw new IllegalArgumentException("Invalid address: " + hexString);
            }
            return new MixedcaseAddress(Address.fromHexString(hexString), hexString);
        }

        public Address getAddress() {
            return address;
        }

        public String getOriginal() {
            return original;
        }

        @Override
        public String toString() {
            return String.format("%s [%s]", original, isValidChecksum() ? "checksum ok" : "checksum INVALID");
        }

        public boolean isValidChecksum() {
            return original.equals(address.toHexString());
        }
    }
    }