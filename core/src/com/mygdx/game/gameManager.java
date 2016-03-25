package com.mygdx.game;

import Elements.Asteroid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;

public class gameManager {

    private Music spaceMusic;
    public int lifes;
    public long score;
    private long requiredscore;               //punteggio richiesto per avere un altra vita
    private int asteroidnum;
    private int level;
    private ArrayList<Asteroid> asteroidi;       //arraylist degli asteroidi

    public gameManager() {
        lifes = 3;
        level = 0;
        score = 0;
        requiredscore = 5000;                //punti richiesti per vita extra
        asteroidnum = 2;
        musicSet();
    }

    /**
     * Funzione che setta il volume della musica e ne fa il play in loop.
     */
    public void musicSet() {
        spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("SpaceMusic.wav"));
        spaceMusic.setVolume((float) 0.4);
        spaceMusic.play();
    }

    /*FUNZIONE CHE RITORNA IL LIVELLO RAGGIUNTO*/
    public int getLevel() {
        return level;
    }

    /*FUNZIONE CHE INCREMENTA IL LIVELLO SE NON CI SONO PIU ASTEROIDI*/
    public void incrementLevel() {
        level++;
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

    /**
     * FUNZIONE CHE INCREMENTA IL PUNTEGGIO DEL GIOCATORE
     *
     * @param n rappresenta di quanto incrementare il punteggio varierà a
     * seconda della dimensione dell'asteroide.
     */
    public void incrementScore(long n) {
        score += n;
    }

    public long getScore() {
        return score;
    }
}
    /**
     * Funzione che controlla l'avanzamento di livello e il numero di asteroidi
     * da creare a seconda del livello in cui è il giocatore.
     */
    
  