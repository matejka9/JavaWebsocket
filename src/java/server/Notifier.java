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
        addToGame();
    }
    
    public void addToGame(){
            loby.addUser(name);
            announce(MessageType.addUser.value, name);
    }

    @OnClose
    public void close(Session session, CloseReason reason) 
    { 
        System.out.println("Zatvaram");
        loby.removeUser(name);
        announce(MessageType.removeUser.value, name);
    }
    
    public void announce(int num, String msg)
    {
        String fullMsg = Integer.toString(num) + SEPARATOR + msg;
        System.out.println(fullMsg);
        try {            
         for (Session sess : mySession.getOpenSessions()) {
            System.out.println("Je tu session");
            if (shouldSend(sess, num)) {
               System.out.println("Posielam");
               sess.getBasicRemote().sendText(fullMsg);
            }
                
         }
      } catch (IOException e) { 
          e.printStackTrace();
      }
    }
    
    private boolean shouldSend(Session sess, int num) {
        if (sess.equals(mySession)) {
            return sess.isOpen() && num != MessageType.addUser.value;
        } 
        return sess.isOpen();
    }
    
    @OnMessage
    public void messageReceiver(String message) {
        System.out.println("Received message:" + message);
        String[] messages = message.split(SEPARATOR);
        for (int i = 0; i < messages.length; i++){
            System.out.println(messages[i]);
        }
        int type = Integer.valueOf(messages[0]);
        if (type == MessageType.startGame.value){
            announce(type, messages[1] + SEPARATOR + messages[2]);
            announce(MessageType.removeUser.value, messages[1]);
            announce(MessageType.removeUser.value, messages[2]);
            loby.removeUser(messages[1]);
            loby.removeUser(messages[2]);
            loby.createGame(messages[1] + SEPARATOR + messages[2]);
            game.setPlaying(true);
        } else if (type == MessageType.move.value) {
            String gameName = messages[1] + SEPARATOR + messages[2];
            int row = Integer.valueOf(messages[3]);
            int column = Integer.valueOf(messages[4]);
            boolean didMove = loby.move(gameName, row, column);
            if (didMove){
                announce(MessageType.move.value, messages[1] + SEPARATOR + messages[2] + SEPARATOR + messages[3] + SEPARATOR + messages[4]);
                String winner = loby.endGame(gameName, row, column, messages[1]);
                System.out.println("" + winner);
                if (winner != null){
                    announce(MessageType.endGame.value, gameName + SEPARATOR + winner);
                }
            }
        } else if (type == MessageType.endGame.value) {
            System.out.println("endgame " + name);
            addToGame();
        }
        
    }
    
}
