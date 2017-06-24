/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

public enum Types {
    CREATE(1), REMOVE(2), START(3), MOVE(4), END(5);
    
    public int value;

    Types(int value){
        this.value = value;
    }
}
