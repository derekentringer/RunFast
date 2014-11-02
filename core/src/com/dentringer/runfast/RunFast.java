package com.dentringer.runfast;

import com.badlogic.gdx.Game;
import com.dentringer.runfast.screens.GameScreen;

public class RunFast extends Game 
{
	@Override
	public void create () 
	{
		setScreen(new GameScreen());
	}
}