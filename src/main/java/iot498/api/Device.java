package iot498.api;

public class Device
{
	public Device(String owner, String id,
		String name, String description)
	{
		this.owner = owner;
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public final String owner;
	public final String id;
	public final String name;
	public final String description;
}
