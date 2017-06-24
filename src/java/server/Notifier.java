/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/message")
public class Notifier {
    
    private static final String SEPARATOR = ";;";
    
    @Inject
    Game game;
    @Inject
    Loby loby;
    private String name;
    private Session mySession;
    
    @OnOpen
    public void open(Session session, EndpointConfig conf) 
    { 
        name = game.getName();
        mySession = session;
        game.setNotifier(this);
        connectToGame();
    }
    


    @OnClose
    public void close(Session session, CloseReason reason) 
    { 
        System.out.println("Zatvaram");
        loby.removeUser(name);
        announce(Types.REMOVE.value, name);
    }
    
    public void announce(int type, String msg)
    {
        String fullMsg = transform(type, msg);
        System.out.println(fullMsg);
        try {            
         for (Session sess : mySession.getOpenSessions()) {
            if (shouldSend(sess, type)) {
               sess.getBasicRemote().sendText(fullMsg);
            }
                
         }
      } catch (IOException e) { 
          e.printStackTrace();
      }
    }
    
    private boolean shouldSend(Session sess, int type) {
        if (sess.equals(mySession)) {
            return sess.isOpen() && type != Types.CREATE.value;
        } 
        return sess.isOpen();
    }
    
    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
        String[] messages = message.split(SEPARATOR);
        int type = Integer.valueOf(messages[0]);
        if (type == Types.START.value){
            startGame(messages);
        } else if (type == Types.MOVE.value) {
            move(messages);
        } else if (type == Types.END.value) {
            connectToGame();
        }
        
    }


    private void startGame(String[] messages) {
        announce(Types.START.value, transform(messages[1] ,messages[2]));
        announce(Types.REMOVE.value, messages[1]);
        announce(Types.REMOVE.value, messages[2]);
        loby.removeUser(messages[1]);
        loby.removeUser(messages[2]);
        loby.createGame(transform(messages[1],messages[2]));
        game.setPlaying(true);
    }

    private void move(String[] messages) {
        String gameName = transform(messages[1], messages[2]);
        int row = Integer.valueOf(messages[3]);
        int column = Integer.valueOf(messages[4]);
        boolean didMove = loby.didMove(gameName, row, column);
        if (didMove){
            announce(Types.MOVE.value, transform(messages[1], messages[2], messages[3], messages[4]));
            String winner = loby.checkWinner(gameName, row, column);
            if (winner != null){
                announce(Types.END.value, transform(gameName, winner));
            }
        }
    }

    private void connectToGame(){
        loby.addUser(name);
        announce(Types.CREATE.value, name);
    }

    private String transform(Object... args){
        String result = "";
        for (int index = 0; index < args.length; index++){
            result += args[index];
            if (index != args.length){
                result += SEPARATOR;
            }
        }
        return result;
    }
    
}
