package com.strongit.utils;

import java.util.UUID;

public class Uuid
{
	static public String get()
	{
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}
}
