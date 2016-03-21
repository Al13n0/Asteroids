package Elements;

import com.mygdx.game.Game;
import interfaces.Loopable;
import interfaces.Renderable;

public class SpaceObject {

    protected float x;
    protected  float y;
    protected float speed;

    public SpaceObject(float x, float y) {
        this.x = x;
        this.y = y;
        if (this instanceof Loopable) {   //se questo oggetto è loopale lo aggiungo ai loopable
            Game.get().registerLoopable((Loopable) this);
        }
        if (this instanceof Renderable) {
            Game.get().registerRenderable((Renderable) this);
        }
    }
    
/*FUNZIONE PER ELIMINARE OGGETTI*/
    
    public void delete(){
        Game.get().delete(this);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

     

}
