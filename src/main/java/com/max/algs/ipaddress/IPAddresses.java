package com.max.algs.ipaddress;

public final class IPAddresses {

    private IPAddresses() {
        super();
    }

    public IPAddress ip4(int[] adr) {
        return new IP4Address(adr);
    }

}
