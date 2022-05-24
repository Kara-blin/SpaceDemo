package components;

import jade.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;

public class Plasma extends Component {
    private transient Rigidbody2D rb;
    private transient float plasmaSpeed = 1.7f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();


    @Override
    public void start(){
        this.rb = this.gameObject.getComponent(Rigidbody2D.class);
    }

    @Override
    public void update(float dt){
        this.rb.setGravityScale(0.0f);
        velocity.x=0;
        velocity.y = plasmaSpeed;
        this.rb.setVelocity(velocity);

    }

    @Override
    public void preSolve(GameObject obj, Contact contact, Vector2f contactNormal) {
        if(obj.getComponent(PlayerController.class)!=null||obj.getComponent(Plasma.class)!=null){
            contact.setEnabled(false);
        }
    }

    public void disappear() {
        this.gameObject.destroy();
    }
}
