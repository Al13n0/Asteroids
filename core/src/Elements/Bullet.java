package Elements;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Game;
import interfaces.Loopable;
import interfaces.Renderable;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bullet extends SpaceObject implements Renderable, Loopable {


    private float rad;           //angolo generato punta astronave (uso per direzione sparare)
    private float speed;
    public Bullet(float x, float y, float rad) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.rad = rad;
        speed = (float) 3.9;
    }

    /*MOVIMENTO BULLET*/
    public void move() {
        x += (float) sin(Math.toRadians(-rad)) * speed;
        y += (float) cos(Math.toRadians(rad)) * speed;

    }

    /*CONTROLLO USCITA SCHERMO*/
    public boolean overscreen() {
        return x > Game.get().getWidth() || x < 0 || y > Game.get().getHeight() || y < 0;
    }
    
    /*DISTRUZIONE ASTEROIDE QUANDOO PROIETTILE INCONTRA ASTEROIDE*/
    public void destroy(){
         for (Asteroid a : Game.get().getAsteroidi() ) {  // per ogni asteroide chiamo expolison e verifico se contiene le cordinate del proiettile
            if(a.collision(x, y))
            {
                delete();  /*!!! DA FIXARE (per gli altri oggetti va)*/
            }
        }
    }
    
    /*LOGICA DEL PROGRAMMA*/
    @Override
    public void loop() {
        move();
        if (overscreen()) { //se proiettile esce da schermo lo elimino
            delete();       // NON RIMUOVE I PROIETTILI PROBLEMA STA CHE NON ME LI AGGIUNGE AI LOOPABLE
        }
        destroy();
 
    }

    /*RENDERING*/
    @Override
    public void render(ShapeRenderer sr) {
        sr.begin(ShapeType.Line);
        sr.setColor(1, 1, 1, 1);
        sr.circle(x, y, 1);
        sr.end();
    }

}
