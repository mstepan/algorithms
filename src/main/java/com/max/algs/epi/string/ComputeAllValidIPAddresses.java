package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;


public class ComputeAllValidIPAddresses {

    private static final Logger LOG = Logger.getLogger(ComputeAllValidIPAddresses.class);

    private static final int IP4_OCTETS_COUNT = 4;
    private static final int MAX_IP4_OCTET_VALUE = 255;

    private static final int MIN_IP4_STR_LENGTH = IP4_OCTETS_COUNT;
    private static final int MAX_IP4_STR_LENGTH = 12;

    private static final int ZERO_CHARACTER_CODE = '0';

    private ComputeAllValidIPAddresses() throws Exception {

        printAllIPs("19216811");
//        printAllIPs("79216811");

//        printAllIPs("19216a11");
//        printAllIPs("0000");
//        printAllIPs("255255255355");

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 7.10. Compute all valid IP addresses.
     * <p>
     * time: O(3^4) ~ O(1)
     * space: O(1)
     */
    public static void printAllIPs(String ipStr) {
        checkValidIPString(ipStr);
        printAllIPsRec(ipStr.toCharArray(), 0, new int[IP4_OCTETS_COUNT], 0);
    }

    private static void printAllIPsRec(char[] arr, int index, int[] res, int resIndex) {

        boolean arrEnded = (index == arr.length);
        boolean resEnded = (resIndex == res.length);

        if (arrEnded && resEnded) {
            System.out.println(toIP4String(res));
            return;
        }

        if (arrEnded ^ resEnded) {
            return;
        }

        int singleValue = 0;
        for (int i = index; i < arr.length; ++i) {
            singleValue = 10 * singleValue + (arr[i] - ZERO_CHARACTER_CODE);

            if (singleValue > MAX_IP4_OCTET_VALUE) {
                break;
            }

            res[resIndex] = singleValue;
            printAllIPsRec(arr, i + 1, res, resIndex + 1);
        }
    }

    private static String toIP4String(int[] arr) {

        StringBuilder buf = new StringBuilder(MAX_IP4_STR_LENGTH);

        for (int singleOctetValue : arr) {
            buf.append(singleOctetValue).append(".");
        }

        buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    private static void checkValidIPString(String ipStr) {
        checkArgument(ipStr != null, "'ipStr' parameter is null");
        checkArgument(ipStr.length() >= MIN_IP4_STR_LENGTH && ipStr.length() <= MAX_IP4_STR_LENGTH,
                "IP address very small or very big: %s", ipStr);

        for (int i = 0, ipStrLength = ipStr.length(); i < ipStrLength; ++i) {
            if (!Character.isDigit(ipStr.charAt(i))) {
                throw new IllegalArgumentException("'" + ipStr + "' can't be a valid IP address.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ComputeAllValidIPAddresses();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
