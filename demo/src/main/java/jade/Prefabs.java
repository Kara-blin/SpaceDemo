package jade;

import components.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY,float rotation) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        block.transform.rotation = rotation;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateShip1() {
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spriteSheet1.png");
        GameObject ship1 = generateSpriteObject(playerSprites.getSprite(0), 0.75f, 0.76f,180);

        // Ship1 states
        AnimationState whole = new AnimationState();
        whole.title = "WholeShip";
        float defaultFrameTime = 0.2f;
        whole.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        whole.setLoop(false);

        AnimationState break1 = new AnimationState();
        break1.title = "BreakShip1";
        break1.addFrame(playerSprites.getSprite(1), defaultFrameTime);
        break1.setLoop(false);

        AnimationState break2 = new AnimationState();
        break2.title = "BreakShip2";
        break2.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        break2.setLoop(false);

        AnimationState break3 = new AnimationState();
        break3.title = "BreakShip3";
        break3.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        break3.setLoop(false);


        AnimationState die = new AnimationState();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(13), 0.1f);
        die.addFrame(playerSprites.getSprite(14), 0.1f);
        die.addFrame(playerSprites.getSprite(15), 0.1f);
        die.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(whole);
        stateMachine.addState(break1);
        stateMachine.addState(break2);
        stateMachine.addState(break3);
        stateMachine.addState(die);

        stateMachine.setDefaultState(whole.title);
        stateMachine.addState(whole.title,break1.title,"getDamage1");
        stateMachine.addState(break1.title, break2.title, "getDamage2");
        stateMachine.addState(break2.title, break3.title, "getDamage3");
        stateMachine.addState(break3.title, die.title, "die");
        ship1.addComponent(stateMachine);

        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(0.45f, 0.35f));
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);

        ship1.addComponent(rb);
        ship1.addComponent(b2d);

        ship1.addComponent(new PlayerController());

        return ship1;
    }

    public static GameObject generateAsteroid1() {
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spriteSheet1.png");
        GameObject asteroid = generateSpriteObject(playerSprites.getSprite(19), 0.85f, 0.85f,0);

        AnimationState whole = new AnimationState();
        whole.title = "WholeAsteroid";
        float defaultFrameTime = 0.2f;
        whole.addFrame(playerSprites.getSprite(18), defaultFrameTime);
        whole.setLoop(false);

        AnimationState break1 = new AnimationState();
        break1.title = "BreakAsteroid1";
        break1.addFrame(playerSprites.getSprite(19), defaultFrameTime);
        break1.setLoop(false);

        AnimationState break2 = new AnimationState();
        break2.title = "BreakAsteroid2";
        break2.addFrame(playerSprites.getSprite(20), defaultFrameTime);
        break2.setLoop(false);

        AnimationState break3 = new AnimationState();
        break3.title = "BreakAsteroid3";
        break3.addFrame(playerSprites.getSprite(21), defaultFrameTime);
        break3.setLoop(false);

        AnimationState break4 = new AnimationState();
        break4.title = "BreakAsteroid4";
        break4.addFrame(playerSprites.getSprite(22), defaultFrameTime);
        break4.setLoop(false);

        AnimationState die = new AnimationState();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(13), 0.2f);
        die.addFrame(playerSprites.getSprite(14), 0.2f);
        die.addFrame(playerSprites.getSprite(15), 0.2f);
        die.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(whole);
        stateMachine.addState(break1);
        stateMachine.addState(break2);
        stateMachine.addState(break3);
        stateMachine.addState(break4);
        stateMachine.addState(die);

        stateMachine.setDefaultState(whole.title);
        stateMachine.addState(whole.title,break1.title,"getDamage1");
        stateMachine.addState(break1.title, break2.title, "getDamage2");
        stateMachine.addState(break2.title, break3.title, "getDamage3");
        stateMachine.addState(break3.title, break4.title, "getDamage4");
        stateMachine.addState(break4.title, die.title, "die");


        asteroid.addComponent(stateMachine);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.30f);
        asteroid.addComponent(circleCollider);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(false);
        rb.setContinuousCollision(false);
        asteroid.addComponent(rb);
        asteroid.addComponent(new Asteroid());



        return asteroid;
    }

    public static GameObject generatePlasma(Vector2f position) {
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spriteSheet1.png");
        GameObject plasma = generateSpriteObject(playerSprites.getSprite(16), 0.25f, 0.25f,0);
        plasma.transform.position = position;

        AnimationState fly = new AnimationState();
        fly.title = "plasmaFly";
        float defaultFrameTime = 0.23f;
        fly.addFrame(playerSprites.getSprite(16), 0.57f);
        fly.setLoop(false);

        AnimationState plasmaExploded = new AnimationState();
        plasmaExploded.title = "Exploded";
        plasmaExploded.addFrame(playerSprites.getSprite(13), 0.1f);
        plasmaExploded.addFrame(playerSprites.getSprite(14), 0.1f);
        plasmaExploded.addFrame(playerSprites.getSprite(15), 0.1f);
        plasmaExploded.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(fly);
        stateMachine.addState(plasmaExploded);
        stateMachine.setDefaultState(fly.title);
        stateMachine.addState(fly.title,plasmaExploded.title,"setDamage");
        plasma.addComponent(stateMachine);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.08f);
        plasma.addComponent(circleCollider);

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setFixedRotation(true);
        rb.setContinuousCollision(false);
        plasma.addComponent(rb);

        plasma.addComponent(new Plasma());

        return plasma;
    }


}