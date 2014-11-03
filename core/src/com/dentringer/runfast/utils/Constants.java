package com.dentringer.runfast.utils;

import com.badlogic.gdx.math.Vector2;

public class Constants 
{
	//game
	public static final int APP_WIDTH = 800;
	public static final int APP_HEIGHT = 480;
	public static final String BACKGROUND_IMAGE_PATH = "background_1.png";
	
	//world
	//gravity of -10m/s^2
	public static final Vector2 WORLD_GRAVITY = new Vector2(0, -10);
	
	//ground
	//in meters, eg 25m in width
	public static final float GROUND_X = 0;
	public static final float GROUND_Y = 0;
	public static final float GROUND_WIDTH = 50f;
	public static final float GROUND_HEIGHT = 2f;
	public static final float GROUND_DENSITY = 0f;
	
	//runner
	public static final float RUNNER_X = 2;
	public static final float RUNNER_Y = GROUND_Y+GROUND_HEIGHT;
	public static final float RUNNER_WIDTH = 1f;
	public static final float RUNNER_HEIGHT = 2f;
	public static final float RUNNER_GRAVITY_SCALE = 3f;
	public static final float RUNNER_DENSITY = 0.5f;
	//jump
	public static final Vector2 RUNNER_JUMPING_LINEAR_IMPULSE = new Vector2(0, 13f);
	//dodge
	public static final float RUNNER_DODGE_X = 2f;
	public static final float RUNNER_DODGE_Y = 1.5f;
	//get hit
	public static final float RUNNER_HIT_ANGULAR_IMPULSE = 10f;
	
	//enemies
	public static final float ENEMY_X = 25f;
    public static final float ENEMY_DENSITY = RUNNER_DENSITY;
    public static final float RUNNING_SHORT_ENEMY_Y = 1.5f;
    public static final float RUNNING_LONG_ENEMY_Y = 2f;
    public static final float FLYING_ENEMY_Y = 3f;
    public static final Vector2 ENEMY_LINEAR_VELOCITY = new Vector2(-10f, 0);
}