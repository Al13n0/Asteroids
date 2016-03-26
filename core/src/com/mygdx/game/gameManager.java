package com.mygdx.game;

import Elements.Asteroid;
import Elements.SpaceShip;
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
  
  
    public gameManager() {
        lifes = 3;
        level = 0;
        score = 0;
        requiredscore = 5000;                //punti richiesti per vita extra
        asteroidnum = 4;
        musicSet();
    }

    /**
     * MUSICSET
     * Funzione che setta il volume della musica e ne fa il play in loop.
     */
    public void musicSet() {
        spaceMusic = Gdx.audio.newMusic(Gdx.files.internal("SpaceMusic.wav"));
        spaceMusic.setLooping(true);
        spaceMusic.setVolume((float) 0.4);
        spaceMusic.play();

    }
    
    /**
     * MUSICSTOP
     * funzione che ferma la musica.
     */
    
    public void musicStop() {
        spaceMusic.stop();
    }

   
   
    /**
     *GETLEVEL
     * @return ritorna il livello raggiunto dal player. 
     */
    
    public int getLevel() {
        return level;
    }

    /**
     * INCREMENTLEVEL
     * Funzione che incrementa il livello.
     */
    
    public void incrementLevel() {
        level++;
    }

    /**GETLIFE
     * 
     * @return ritorna le vite del player. 
     */
    
    public int getlife() {
        return lifes;
    }

    /**
     * LOSELIFE
     * Funzione che decrementa le vite del giocatore.
     */
    
    public void loseLife() {
        lifes--;
    }
    
    /**
     * SETLIFE
     * @param l setta le vite al parametro l. 
     */
    
    public void setLife(int l) {
        lifes = l;
    }

    /**ADDLIFE
     * funzione che aggiunge una vita al giocatore ogni 5000 punti.
     */
    
    public void addLife() {
        if (score > requiredscore) {
            lifes++;
            requiredscore += requiredscore;   
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
    
    /**
     * GETSCORE
     * @return  ritorna il punteggio del giocatore.
     */
    public long getScore() {
        return score;
    }

    
}
