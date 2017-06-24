/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@Named
@SessionScoped
public class Game implements Serializable{
    private String name;
    private boolean playing;
    
    Notifier notifier;
    
    @Inject
    Loby loby;
    
    public Game() {
        System.out.println("Vytvaram game");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Loby getLoby() {
        return loby;
    }

    public void setLoby(Loby loby) {
        this.loby = loby;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
    
    public void start(){
        
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Game){
            return this.name.equals(((Game) obj).name);
        } else if (obj instanceof String){
            return this.name.equals(obj);
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
