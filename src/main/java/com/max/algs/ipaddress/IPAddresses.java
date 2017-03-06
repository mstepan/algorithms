package com.max.algs.ipaddress;

public final class IPAddresses {

	public IPAddress ip4(int[] adr) {
		return new IP4Address(adr);
	}

	private IPAddresses() {
		super();
	}

}
