package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.mygdx.game.Game;
import interfaces.Loopable;
import interfaces.Renderable;
import java.util.ArrayList;

public class SpaceShip extends SpaceObject implements Renderable, Loopable {

    private float x, y, speed;
    private final float max_speed;
    private final Polygon Spaceship;                            //polygon contenuto nella libreria gdx
    private float[] vertices;
    private final float width;
    private final float height;
    private int lifes;                                       //vite del giocatore
    private long score;                                     //score del giocatore
    private long requiredscore;                             //punteggio richiesto per avere un altra vita
    private final ArrayList<Bullet> bullets;                //array di proiettili 
    private final Sound sparo;                               //Sound è un interfaccia messa a disposizione dalla libreria
    private final Sound espolisonenave;

    /*COSTRUTTORE*/
    public SpaceShip(float x, float y) {
        super(x, y);                                         //richiamo costruttore della superclasse spaceobject
        width = Game.get().getWidth();                       //larghezza della finestra
        height = Game.get().getHeight();                     //altezza finestra


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
        max_speed = (float) 5;
        bullets = new ArrayList<Bullet>();

        /*SUONI*/
        sparo = Gdx.audio.newSound(Gdx.files.internal("explode.ogg"));     //file contenuto nella cartella Android/assets
        espolisonenave = Gdx.audio.newSound(Gdx.files.internal("pulsehigh.ogg"));

      

    }

    /*FUNZIONE PER IL MOVIMENTO DELLA SPACESHIP*/
    public void move() {
        rotate();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) { //Accellerazione avanti//
            speed += 0.2;
        } else if (speed < 0) {
            speed = 0;
        } else if (speed != 0) {
            speed -= 0.03;        //decremento velocita 
        }
        if (speed > max_speed) {
            speed = max_speed;
        }
        x = (float) Math.cos(Math.toRadians(Spaceship.getRotation())) * speed; //calcolo seno e coseno del angolo generato  dalla rotazione
        y = (float) Math.sin(Math.toRadians(-Spaceship.getRotation())) * speed;
        Spaceship.translate(y, x);  //traslo i punti tramite funzione libreria
    }

    /*FUNZIONE PER RUOTARE LA SPACESHIP*/
    public void rotate() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Spaceship.rotate( (2));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Spaceship.rotate( (-2));
        }
    }

    /*FUNZIONE CHE SI OCCUPA DELLA GESTIONE DELLA FUORISCITA DALLA FINESTRA*/
    public void overScreen(float x, float y) {
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

    /*FUNZIONE PER SPARARE*/
    public void shoot() {
        vertices = Spaceship.getTransformedVertices();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { //iskeyjustpressed restituisce true se il tasto è appena stato premuto
            vertices = Spaceship.getTransformedVertices();
            bullets.add(new Bullet(vertices[4], vertices[5], Spaceship.getRotation()));  //i due punti che passo sono le cordinate della punta dell'astronave
            sparo.play(1.0f); //1.0f rappresenta il volume
        }
        for (Bullet b : bullets) {
            b.loop();

        }

    }

    /* FUNZIONE CHE DISTRUGGE ASTRONAVE  QUANDO COLLIDE CON ASTEROIDE*/
    public void destroy(float x, float y) {
        for (Asteroid a : Game.get().getAsteroidi()) {  // per ogni asteroide chiamo expolison e verifico se contiene le cordinate del proiettile
            if (a.containsxy(x, y)) {
                delete();                           //cancello astronave
                Game.get().loseLife();              //elimino una vita al player
                espolisonenave.play(1.0f);
               
                /*CONTROLLO VITE GIOCATORE*/
                if (Game.get().lifes > 0) {
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
        overScreen(x, y);    //controlla che astronvave non esce schermo
        shoot();             //sparare
        destroy(vertices[4], vertices[5]);

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

}
