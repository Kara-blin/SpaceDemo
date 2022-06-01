package components;

import jade.GameObject;
import jade.KeyListener;
import jade.Prefabs;
import jade.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;
import scenes.Scene;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    public transient int HP;
    public transient int staticHP=100;

    public transient float flySpeed = 1.9f;
    public transient float slowDownForce = 0.05f;
    public transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient float rechargeTime=0;
    private transient float constantRechargeTime=0.25f;
    private  transient  boolean changeState = true;
    private transient float timeToKill = 0.5f;


    public boolean canSpawn(){
        if (rechargeTime<=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void start() {

        this.HP = this.staticHP;
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
        if(!AssetPool.getSound("src/main/resources/sounds/Bill_Kiley_-_Driving_Force_Neon_Fog.ogg").isPlaying()){
            AssetPool.getSound("src/main/resources/sounds/Bill_Kiley_-_Driving_Force_Neon_Fog.ogg").play();
        }
        if(isDead){
            timeToKill -= dt;
            if (timeToKill <= 0) {
                this.gameObject.destroy();
            }
            this.rb.setVelocity(new Vector2f());
            return;
        }
        if(rechargeTime>0){
            rechargeTime-=dt;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)){
            this.acceleration.y = flySpeed;
            if(this.velocity.y<0){
                this.velocity.y += slowDownForce;
            }

        }else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)){
            this.acceleration.y = -flySpeed;
            if(this.velocity.y>0){
                this.velocity.y -= slowDownForce;
            }
        }else{
            this.acceleration.y=0;
            if(this.velocity.y>0){
                this.velocity.y = Math.max(0,this.velocity.y - slowDownForce);
            }else if(this.velocity.y<0){
                this.velocity.y = Math.min(0,this.velocity.y + slowDownForce);
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.acceleration.x = flySpeed;
            if (this.velocity.x < 0) {
                this.velocity.x += slowDownForce;
            } else {
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.acceleration.x = -flySpeed;

            if (this.velocity.x > 0) {
                this.velocity.x -= slowDownForce;
            } else {
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
            }
        }
        if((KeyListener.isKeyPressed(GLFW_KEY_SPACE))&&(canSpawn())){
            rechargeTime = constantRechargeTime;
            AssetPool.getSound("src/main/resources/sounds/plasma.ogg").play();
            Vector2f position = new Vector2f(this.gameObject.transform.position).add(new Vector2f(0,0.30f));
            GameObject plasma = Prefabs.generatePlasma(position);
            Window.getScene().addGameObjectToScene(plasma);

        }



        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if(isDead){
            return;
        }
        if(obj.getComponent(Asteroid.class)!=null){
            this.HP-=20;
            if((HP<staticHP*0.65f)&&(HP>staticHP*0.45f)&&(changeState)){
                changeState=false;
                stateMachine.trigger("getDamage1");
            }else if((HP<=staticHP*0.45f)&&(HP>staticHP*0.25f)&&(!changeState)){
                changeState=true;
                stateMachine.trigger("getDamage2");
            }else if((HP<=staticHP*0.25f)&&(HP>0)&&(changeState)){
                changeState=false;
                stateMachine.trigger("getDamage3");
            }else if(HP<=0){
                disappear();
            }
            obj.getComponent(Asteroid.class).disappear();
            return;
        }

    }

    public void disappear() {
        AssetPool.getSound("src/main/resources/sounds/explode.ogg").play();
        this.isDead = true;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.stateMachine.trigger("die");
        this.rb.setIsSensor();
    }


}