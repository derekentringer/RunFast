package com.dentringer.runfast.box2d;

import com.dentringer.runfast.enums.UserDataType;

public class GroundUserData extends UserData
{
	public GroundUserData(float width, float height)
	{
		super(width, height);
		userDataType = UserDataType.GROUND;
	}
}