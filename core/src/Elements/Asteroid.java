package Elements;

import com.mygdx.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import interfaces.Loopable;
import interfaces.Renderable;

public class Asteroid extends SpaceObject implements Loopable, Renderable {

    private final float[] vertices;               //array punti asteroide
    private final Polygon asteroid;              //polygon contenuto nella libreria gdx
    private int npunti;                          //N punti del poligono asteroide
    private static Sound esplosione;            //Sound è un interfaccia messa a disposizione dalla libreria
    private float dimmin, vardim;

    /**
     * COSTRUTTORE GENERICO ASTEROIDI
     *
     * @param x xasteroide
     * @param y yasteroide
     * @param dimmin dimensione minima di un asteroide
     * @param vardim variazione nella dimensione dell'asteroide
     * 
     */
   
    public Asteroid(float x, float y, float dimmin, float vardim) {
        super(x, y);
        this.vardim = vardim;
        this.dimmin = dimmin;
        vertices = genera(dimmin, vardim);
        asteroid = new Polygon(vertices);
        asteroid.setPosition(x, y);
        Game.get().getAsteroidi().add(this);
        if (esplosione == null) {
            esplosione = Gdx.audio.newSound(Gdx.files.internal("thruster.ogg"));     //file contenuto nella cartella Android/assets
        }
    }

    /**
     * COSTRUTTORE DEFAULT DI ASTEROIDI GRANDI
     *
     * @param x xasteroide
     * @param y yasteroide+
     *gli asteroidi grandi avranno una dimensione minima di 42 e una
     * variabilita di forma di 15 
     */
    
    public Asteroid(float x, float y) {
        this(x, y, 42, 15);
    }

    /**
     * Funzione che si occupa della gestione della fuoriuscita dallo schermo
     */
    public void overScreen() {
        float x = asteroid.getX();
        float y = asteroid.getY();
        if (x > Game.get().getWidth()) {
            x = 0;
        }
        if (x < 0) {
            x = Game.get().getWidth();
        }
        if (y > Game.get().getHeight()) {
            y = 0;
        }
        if (y < 0) {
            y = Game.get().getHeight();
        }
        asteroid.setPosition(x, y);
    }

    /*FUNZIONE PER GENERARE PUNTI CASUALI DEGLI ASTEROIDI*/
    private float[] genera(float dimmin, float vardim) {
        npunti = MathUtils.random(11, 19);    //numero punti asteroidse
        float[] punti = new float[npunti * 2];   //per due perchè contiene x e y
        float a = (float) (Math.random() * Math.PI * 2);
        for (int i = 0; i < npunti * 2; i += 2) {
            float r = (float) (Math.random() * vardim + dimmin); //raggio circonferenza
            float spost = (float) (Math.random() * Math.PI * 2 / (npunti * 10));  //serve per non avere asteroidi troppo circolari
            a += (float) (Math.PI * 2 / npunti) + spost - Math.PI * 2 / (npunti * 10 * 2);
            punti[i] = (float) (Math.cos(a) * r);  //x è il coseno dell angolo
            punti[i + 1] = (float) (Math.sin(a) * r);  //y è il seno dell angolo
        }
        return punti;
    }

    /**
     * MOVIMENTO ASTEROIDE
     */
    public void move() {
        rotate();
        //direzione pseudocasuale degli asteroidi     !!(provare a pensare variante)!!
        if (npunti % 2 == 0) {
            x = (float) (1.5 * (float) Math.random());
            y = (float) -Math.random();
        } else {
            x = -(float) (1.8 * (float) Math.random());
            y = (float) Math.random();
        }

    }

    /*ROTAZIONE*/
    public void rotate() {
        if (npunti % 2 == 0) {    //rotazione dipende da numero punti asteroide 
            asteroid.rotate((float) -1.8);
        } else {
            asteroid.rotate((float) +1.2);
        }

    }

    /*COLLISSIONE*/
    public boolean containsxy(float x, float y) {
        return asteroid.contains(x, y);        //ritorna true se punti del bullet sono contenuti nel poligono
    }

    public void collision(float xb, float yb) {
        esplosione.play(0.4f);
        if (dimmin > 15) {
            new Asteroid(xb, yb, dimmin / 2, vardim / 2);
            new Asteroid(xb, yb, dimmin / 2, vardim / 2);
        }
        delete();
    }

    /*LOGICA ASTEROIDE*/
    @Override
    public void loop() {
        move();
        asteroid.translate(x, y);      //traslo i punti tramite funzione libreria
        overScreen();                 //controlla che asteroide non esca schermo
    }

    /*RENDERING*/
    @Override
    public void render(ShapeRenderer sr) {
        sr.begin(ShapeType.Line);
        sr.setColor(1, 1, 1, 1);
        sr.polygon(asteroid.getTransformedVertices());
        sr.end();
    }

    @Override
    public void delete() {
        super.delete();
        Game.get().getAsteroidi().remove(this);
    }

}
