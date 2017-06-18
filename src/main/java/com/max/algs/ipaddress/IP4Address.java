package com.max.algs.ipaddress;

/**
 * 32 bits long IPv4 address.
 *
 * @author mstepan
 */
final class IP4Address implements IPAddress {


    private final long value;

    IP4Address(int[] newOctets) {

        if (newOctets == null) {
            throw new IllegalArgumentException("NULL 'octets' array passed");
        }
        if (newOctets.length != 4) {
            throw new IllegalArgumentException("'octets' length not equals to '4': " + newOctets.length);
        }

        value = encodeValue(newOctets);
    }


    public long getValue() {
        return value;
    }


    private long encodeValue(int[] octets) {

        long ipValue = 0;

        for (int i = octets.length - 1; i >= 0; i--) {
            ipValue <<= 8;
            ipValue |= octets[i];
        }
        return ipValue;
    }

    private int[] decodeValue() {

        final int[] octets = new int[4];

        long ipValue = value;

        for (int i = 0; i < octets.length; i++) {
            octets[i] = (int) (ipValue & 0xFF);
            ipValue >>>= 8;
        }

        return octets;
    }


    public String toString() {
        StringBuilder buf = new StringBuilder(64);

        final int[] octets = decodeValue();
        final int endIndex = octets.length - 1;

        buf.append("ip: ");

        for (int i = endIndex; i >= 0; i--) {

            if (i != endIndex) {
                buf.append(".");
            }

            buf.append(octets[i]);
        }

        buf.append(", binary: ").append(value);

        return buf.toString();
    }
}
