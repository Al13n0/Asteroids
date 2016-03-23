package Elements;

import com.mygdx.game.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import interfaces.Loopable;
import interfaces.Renderable;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bullet extends SpaceObject implements Renderable, Loopable {

    private float rad;           //angolo generato punta astronave
    private float speed;

    public Bullet(float x, float y, float rad) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.rad = rad;
        speed = (float) 4.5;
    }

    /**
     * MOVIMENTO BULLET funzione che gestisce il movimento del proiettile
     * incrementanto la sua x rispetto al seno dell'angolo generato dalla
     * rotazione dell'astronave e la sua y rispetto al coseno dell'angolo
     */
    public void move() {
        x += (float) sin(Math.toRadians(-rad)) * speed;
        y += (float) cos(Math.toRadians(rad)) * speed;

    }

    /*CONTROLLO USCITA SCHERMO*/
    public boolean overscreen() {
        return x > Game.get().getWidth() || x < 0 || y > Game.get().getHeight() || y < 0;
    }

    @Override
    public void delete() {
        super.delete();
        Game.get().getShip().getBullets().remove(this);
    }

    /**
     * DISTRUZIONE ASTEROIDE funzione che distrugge il proiettile quando esso
     * collide con un asteroide, la funzione richiama anche il metodo
     * incrementScore che incrementa il punteggio del giocatore quando esso
     * distrugge un asteroide
     */
    public void destroy() {
        boolean flag = false;
        for (int i = 0; i < Game.get().getAsteroidi().size(); i++) {
            if (Game.get().getAsteroidi().get(i).containsxy(x, y)) {
                Game.get().getAsteroidi().get(i).collision(x, y);
                delete();
                Game.get().incrementScore(10);
                //flag = true;
                break;
            }
        }
    }

    /**
     * LOGICA DEL PROGRAMMA contiene le funzioni per il movimento del
     * proiettile,e per cancellare il proiettile quando esso esce dallo schermo
     */
    @Override
    public void loop() {
        move();
        if (overscreen()) {
            delete();
        }
        destroy();
    }

    /**
     * RENDERING override del metodo render preso dall'interfaccia renderable
     * questo metodo necessita uno shaperender(oggetto fornito dalla libreria
     * per fare il rendering di shape
     */
    @Override
    public void render(ShapeRenderer sr) {
        sr.begin(ShapeType.Line);
        sr.setColor(1, 1, 1, 1);
        sr.circle(x, y, 1);
        sr.end();
    }

}
