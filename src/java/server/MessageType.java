/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

public enum MessageType {
    addUser(1), removeUser(2), startGame(3), move(4), endGame(5);
    
    public int value;

    MessageType(int value){
        this.value = value;
    }
}
