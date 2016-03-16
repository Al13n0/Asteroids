package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import interfaces.Loopable;
import interfaces.Renderable;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bullet extends SpaceObject implements Renderable, Loopable {

    private float lifetime;
    private float dirx;
    private float rad;
    private float dx;
    private float dy;
    private float speed;
    private float x;
    private float y;
    private Asteroid asteroide;

    public Bullet(float x, float y, float rad) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.rad = rad;
        speed = (float) 3.8;
    }

    /*MOVIMENTO BULLET*/
    public void move() {
        x += (float) sin(Math.toRadians(-rad)) * speed;
        y += (float) cos(Math.toRadians(rad)) * speed;

    }

    /*CONTROLLO USCITA SCHERMO*/
    public boolean overscreen() {
        return x > Gdx.graphics.getWidth() || x < 0 || y > Gdx.graphics.getHeight() || y < 0;
    }


    /*LOGICA DEL PROGRAMMA*/
    @Override
    public void loop() {
        move();
        if (overscreen()) { //se proiettile esce da schermo lo elimino
            delete();        //funzione di spaceobject
        }
         //asteroide.expolosion(x,y);
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
