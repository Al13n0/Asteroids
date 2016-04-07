package Elements;

import com.mygdx.Game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import interfaces.Loopable;
import interfaces.Renderable;
import java.util.Collection;

public class Asteroid extends SpaceObject implements Loopable, Renderable {

    private final float[] vertices;               //array punti asteroide
    private final Polygon asteroid;              //polygon contenuto nella libreria gdx
    private int npunti;                          //N punti del poligono asteroide
    private static Sound esplosione;
    private float dimmin, vardim;
    private float orbita;

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
        orbita = MathUtils.random(100, 360);
        Game.get().getAsteroidi().add(this);
        if (esplosione == null) {
            esplosione = Gdx.audio.newSound(Gdx.files.internal("thruster.ogg"));     //file contenuto nella cartella Android/assets
        }
    }

    /**
     * COSTRUTTORE DEGLI ASTEROIDI GRANDI INIZIALI
     *
     * @param x xasteroide
     * @param y yasteroide+ gli asteroidi grandi avranno una dimensione minima
     * di 32 e una variabilita di forma di 15
     */
    public Asteroid(float x, float y) {
        this(x, y, 32, 15);
    }

    /**
     * OVERSCREEN Funzione che si occupa della gestione della fuoriuscita dallo
     * schermo.
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

    /**
     * GENERA questa funzione genera delle coppie di punti pseudocausali, ogni
     * punto ha una sua x e una sua y; i punti appartengono tutti ad una
     * circonferenza immaginaria della quale viene calcolato ogni volta un
     * angolo e assegnato alla x il coseno di quel angolo e alla y il seno
     * dell'angolo.
     *
     * @param dimmin dimensione minima asteroide
     * @param vardim variabilita asteroide
     * @return array contenente le cordinate di punti.
     */
    private float[] genera(float dimmin, float vardim) {
        npunti = MathUtils.random(11, 19);    //numero punti asteroide
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
     * MOVIMENTO ASTEROIDE Funzione che richiama rotate che gestisce la
     * rotazione dell'asteroide e incrementa la x e la y per il seno e il coseno
     * dell'orbita che è un numero casuale assegnato all'asteroide quando viene
     * istanziato inoltre se gli asteroidi sono  molto piccoli incremento la loro velocita.
     * 
     */
    public void move() {
        rotate();
        x = (float) Math.cos(orbita);
        y = (float) Math.sin(orbita);
        
        if (asteroid.area() <=1255) {
            x = (float) ((float) Math.cos(orbita) +0.3);
            y = (float) ((float) Math.sin(orbita) +0.3);
        }
         if (asteroid.area() <=315) {
            x = (float) ((float) Math.cos(orbita) +0.56);
            y = (float) ((float) Math.sin(orbita) +0.56);
        }

    }

    /**
     * ROTATE funzione che si occupa della rotazinr dell'asteroide se la sua
     * orbita (numero casuale assegnatogli quando viene creato) è pari ruotera
     * in un senso altrimenti ruotera al contrario.
     */
    public void rotate() {
        if (orbita % 2 == 0) {
            asteroid.rotate((float) -2);
        } else {
            asteroid.rotate((float) +1.5);
        }

    }

    /**
     * CONTIENE Funzione che verifica se un punto è contenuto nel poligono che
     * rappresenta l'asteroide.
     *
     * @param x cordinata x
     * @param y cordinata y
     * @return ritorna true se le cordinate del punto sono contenute nel
     * poligono
     */
    public boolean containsxy(float x, float y) {
        return asteroid.contains(x, y);
    }

    /**
     * DIVISIONEASTEROIDE Funzione che divide l'asteroide se non ha gia
     * raggiunto la sua dimensione minima,questa funzione richiama anche il
     * suono e scoresize che assegna un punteggio in base alla dimensione dell
     * asteroide.
     *
     * @param xb x proiettile
     * @param yb y proiettile gli passo x e y che saranno le cordinate di dove
     * generare il nuovo asteroide.
     *
     */
    public void splitAsteroid(float xb, float yb) {
        esplosione.play(0.5f);
        if (dimmin > 15) {
            new Asteroid(xb, yb, dimmin / 2, vardim / 2);
            new Asteroid(xb, yb, dimmin / 2, vardim / 2);
        }
        scoreSize();
        delete();
    }

    /**
     * ASSEGNA PUNTEGGIO questa funzione calcola l'area del poligono che
     * costituice l'asteroide e a seconda della sua dimensione restituisce i
     * seguentei valori: asteroide grande +20 medio +50 piccolo +100 punti che
     * andranno ad incrementare il punteggio del giocatore.
     */
    public int scoreSize() {
        int score = 0;
        if (asteroid.area() > 3900) {
            score = 20;
        } else if (asteroid.area() > 1100) {
            score = 80;
        } else if (asteroid.area() < 500) {
            score = 100;
        }
        System.out.println(asteroid.area());

        return score;
    }

    /**
     * LOOP funzione che richiama tutte le altre funzione per gestire la logica
     * degli asteroidi.
     */
    @Override
    public void loop() {
        move();
        asteroid.translate(x, y);      //traslo i punti tramite funzione libreria
        overScreen();                 //controlla che asteroide non esca schermo
    }

    /**
     * RENDERING funzione che disegna l'asteroide.
     *
     * @param sr shaperender oggetto predefinito dalla libreria per disegnare
     * figure.
     */
    @Override
    public void render(ShapeRenderer sr) {
        sr.begin(ShapeType.Line);
        sr.setColor(1, 1, 1, 1);
        sr.polygon(asteroid.getTransformedVertices());
        sr.end();
    }

    /**
     * DELETE funzione per eliminare una asteroide dalla lista dei renderable e
     * dei loopable.
     *
     */
    @Override
    public void delete() {
        super.delete();
        Game.get().getAsteroidi().remove(this);
    }

}
