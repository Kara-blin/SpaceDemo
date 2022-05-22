package components;

import org.joml.Vector2f;
import physics2d.components.Rigidbody2D;

public class Plasma extends Component {
    private transient Rigidbody2D rb;
    private transient float plasmaSpeed = 1.7f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private static float rechargeTime=0;
    private float constantRechargeTime=2.0f;


    public static boolean canSpawn(){
        if (rechargeTime<=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void start(){
        this.rb = this.gameObject.getComponent(Rigidbody2D.class);
        rechargeTime = constantRechargeTime;
    }

    @Override
    public void update(float dt){
        if(rechargeTime>0){
            rechargeTime-=dt;
        }
        velocity.x=0;
        velocity.y = plasmaSpeed;
        this.rb.setVelocity(velocity);

    }
}
