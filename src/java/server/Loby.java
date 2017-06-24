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
    public static final String SEPARATOR = ";;";
    
    private static final int ROWS = 5;
    private static final int COLUMNS = 7;
    
    //User names
    private Set<String> users = new HashSet<String>();
    //gameName -> x,y locations
    private Map<String, Integer[][]> games = new HashMap<String, Integer[][]>();
    //gameName -> who is on turn
    private Map<String, String> gamesPlayerTurn = new HashMap<String, String>();
    //gamaName -> done or not
    private Map<String, Boolean> gamesEnd = new HashMap<String, Boolean>();
    
    public Loby() {
    }
    
    public Set<String> getUsers() {
        return users;
    }
    
    public void addUser(String name) {
        users.add(name);
    }
    
    public void removeUser(String name) {
        users.remove(name);
    }

    void createGame(String string) {
        games.put(string, new Integer[ROWS][COLUMNS]);
        gamesPlayerTurn.put(string, string.split(SEPARATOR)[0]);
        gamesEnd.put(string, Boolean.FALSE);
    }

    boolean didMove(String gameName, int row, int column) {
        String firstName = gameName.split(SEPARATOR)[0];
        String secondName = gameName.split(SEPARATOR)[1];
        gameName = getGameName(gameName, firstName, secondName);
        int point = 0;
        if (!gamesPlayerTurn.get(gameName).equals(gameName.split(SEPARATOR)[0])) {
            point = 1;
        }
        Integer[][] game = games.get(gameName);
        
        if (gamesEnd.get(gameName) != true) {
            if (gamesPlayerTurn.get(gameName).equals(firstName)){
                if (game[row][column] == null){
                    if (row == game.length - 1) {
                        if (game[row][column] == null){
                            game[row][column] = point;
                            gamesPlayerTurn.put(gameName, secondName);
                            return true;
                        }
                    } else {
                        if (game[row+1][column] != null){
                            game[row][column] = point;
                            gamesPlayerTurn.put(gameName, secondName);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getGameName(String gameName, String firstName, String secondName) {
        if (!games.containsKey(gameName)){
            gameName = secondName + SEPARATOR + firstName;
        }
        return gameName;
    }

    public String checkWinner(String gameName, int r, int c) {
        String fss = gameName.split(SEPARATOR)[0];
        String sec = gameName.split(SEPARATOR)[1];
        gameName = getGameName(gameName, fss, sec);
        Integer[][] game = games.get(gameName);
        
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
              gamesEnd.put(gameName, Boolean.TRUE);
              return fss;
            }
        }
        
        for (int i = 0; i < 5;i++) {
            for (int j = 0; j < 7;j++) {
                if (game[i][j] == null) {
                    return null;
                }
            }
        }
        gamesEnd.put(gameName, Boolean.TRUE);
        return "";
    }
    
}
