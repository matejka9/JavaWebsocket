/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Loby {
    
    private Set<String> users = new HashSet<String>();
    private Map<String, Integer[][]> games = new HashMap<String, Integer[][]>();
    private Map<String, String> turns = new HashMap<String, String>();
    private Map<String, Boolean> ends = new HashMap<String, Boolean>();
    
    public Loby() {
        System.out.println("Vytvaram loby");
    }

    public int getNumberOfUsers() {
        return users.size();
    }

//    public void setNumberOfUsers(int numberOfUsers) {
//        this.numberOfUsers = numberOfUsers;
//    }
    
    public Set<String> getUsers() {
        return users;
    }
    
    public void addToUsers(String name) {
        //TODO posli vsetkym doterajsim userom informaciu o novom
        users.add(name);
    }
    
    public void removeFromUsers(String name) {
        //TODO posli vsetkym doterajsim userom informaciu o odstraneni usra
        users.remove(name);
    }

    void createGame(String string) {
        games.put(string, new Integer[5][7]);
        turns.put(string, string.split(";")[0]);
        ends.put(string, Boolean.FALSE);
    }

    boolean move(String gameName, int row, int column) {
        String firstName = gameName.split(";")[0];
        String secondName = gameName.split(";")[1];
        if (!games.containsKey(gameName)){
            gameName = secondName + ";" + firstName;
        }
        int point = 0;
        if (!turns.get(gameName).equals(gameName.split(";")[0])) {
            point = 1;
        }
        Integer[][] game = games.get(gameName);
        System.out.println(turns.get(gameName));
        System.out.println(firstName);
        
        if (ends.get(gameName) != true) {
            if (turns.get(gameName).equals(firstName)){
                if (game[row][column] == null){
                    if (row == game.length - 1) {
                        if (game[row][column] == null){
                            game[row][column] = point;
                            turns.put(gameName, secondName);
                            return true;
                        }
                    } else {
                        if (game[row+1][column] != null){
                            game[row][column] = point;
                            turns.put(gameName, secondName);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String endGame(String gameName, int r, int c, String firstName) {
        String fss = gameName.split(";")[0];
        String sec = gameName.split(";")[1];
        if (!games.containsKey(gameName)){
            gameName = sec + ";" + fss;
        }
        Integer[][] game = games.get(gameName);
        System.out.println("" + game);
        
        Integer[][] dir = {{-1, 0}, {0, -1}, {-1, -1}, {-1, 1}};
        for (int d = 0; d < 4; d++)
        {
          int sr = r;
          int sc = c;
          while ((sr + dir[d][0] >= 0) && (sc + dir[d][1] >= 0) && (sc + dir[d][1] < 7))

              
              
              if (game[sr][sc] == game[sr + dir[d][0]][sc + dir[d][1]])
            {
              sr += dir[d][0];
              sc += dir[d][1];
            }
            else break;
          int er = r;
          int ec = c;
          while ((er - dir[d][0] < 5) && (ec - dir[d][1] < 7) && (ec - dir[d][1] >= 0))
            if (game[er][ec] == game[er - dir[d][0]][ec - dir[d][1]])
            {
              er -= dir[d][0];
              ec -= dir[d][1];
            }
            else break;
          if ((er - sr >= 3) || (ec - sc >= 3)) 
          {
              //Vyhral
              ends.put(gameName, Boolean.TRUE);
            return firstName;
          }
        }
        
        for (int i = 0; i < 5;i++) {
            for (int j = 0; j < 7;j++) {
                if (game[i][j] == null) {
                    //Este nie je koniec hry
                    return null;
                }
            }
        }
        //Remiza
        ends.put(gameName, Boolean.TRUE);
        return "";
    }
    
}