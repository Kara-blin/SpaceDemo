package components;

import jade.GameObject;
import jade.KeyListener;
import jade.Prefabs;
import jade.Window;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    public float flySpeed = 1.9f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private  float rechargeTime=0;
    private float constantRechargeTime=0.25f;


    public boolean canSpawn(){
        if (rechargeTime<=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
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
}