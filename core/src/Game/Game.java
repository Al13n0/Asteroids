package Game;

import Elements.Asteroid;
import Elements.SpaceShip;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import interfaces.Loopable;
import interfaces.Renderable;
import java.util.ArrayList;

public class Game extends ApplicationAdapter {

    private static Game game;
    private OrthographicCamera camera;            //telecamera che renderizza oggetti
    private SpaceShip ship;
    private ShapeRenderer sr;                    //utilizzo per renderizzare shape 
    private SpriteBatch sb;                       //utilizzo per renderizzare testo
    private BitmapFont font;                       //genera fonts da un file pngs

    private ArrayList<Renderable> renderables;   //arraylist degli oggetti renderizzabili
    private ArrayList<Loopable> loopables;       //arraylist degli oggetti loopabili
    private ArrayList<Asteroid> asteroidi;       //arraylist degli asteroidi

    private float width;
    private float height;
    private int asteroidnum;
    private gameManager gm;
    private boolean gameover;

    /*FUNZIONE DOVE INSTAZIO OGGETTI*/
    @Override
    public void create() {
        game = this;
        asteroidnum = 2;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        sr = new ShapeRenderer();                //renderizzo shape
        sb = new SpriteBatch();                  //renderizzo texture e testo

        width = Gdx.graphics.getWidth();        //larghezza della finestra
        height = Gdx.graphics.getHeight();      //altezza finestra

        renderables = new ArrayList();
        loopables = new ArrayList();
        gameover = false;

        ship = new SpaceShip(100, 100);
        asteroidi = new ArrayList();
        gm = new gameManager();
        fontGenerator();
        levelControll();
     

    }

    public void main() {
        render();

    }

    /*RENDERING*/
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //pulisce lo schermo
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        for (Renderable r : renderables) {  // per ogni elemento dll arraylist faccio il render
            r.render(sr);
        }
        loop();
        if (gameover==true) {
            drawText("GAME OVER", width / 2 - 50, height / 2);
            drawText("PRESS ENTER TO PLAY AGAIN", 270, height / 2 - 35);
            //System.exit(0);
        }
        hud();

    }

    /*FUNZIONE CHE DISEGNA SU SCHERMO VITA LIVELLO E SCORE*/
    public void hud() {
        drawText("LEVEL: ", 10, 468);
        drawText(Long.toString(gm.getLevel()), 70, 468);

        drawText("LIFES: ", 370, 468);
        drawText(Long.toString(gm.getlife()), 435, 468);

        drawText("SCORE: ", 650, 468);
        drawText(Long.toString(gm.getScore()), 715, 468);

        drawText("©1996 CESARI INC", 330, 35);
    }

    /**
     * DRAWTEXT funzione per disegnare delle stringhe a video
     *
     * @param stringa stringa da disegnare
     * @param x cordinata x
     * @param y cordinata y
     */
    public void drawText(String stringa, float x, float y) {
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        font.draw(sb, stringa, x, y);
        sb.end();
    }

    /**
     * LOOP Funzione che contiene la logica del gioco, per ogni elemento
     * presente nell'arraylist dei loopable viene richiamato il suo metodo loop.
     */
    public void loop() {
        for (int i = 0; i < loopables.size(); i++) {
            try {
                loopables.get(i).loop();
            } catch (Exception x) {
                x.printStackTrace();
                break;
            }
            Checklife();

        }
    }

    /**
     * RegisterRenderable funzione per aggiungere un oggetto all'arraylist degli
     * oggetti disegnabili
     *
     * @param r rappresenta l'oggetto da aggiungere
     */
    public void registerRenderable(Renderable r) {
        if (!renderables.contains(r)) {
            renderables.add(r);
        }
    }

    /**
     * registerLoopable Funzione per aggiungere un oggetto all'arraylist degli
     * oggetti con logica
     *
     * @param l rappresenta l'oggetto da aggiungere
     */
    public void registerLoopable(Loopable l) {
        if (!loopables.contains(l)) {
            loopables.add(l);
        }
    }

    /**
     * DELETE funzione che utilizzo per rimuovere un oggetto dagli arraylist dei
     * renderizzabili e dei loopabili
     *
     * @param o rappresenta l'oggetto da rimuovere
     */
    public void delete(Object o) {
        if (o instanceof Loopable) {
            loopables.remove((Loopable) o);
        }
        if (o instanceof Renderable) {
            renderables.remove((Renderable) o);
        }
    }

    /**
     * GETASTEROIDI
     *
     * @return ritorna un arraylist contentente gli asteroidi
     */
    public ArrayList<Asteroid> getAsteroidi() {
        return asteroidi;
    }

    /**
     * Funzione che ritorna un oggetto statico game (statico legato alla classe
     * non alla singola istanza)
     *
     * @return game
     */
    public static Game get() {
        return game;
    }

    /**
     * GETWIDTH
     *
     * @return ritorna la larghezza della finestra di gioco
     */
    public float getWidth() {
        return width;
    }

    /**
     * GETHEIGHT
     *
     * @return ritorna l'altezza della finestra
     */
    public float getHeight() {
        return height;
    }

    /**
     * Funzione che ritorna un array list contenente gli oggetti renderizzabili
     *
     * @return arraylist oggetti renderizzabili
     */
    public ArrayList<Renderable> getRenderable() {
        return renderables;
    }

    /**
     * Funzione che setta il tipo di garattere da usare e la sua dimensione il
     * carattere viene prelevato dalla cartella android/assets.
     */
    public void fontGenerator() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Hyperspace Bold.ttf")
        );
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 17;
        font = gen.generateFont(parameter);
    }

    /**
     * Funzione che controlla l'avanzamento di livello e il numero di asteroidi
     * da creare a seconda del livello in cui è il giocatore.
     */
    public void levelControll() {
        if (asteroidi.isEmpty()) {
            gm.incrementLevel();
            asteroidnum += 2;
            if (asteroidnum == 12) {
                asteroidnum = 12;
            }
            spawnAsteroids(asteroidnum);
        }
    }

    /**
     * Funzione che serve per creare gli asteroidi nel gioco
     *
     * @param num rappresenta il numero di asteroidi da creare
     */
    public void spawnAsteroids(int num) {
        for (int i = 1; i <= num; i++) {
            float angolo= MathUtils.random(200, 800);
            float raggio=height/2;
            int xasteroide = Math.round(MathUtils.cos(angolo)*raggio);
            int yasteroide = Math.round(MathUtils.sin(angolo)*raggio);
            new Asteroid(xasteroide, yasteroide);
        }
    }

    public void Checklife() {
        if (gm.getlife() <= 0 ||Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ) {
            gameover();
        }
        gm.addLife();
        levelControll();
    }

    /**
     * GAME OVER funzione che gestisce il gameover,impostando la vita a 0,
     * fermando la musica e mandando a video la scritta playagain e qualora il
     * giocatore premesse invio inizia una nuova partita
     */
    public void gameover() {
        gameover = true;
        gm.setLife(0);
         ship.delete();
        gm.musicStop();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            create();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
            System.exit(0);
        }
    }

    /**
     * Funzione che ritorna una spaeship
     *
     * @return ship rappresenta il player.
     */
    public SpaceShip getShip() {
        return ship;
    }

    /**
     * Funzione che ritorna un gameManager.
     */
    public gameManager getGm() {
        return gm;
    }

}
