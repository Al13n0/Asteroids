package com.mygdx.game;

import Elements.Asteroid;
import Elements.Bullet;
import Elements.SpaceShip;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import interfaces.Loopable;
import interfaces.Renderable;
import java.util.ArrayList;

public class Game extends ApplicationAdapter {

    private static Game game;                    //variabile statica che assumerà stesso valore per ogni oggetto classe

    private OrthographicCamera camera;           //telecamera che renderizza oggetti
    private SpaceShip ship;
    private ShapeRenderer sr;                   //utilizzo per renderizzare shape 
    private ArrayList<Renderable> renderables; //arraylist degli oggetti renderizzabili
    private ArrayList<Loopable> loopables;      //arraylist degli oggetti loopabili
    private ArrayList<Asteroid> asteroidi;       //arraylist degli asteroidi
    private float width;
    private float height;

    /*FUNZIONE DOVE INSTAZIO OGGETTI*/
    @Override
    public void create() {
        game = this;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        sr = new ShapeRenderer();
        width = Gdx.graphics.getWidth();        //larghezza della finestra
        height = Gdx.graphics.getHeight();      //altezza finestra
        renderables = new ArrayList();
        loopables = new ArrayList();
        ship = new SpaceShip(100, 100);
        asteroidi = new ArrayList();         
       
        /*ISTANZO ASTEROIDI*/
        for (int i = 0; i < 4; i++){
           asteroidi.add(new Asteroid(MathUtils.random(100, 400), MathUtils.random(250, 400))); //aggiungo asteroide alla lista
           
           /*Asteroid a,b;
           a=new Asteroid(MathUtils.random(100, 400), MathUtils.random(250, 400));
           b=a;*/
           
        }   
            
             System.out.println(renderables);
       
    }

   
    /*FUNZIONE MAIN*/
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
        loop();          //loop del gioco
    }

    /*LOOP DEGLI OGGETTI*/
    public void loop() {
        for (int i = 0; i < loopables.size(); i++) {
            try {
                loopables.get(i).loop(); //Nel loop è contenuta la logica degli oggetti
            } catch (Exception x) {
                break;
            }
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

    /*FUNZIONE CHE RITORNA UN OGGETTO ASTEROIDE*/
    public ArrayList<Asteroid> getAsteroidi() {
        return asteroidi;
    }
   
    /*FUNZIONE STATICA CHE  RITORNA UN OGGETTO GAME*/
    public static Game get() {                        //metodo statico associato alla classe non all'istanza
        return game;
    }
    
   /*FUNZIONE CHE RITORNA LA LARGHEZZA DELLA FINESTRA*/
    public float getWidth() {
        return width;
    }
    
    /*FUNZIONE CHE RITORNA L'ALTEZZA DELLA FINESTRA*/
    public float getHeight() {
        return height;
    }
      public ArrayList<Renderable> getRenderable() {                        //metodo statico associato alla classe non all'istanza
        return  renderables;
    }
}
