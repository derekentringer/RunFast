package com.dentringer.runfast.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.dentringer.runfast.actors.Background;
import com.dentringer.runfast.actors.Enemy;
import com.dentringer.runfast.actors.Ground;
import com.dentringer.runfast.actors.Runner;
import com.dentringer.runfast.utils.BodyUtils;
import com.dentringer.runfast.utils.Constants;
import com.dentringer.runfast.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener
{
	private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
	private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;
	
	private World world;
	private Ground ground;
	private Runner runner;
	private Enemy enemy;
	
	private final float TIME_STEP = 1/200f;
	private float accumulator = 0f;
	
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	
	private Rectangle screenLeftSide;
	private Rectangle screenRightSide;
	
	private Vector3 touchPoint;
	
	public GameStage()
	{
		super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
		setupWorld();
		setupCamera();
		setupTouchControlAreas();
		
		renderer = new Box2DDebugRenderer();
	}
	
	private void setupWorld()
	{
		world = WorldUtils.createWorld();
		
		//collisions
		world.setContactListener(this);
		
		setupBackground();
		setupGround();
		setupRunner();
		createEnemy();
	}
	
	private void setupBackground()
	{
		addActor(new Background());
	}
	
	private void setupGround()
	{
		ground = new Ground(WorldUtils.createGround(world));
	}
	
	private void setupRunner()
	{
		runner = new Runner(WorldUtils.createRunner(world));
	}
	
	private void createEnemy()
	{
		enemy = new Enemy(WorldUtils.createEnemy(world));
		addActor(enemy);
	}
	
	private void setupCamera()
	{
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0f);
		camera.update();
	}
	
	private void setupTouchControlAreas() 
	{
        touchPoint = new Vector3();
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }
	
	@Override
    public boolean touchDown(int x, int y, int pointer, int button) 
	{
        // Need to get the actual coordinates
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) 
        {
            runner.jump();
        }
        else if (leftSideTouched(touchPoint.x, touchPoint.y)) 
        {
            runner.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }
	
	@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
        if (runner.isDodging()) 
        {
            runner.stopDodge();
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }
	
	private boolean leftSideTouched(float x, float y) 
	{
        return screenLeftSide.contains(x, y);
    }

    private boolean rightSideTouched(float x, float y) 
    {
        return screenRightSide.contains(x, y);
    }

    /**
     * Helper function to get the actual coordinates in my world
     * @param x
     * @param y
     */
    private void translateScreenToWorldCoordinates(int x, int y) 
    {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);
		
		for (Body body : bodies) 
		{
            update(body);
        }
		
		//fixed timestep
		accumulator += delta;
		
		while(accumulator >= delta)
		{
			world.step(TIME_STEP,  6,  2);
			accumulator -= TIME_STEP;
		}
	}
	
	private void update(Body body) 
	{
        if (!BodyUtils.bodyInBounds(body)) 
        {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) 
            {
                createEnemy();
            }
            world.destroyBody(body);
        }
    }
	
	@Override
	public void draw()
	{
		super.draw();
		renderer.render(world, camera.combined);
	}

	@Override
	public void beginContact(Contact contact) 
	{
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) 
		{
            runner.hit();
            
        } 
		else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) 
		{
            runner.landed();
        }
	}

	@Override
	public void endContact(Contact contact) 
	{
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		
	}
}