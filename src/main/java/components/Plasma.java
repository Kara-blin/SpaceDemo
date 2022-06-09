package components;

import engine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;

public class Plasma extends Component {
    private transient Rigidbody2D rb;
    private transient float plasmaSpeed = 1.7f;
    private transient Vector2f velocity = new Vector2f();
    private transient float timeToKill = 0.30f;
    private  transient boolean isDead=false;
    private transient StateMachine stateMachine;


    @Override
    public void start(){
        this.rb = this.gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
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
        if(this.gameObject.transform.position.y>3.325f){
            this.gameObject.destroy();
        }
        this.rb.setGravityScale(0.0f);
        velocity.x=0;
        velocity.y = plasmaSpeed;
        this.rb.setVelocity(velocity);

    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        if(obj.getComponent(PlayerController.class)!=null||obj.getComponent(Plasma.class)!=null||obj.getComponent(Ground.class)!=null){
            contact.setEnabled(false);
        }
    }

    public void disappear() {
        this.isDead = true;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.stateMachine.trigger("setDamage");
        this.rb.setIsSensor();
    }
}
