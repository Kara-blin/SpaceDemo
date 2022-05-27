package components;

import jade.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;

public class Asteroid extends Component {
    private transient Rigidbody2D rb;
    private transient float asteroidSpeed = 0.17f;
    private transient Vector2f velocity = new Vector2f();
    private transient float HP = 25;
    private transient float staticHP = 25;
    private  transient boolean isDead=false;
    private transient float timeToKill = 0.5f;
    private transient StateMachine stateMachine;
    private transient boolean changeState = true;


    @Override
    public void start(){
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = this.gameObject.getComponent(Rigidbody2D.class);
    }

    @Override
    public void update(float dt){
        if(isDead){
            timeToKill -= dt;
            if (timeToKill <= 0) {
                this.gameObject.destroy();
            }
            this.rb.setVelocity(new Vector2f());
            return;
        }
        this.rb.setGravityScale(0.0f);
        velocity.x=0;
        velocity.y = -asteroidSpeed;
        this.rb.setVelocity(velocity);

    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if(isDead){
            return;
        }
        if(obj.getComponent(Plasma.class)!=null){
            HP-=2;
            if((HP<staticHP*0.75f)&&(HP>staticHP*0.5f)&&(changeState)){
                changeState=false;
                stateMachine.trigger("getDamage1");
            }else if((HP<=staticHP*0.5f)&&(HP>staticHP*0.35f)&&(!changeState)){
                changeState=true;
                stateMachine.trigger("getDamage2");
            }else if((HP<=staticHP*0.35f)&&(HP>staticHP*0.2f)&&(changeState)){
                changeState=false;
                stateMachine.trigger("getDamage3");
            }else if((HP<=staticHP*0.2f)&&(HP>0)&&(!changeState)){
                changeState = true;
                stateMachine.trigger("getDamage4");
            }else if(HP<=0){
                disappear();
            }
            obj.getComponent(Plasma.class).disappear();
            return;
        }

    }

    public void disappear() {
            this.isDead = true;
            this.velocity.zero();
            this.rb.setVelocity(new Vector2f());
            this.rb.setAngularVelocity(0.0f);
            this.rb.setGravityScale(0.0f);
            this.stateMachine.trigger("die");
            this.rb.setIsSensor();
    }
}
