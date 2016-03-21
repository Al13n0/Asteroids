package com.mygdx.game;

import Elements.Asteroid;
import Elements.Asteroid;
import Elements.SpaceShip;
import Elements.SpaceShip;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

    private static Game game;                    //variabile statica che assumerà stesso valore per ogni oggetto classe
    private int level;
    private OrthographicCamera camera;            //telecamera che renderizza oggetti

    private SpaceShip ship;                        //rappresenta l'asronave
    private ShapeRenderer sr;                    //utilizzo per renderizzare shape 
    private SpriteBatch sb;                       //utilizzo per renderizzare testo
    private BitmapFont font;                       //genera fonts da un file pngs

    private ArrayList<Renderable> renderables;   //arraylist degli oggetti renderizzabili
    private ArrayList<Loopable> loopables;       //arraylist degli oggetti loopabili
    private ArrayList<Asteroid> asteroidi;       //arraylist degli asteroidi

    private float width;
    private float height;
    public int lifes;
    public long score;
    private long requiredscore;               //punteggio richiesto per avere un altra vita

    /*FUNZIONE DOVE INSTAZIO OGGETTI*/
    @Override
    public void create() {
        game = this;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        sr = new ShapeRenderer();                //renderizzo shape
        sb = new SpriteBatch();                  //renderizzo texture e testo

        width = Gdx.graphics.getWidth();        //larghezza della finestra
        height = Gdx.graphics.getHeight();      //altezza finestra

        renderables = new ArrayList();
        loopables = new ArrayList();

        ship = new SpaceShip(100, 100);     //creo una nuova astronave passangoli le cordinate
        asteroidi = new ArrayList();

        /*ATTRIBUTI PARTITA E PLAYER*/
        lifes = 3;
        level = 1;
        score = 0;
        requiredscore = 10000;                //punti richiesti per vita extra

        fontGenerator();                        //generazione font                   

        spawnAsteroids();                      //faccio la spawn degli asteroidi

    }

    /*FUNZIONE MAIN*/
    public void main() {

        render();
        dispose();
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
        loop();                                //loop del gioco
        hud();                                 //disegno livello vite e punteggio in alto
    }

    /*FUNZIONE CHE DISEGNA SU SCHERMO VITA LIVELLO E SCORE*/
    public void hud() {
        drawText("LEVEL: ", 10, 468);
        drawText(Long.toString(getLevel()), 70, 468);

        drawText("LIFES: ", 370, 468);
        drawText(Long.toString(getlife()), 435, 468);

        drawText("SCORE: ", 650, 468);
        drawText(Long.toString(getScore()), 715, 468);

        drawText("©1979 ATARI INC", 330, 35);
    }

    /*FUNZIONE PER DISEGNARE STRINGHE A VIDEO*/
    public void drawText(String stringa, float width, float height) {
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        font.draw(sb, stringa, width, height);
        sb.end();
    }

    /*LOOP DEGLI OGGETTI*/
    public void loop() {
        for (int i = 0; i < loopables.size(); i++) {
            try {
                loopables.get(i).loop(); //Nel loop è contenuta la logica degli oggetti
            } catch (Exception x) {
                //x.printStackTrace();
                break;
            }
            if (game.lifes <= 0) {
                lifes = 0;
                Game.get().drawText("GAME OVER", width / 2 - 50, height / 2);
            }
            addLife();
        }
    }

    /*FUNZIONI PER AGGIUNGERE UN OGGETTO ALL'ARRAYLIST DEI RENDERABLE*/
    public void registerRenderable(Renderable r) {
        if (!renderables.contains(r)) {
            renderables.add(r);
        }
    }

    /*FUNZIONI PER AGGIUNGERE UN OGGETTO ALL'ARRAYLIST DEI LOOPABLE*/
    public void registerLoopable(Loopable l) {
        if (!loopables.contains(l)) {
            loopables.add(l);
        }
    }

    /*FUNZIONE CHE UTILIZZO PER RIMUOVERE GLI OGGETTI*/
    public void delete(Object o) {
        if (o instanceof Loopable) {
            loopables.remove((Loopable) o);
        }
        if (o instanceof Renderable) {
            renderables.remove((Renderable) o);
        }
    }

    /*FUNZIONE PER CREARE CREARE DEGLI ASTEROIDI*/
    public void spawnAsteroids() {
        for (int i = 0; i < 4; i++) {

            int xasteroide = MathUtils.random(420, 800);
            int yasteroide = MathUtils.random(250, 450);
            //controllo asteroidi non nascano su Spaceship         
            if (xasteroide == Game.get().ship.getVertices()[4] && yasteroide == Game.get().ship.getVertices()[5]) {
                xasteroide += 100;
                yasteroide += 150;
            }
            asteroidi.add(new Asteroid(xasteroide, yasteroide)); //aggiungo asteroide alla lista
        }
    }

    /*FUNZIONE CHE RITORNA ARRAYLIST DI  ASTEROIDI*/
    public ArrayList<Asteroid> getAsteroidi() {
        return asteroidi;
    }

    /*FUNZIONE STATICA CHE  RITORNA UN OGGETTO GAME*/
    public static Game get() {                        //metodo statico associato alla classe non all'istanza
        return game;
    }

    /*FUNZIONE CHE RITORNA IL LIVELLO RAGGIUNTO*/
    public int getLevel() {
        return level;
    }

    /*FUNZIONE CHE INCREMENTA IL LIVELLO SE NON CI SONO PIU ASTEROIDI*/
    public void incrementLevel() {
        if (asteroidi.size() < 0) {
            level++;
        }
    }

    /*FUNZIONE CHE RITORNA LE VITE RIMASTE AL PLAYER*/
    public int getlife() {
        return lifes;
    }

    /*FUNZIONE CHE ELIMINA UNA VITA AL GIOCATORE*/
    public void loseLife() {
        lifes--;
    }

    /*FUNZIONE CHE AGGIUNGE UNA VITA AL GIOCATORE*/
    public void addLife() {
        if (score > requiredscore) {
            lifes++;
            requiredscore += requiredscore;   //ogni 10000 assegno una vita extra
        }

    }

    /*FUNZIONE CHE RITORNA IL PUNTEGGIO DEL GIOCATORE*/
    public long getScore() {
        return score;
    }

    /*FUNZIONE CHE INCREMENTA  IL PUNTEGGIO DEL GIOCATORE*/
    public void incrementScore(long n) {
        score += n;

    }

    /*FUNZIONE CHE RITORNA LA LARGHEZZA DELLA FINESTRA*/
    public float getWidth() {
        return width;
    }

    /*FUNZIONE CHE RITORNA L'ALTEZZA DELLA FINESTRA*/
    public float getHeight() {
        return height;
    }

    /*FUNZIONE CHE RITORNA UN ASTRONAVE*/
    public SpaceShip getShip() {
        return ship;
    }

    /*FUNZIOEN CHE RITORNA ARRAYLIST DEGLI OGGETTI DISEGNABILI*/
    public ArrayList<Renderable> getRenderable() {
        return renderables;
    }

    /*FUNZIONE CHE GENERA IL FONT E I SUOI PARAMETRI*/
    public void fontGenerator() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Hyperspace Bold.ttf") //sono nella cartella android/assets
        );
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 17;                       //rappresenta la dimesione del font
        font = gen.generateFont(parameter);
    }
 
/*FUNZIONE PER AIUTARE IL SISTEMA OPERATIVO A "RIPULIRE" QUANDO CHIUDO GIOCO*/
    public void dispose(){
        sb.dispose();
        sr.dispose();
        font.dispose();
    }
}
