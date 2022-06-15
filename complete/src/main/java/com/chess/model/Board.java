package com.chess.model;

import java.util.Arrays;
import java.util.StringJoiner;
//import netscape.javascript.JSObject;


/**
 * board
 */
public class Board {

    private int[][] b = new int[8][8];

    public Board(){
        for (int i=0; i<2; i++)
            for (int j=0; j<8; j++)
                b[i][j]=1;//mark of WHITE
        for (int i=2; i<6; i++)
            for (int j=0; j<8; j++)
                b[i][j]=0;
        for (int i=6; i<8; i++)
            for (int j=0; j<8; j++)
                b[i][j]=2;//mark of BLACK
    }
    public Boolean empty(int x, int y){
        return b[y-1][x-1]==0;
    }
    public boolean samePlayer(int sourceX, int sourceY, int targetX, int targetY){
        return b[targetY-1][targetX-1]==b[sourceY-1][sourceX-1];
    }
    public void move(int sourceX, int sourceY, int targetX, int targetY)
    {
        b[targetY-1][targetX-1]=b[sourceY-1][sourceX-1];
        b[sourceY-1][sourceX-1]=0;
    }
    public int getVal(int x, int y){return b[y-1][x-1];}
    public void setVal(int x, int y, int val){b[y-1][x-1]=val;}
    public void getBack(int x, int y, int color){b[y-1][x-1]=color;}
    public int[][] getBoard(){return b;}

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (int[] row : b) {
            sj.add(Arrays.toString(row));
        }   
        return sj.toString();
        //return Arrays.deepToString(b);
    }
    
    // public JSObject toSObject() {
    //     JSObject jo = new JSObject();
    //     for(int i=0;i<8;i++)
    //         ((Object) jo).put(String.valueOf(i), b[i]);
    //     System.out.println(jo.toString());
    //     return jo;   
    // }
   

//     @Override
//   public String toString() {
//     return String.format(
//         "Customer[id=%d, firstName='%s', lastName='%s']",
//         id, firstName, lastName);
//   }

}