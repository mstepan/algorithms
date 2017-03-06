package com.max.algs.sesssions;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class UserSesssion implements Delayed {

	private static final long DELAY = 3_000; // 3 seconds

	private final String id;
	private volatile long creationTime;

	public UserSesssion(String id) {
		super();
		this.id = id;
		creationTime = System.currentTimeMillis();
	}

	public void updateTimestamp() {
		creationTime = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	@Override
	public int compareTo(Delayed other) {
		return id.compareTo(((UserSesssion) other).id);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long passedTime = System.currentTimeMillis() - creationTime;
		return unit.convert(DELAY - passedTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSesssion other = (UserSesssion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else
			if (!id.equals(other.id))
				return false;
		return true;
	}

}
