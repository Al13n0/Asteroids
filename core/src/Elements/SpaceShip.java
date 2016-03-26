package Elements;

import com.mygdx.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import interfaces.Loopable;
import interfaces.Renderable;
import java.util.ArrayList;

public class SpaceShip extends SpaceObject implements Renderable, Loopable {

    private float x, y, speed;
    private final float max_speed;
    private final Polygon Spaceship;     //polygon contenuto nella libreria gdx
    private float[] vertices;
    private final float width;
    private final float height;
    private int lifes;
    private long score;
    private long requiredscore;             //punteggio per vita  extra
    private final ArrayList<Bullet> bullets;
    private static Sound sparo;              //Sound è un interfaccia messa a disposizione dalla libreria
    private static Sound espolisonenave;

    private float dx;
    private float dy;
    private float acceleration;
    private float decelleration;
    private float maxspeed;

    /**
     * Crea una nave alle coordinate date
     *
     * @param x coordinata x
     * @param y coordinate y la nave è costituita da un poligono i cui vertici
     * vengono inizializzati in questa funzione tenendo conto dell'altezza e
     * della larghezza della finestra.
     *
     */
    public SpaceShip(float x, float y) {
        super(x, y);

        width = Game.get().getWidth();
        height = Game.get().getHeight();

        /*Array punti spaceship*/
        vertices = new float[]{
            width / 2, height / 2 - 10,
            width / 2 + 5, height / 2 - 15,
            width / 2, height / 2,
            width / 2 - 5, height / 2 - 15,
            width / 2, height / 2 - 10
        };

        Spaceship = new Polygon(vertices);
        Spaceship.setOrigin(width / 2, height / 2 - 10); //setto origine poligono per fare la rotazione
        max_speed = (float) 3.3;
        bullets = new ArrayList<Bullet>();
        vertices = Spaceship.getTransformedVertices();
        iniSound();

    }

    /**
     * FUNZIONE PER IL MOVIMENTO DELLA SPACESHIP Questa funzione gestisce il
     * movimento dell astronave, prima di tutto richiama la funzione rotate che
     * fa girare l'astronavave e poi verifica se il tasto up + premuto
     * incrementa le cordinate dell'astronave.
     */
    public void move() {
        rotate();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) { //Accellerazione avanti//
            speed += 0.75;
        } else if (speed < 0) {
            speed = 0;
        } else if (speed != 0) {
            speed -= 0.04;        //decremento velocita 
        }
        if (speed > max_speed) {
            speed = max_speed;
        }
        x = (float) Math.cos(Math.toRadians(Spaceship.getRotation())) * speed; //calcolo seno e coseno del angolo generato  dalla rotazione
        y = (float) Math.sin(Math.toRadians(-Spaceship.getRotation())) * speed;
        Spaceship.translate(y, x);
    }

    /**
     * Funzione che ruota la nave in base all'tasto premuto dal giocatore di
     * conseguenza viene richiamata la funzione di libreria degli oggetti
     * poligoni che dato un float ruota automaticamente tutti i punti che
     * costituiscono il poligono.
     */
    public void rotate() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Spaceship.rotate((float) (2.7));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Spaceship.rotate((float) (-2.7));
        }
    }

    /**
     * Funzione che si occupa della fuoriuscita dalla finestra dell'player
     * quando il giocatore esce dallo schermo riappare dalla parte opposta.
     */
    public void overScreen() {
        x = Spaceship.getX();
        y = Spaceship.getY();
        if (x > width / 2) {
            x = -width / 2;
        }
        if (x < -width / 2) {
            x = width / 2;
        }
        if (y > height / 2) {
            y = -height / 2;
        }
        if (y < -height / 2) {
            y = height / 2;
        }
        Spaceship.setPosition(x, y);
    }

    /**
     * SHOOT Funzione per sparare.
     */
    public void shoot() {
        vertices = Spaceship.getTransformedVertices();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { //iskeyjustpressed restituisce true se il tasto è appena stato premuto
            vertices = Spaceship.getTransformedVertices();
            bullets.add(new Bullet(vertices[4], vertices[5], Spaceship.getRotation()));  //i due punti che passo sono le cordinate della punta dell'astronave
            sparo.play(0.8f); //1.0f rappresenta il volume
        }
    }

    /* FUNZIONE CHE DISTRUGGE ASTRONAVE  QUANDO COLLIDE CON ASTEROIDE*/
    public void checkDestruction() {
        float x = vertices[4], y = vertices[5];
        for (Asteroid a : Game.get().getAsteroidi()) {  // per ogni asteroide chiamo expolison e verifico se contiene le cordinate del proiettile
            if (a.containsxy(x, y)) {
                delete();                           //cancello astronave
                Game.get().getGm().loseLife();              //elimino una vita al player
                espolisonenave.play(1.0f);

                /*CONTROLLO VITE GIOCATORE*/
                if (Game.get().getGm().lifes > 0) {
                    rigenerate();
                }
            }
        }
    }

    /*FUNZIONE CHE RITORNA L' ARRAYLIST DEI PROIETTILI*/
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /*FUNZIONE PER RIGENERARE ASTRONAVE DOPO ESSERE STATA DISTRUTTA*/
    public void rigenerate() {
        new SpaceShip(100, 100);
    }

    /*FUNZIONE CONTENENTE LA LOGICA DEL GIOCO*/
    @Override
    public void loop() {
        move();              //movimento astronave
        overScreen();    //controlla che astronvave non esce schermo
        shoot();             //sparare
        checkDestruction();
    }

    /*RENDERING ASTRONAVE*/
    @Override
    public void render(ShapeRenderer sr) {
        sr.begin(ShapeType.Line);
        sr.setColor(1, 1, 1, 1);
        sr.polygon(Spaceship.getTransformedVertices());
        sr.end();
        for (Bullet b : bullets) {
            b.render(sr); //per ogni proiettile b contenuto in bullets chiamo il metodo b.render
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    /**
     * Funzione che inizializza i suoni per l'esplosione della nave e per lo
     * sparo del proiettile i suoni sono definiti come degli attributi statici
     * non legati all istanza ma alla classe.
     */
    public void iniSound() {
        if (sparo == null) {
            sparo = Gdx.audio.newSound(Gdx.files.internal("explode.ogg"));     //file contenuto nella cartella Android/assets
        }
        if (espolisonenave == null) {
            espolisonenave = Gdx.audio.newSound(Gdx.files.internal("pulsehigh.ogg"));
        }
    }

}
