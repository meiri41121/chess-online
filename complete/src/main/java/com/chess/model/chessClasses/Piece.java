package com.chess.model.chessClasses;

enum Player{PAWN, KNIGHT, BISHOF, ROOK, QUEEN, KING};
enum color{WHITE, BLACK};

public class Piece {

    Pos myPos;
    Player name;
    boolean col; //0 refers to WHITE, 1 to BLACK
    int value;
    boolean initial;
    
    Piece(){}
    Piece(Pos p, Player pl, Boolean c,Boolean ini)
    {
        myPos=new Pos(p.posX,p.posY);
        name=pl;
        col=c;
        initial=ini;
        move(myPos.posX,myPos.posY,initial);
        //setValue();
    }
    Piece(Pos p, Player pl, Boolean c)
    {
        this(p,pl,c,true);
    }
    void setValue(){
        move(myPos.posX,myPos.posY,initial);
        /*switch (name) {
            case PAWN:
                value = col==WHITE? 10: -10;
                break;
            case KNIGHT:
                value = col==WHITE? 30: -30;
                break;
            case BISHOF:
                value = col==WHITE? 30: -30;
                break;
            case ROOK:
                value = col==WHITE? 50: -50;
                break;
            case QUEEN:
                value = col==WHITE? 90: -90;
                break;
            case KING:
                value = col==WHITE? 900: -900;
                break;
        }*/
    }
    Player getName(){return name;}
    boolean getCol(){return col;}
    public Pos getPos(){return myPos;}
    boolean getInit(){return initial;}
    int getValue(){return value;}
    //int move(int x,int y){return move(x,y,false);}
    int move(int x,int y){return move(x,y,false);}
    int move(int x,int y,Boolean fm){
        myPos.move(x,y);
        initial=fm;
        y=col?9-y:y;
        int v=value;
        switch (name) {
            case PAWN:
                if(y==1 || (y==3 && (x==4|| x==5)) || (y==4 && x!=4 && x!=5) || y==8)
                    value = col? -20:20;
                else if(((y==2 ||y==3) && (x==1|| x==8)) || (y==5 && (x==1|| x==2 || x==7|| x==8)))
                    value = col? -21:21;
                else if((y==2 && (x==2|| x==3 || x==6|| x==7)) || (y==5 && (x==3 || x==6)) || (y==6 && (x==1|| x==2 || x==7|| x==8)))
                    value = col? -22:22;
                else if((y==4 && (x==4|| x==5)) || (y==6 && (x==3|| x==6)))
                    value = col? -24:24;
                else if(y==5 && (x==4|| x==5))
                    value = col? -25:25;
                else if(y==6 && (x==4|| x==5))
                    value = col? -26:26;
                else if(y==7)
                    value = col? -30:30;
                else if(y==3 && (x==2|| x==7))
                    value = col? -19:19;
                else if(y==3 && (x==3|| x==6))
                    value = col? -18:18;
                else if(y==2 && (x==4|| x==5))
                    value = col? -16:16;
                break;
            case KNIGHT:
                if((y==1 ||y==8) && (x==1|| x==8))
                    value = col? -50:50;
                else if(((y==2 || y==7) && (x==1 || x==8)) || ((y==1 || y==8) && (x==2 || x==7)))
                    value = col? -52:52;
                else if(((y>2 && y<7) && (x==1 || x==8)) || ((y==1 || y==8) && (x>2 && x<7)))
                    value = col? -54:54;
                else if((y==2 || y==7) && (x==2 || x==7))
                    value = col? -56:56;
                else if((y==7 && (x>2 && x<7)) || ((y==4 || y==6) && (x==2 || x==7)) || (y==2 && (x==3|| x==6)))
                    value = col? -60:60;
                else if((y==2 && (x==4 || x==5)) || (y==3 || y==5) && (x==2 || x==7))
                    value = col? -61:61;
                else if((y==3 || y==6) && (x==3 || x==6))
                    value = col? -62:62;
                else if(((y==4 || y==5) && (x==3 || x==6)) || ((y==3 || y==6) && (x==4 || x==5)))
                    value = col? -63:63;
                else if((y==4 || y==5) && (x==4 || x==5))
                    value = col? -64:64;
                break;
            case BISHOF:
                if((y==1 || y==8) && (x==1 || x==8))
                    value = col? -56:56;
                else if(((y>1 && y<8) && (x==1 || x==8)) || ((y==1 || y==8) && (x>1 && x<8)))
                    value = col? -58:58;
                else if(((y==4 || y==6) && (x==2 || x==7)) || (y==7 && (x>1 && x<8)) || (y==2 && (x>2 && x<7)))
                    value = col? -60:60;
                else if((y==2 && (x==2 || x==7)) || (y==5 && (x==2 || x==3 || x==6 || x==7)) || (y==6 && (x==3 || x==6)))
                    value = col? -61:61;
                else if(((y==5 || y==6) && (x==4 || x==5)) || (y==3 && (x>1 && x<8)) || (y==4 && (x>2 && x<7)))
                    value = col? -62:62;
                break;
            case ROOK:
                if((y>1 && y<7) && (x==1 || x==8))
                    value = col? -99:99;
                else if(y==8 || (y==1 && x!=4 && x!=5) || ((y>1 && y<7) && (x>1 || x<8)))
                    value = col? -100:100;
                else if((y==1 && (x==4 || x==5)) || (y==7 && (x==1 || x==8)))
                    value = col? -101:101;
                else if(y==7 && (x>1 && x<8))
                    value = col? -102:102;
                break;
            case QUEEN:
                if((y==1 || y==8) && (x==1 || x==8))
                    value = col? -176:176;
                else if(((y==2 || y==3 || y==6 || y==7) && (x==1 || x==8)) || ((x==2 || x==3 || x==6 || x==7) && (y==1 || y==8)))
                    value = col? -178:178;
                else if(((y==1 || y==8) && (x==4 || x==5)) || (y==5 && (x==1 || x==8)))
                    value = col? -179:179;
                else if(((y==4 || y==5 || y==6) && (x==2 || x==7)) || (y==7 && (x>1 && x<8)) || (y==2 && (x!=1 && x!=3 && x!=8)) || (y==3 && x==7) || (y==4 && x==1))
                    value = col? -180:180;
                else if(((y>1 && y<7) && (x>2 && x<7)) || (y==3 && x==2) || (y==2 && x==3))
                    value = col? -181:181;
                break;
            case KING:
                if((y>4 && y<9) && (x==4 || x==5))
                    value = col? -1790:1790;
                else if(((y>4 && y<9) && (x==2 || x==3 || x==6 || x==7)) || (y==4 && (x==4 || x==5)))
                    value = col? -1792:1792;
                else if(((y>4 && y<9) && (x==1 || x==8)) || (y==4 && (x==2 || x==3 || x==6 || x==7)))
                    value = col? -1794:1794;
                else if((y==4 && (x==1 || x==8)) || (y==3 && (x>1 && x<8)))
                    value = col? -1796:1796;
                else if(y==3 && (x==1 || x==8))
                    value = col? -1798:1798;
                else if((y==1 && (x==4 || x==5)) || (y==2 && (x>2 && x<7)))
                    value = col? -1800:1800;
                else if(y==1 && (x==3 || x==6))
                    value = col? -1802:1802;
                else if(((y==1 || y==2) && (x==1 || x==8)) || (y==2 && (x==2 || x==7)))
                    value = col? -1804:1804;
                else if(y==1 && (x==2 || x==7))
                    value = col? -1806:1806;
        }
        return value-v;
    }
    int become(Player p){name=p; int v=value; move(myPos.posX,myPos.posY,initial); return value-v;}
    boolean validMove(Board b, Pos target){
        if (!target.valid() || b.samePlayer(myPos.posX, myPos.posY, target.posX, target.posY))
            return false;
        int dx = target.posX - myPos.posX;
        int dy = target.posY - myPos.posY;
        boolean flagR = true,flagB = true;
        switch (name) {
            case PAWN:
                if(
                   (!col && ( //WHITE Player
    /*1 steps*/         (dy==1 && dx==0 && b.empty(target.posX, target.posY)) ||
    /*eat in x*/        (dy==1 && (dx==1 || dx==-1) &&
                        !b.samePlayer(myPos.posX, myPos.posY, target.posX, target.posY) && !b.empty(target.posX,target.posY)) ||
    /*2 steps*/         (myPos.posY==2 && dy==2 && dx==0 &&
                        b.empty(target.posX,target.posY) && b.empty(target.posX,target.posY-1)))) ||
                   (col && ( //BLACK Player
                        (dy==-1 && dx==0 && b.empty(target.posX, target.posY)) ||
                        (dy==-1 && (dx==1 || dx==-1) &&
                        !b.samePlayer(myPos.posX, myPos.posY, target.posX, target.posY) && !b.empty(target.posX,target.posY)) ||
                        (myPos.posY==7 && dy==-2 && dx==0 &&
                        b.empty(target.posX, target.posY) && b.empty(target.posX,target.posY+1)))))
                    return true;
                break;
            case KNIGHT:
                if((target.posY==myPos.posY+1 && target.posX==myPos.posX+2) || (target.posY==myPos.posY+2 && target.posX==myPos.posX+1) || (target.posY==myPos.posY-1 && target.posX==myPos.posX-2) || (target.posY==myPos.posY-2 && target.posX==myPos.posX-1) || (target.posY==myPos.posY+1 && target.posX==myPos.posX-2) || (target.posY==myPos.posY+2 && target.posX==myPos.posX-1) || (target.posY==myPos.posY-1 && target.posX==myPos.posX+2) || (target.posY==myPos.posY-2 && target.posX==myPos.posX+1))
                    return true;
                break;
            case BISHOF:
                if(dy != dx && dy != -dx)
                    return false;
                for (int i=1, d= dx>0? dx:-dx; i<d; i++)
                    if(!b.empty(myPos.posX+dx/d*i,myPos.posY+dy/d*i))
                        return false;
                return true;
            case ROOK:
                if((dy!=0 && dx!=0) || (dx==0 && dy==0))
                    return false;
                if(dx!=0)
                    for (int i=1,d = (dx>0? dx:-dx); i<d; i++)
                        if(!b.empty(myPos.posX+dx/d*i,myPos.posY))
                            return false;
                if(dy!=0)
                    for (int i=1,d = (dy>0? dy:-dy); i<d; i++)
                        if(!b.empty(myPos.posX,myPos.posY+d/dy*i))
                            return false;
                return true;
            case QUEEN:
                if(dy != dx && dy != -dx)
                    flagB = false;
                if((dy!=0 && dx!=0) || (dx==0 && dy==0))
                    flagR = false;
                for (int i=1, d= dx>0? dx:-dx; i<d && flagB; i++)
                    if(!b.empty(myPos.posX+dx/d*i,myPos.posY+dy/d*i))
                        flagB = false;
                if(flagR){
                    if(dx != 0)
                        for (int i=1,d = (dx>0? dx:-dx); i<d && flagR; i++)
                            if(!b.empty(myPos.posX+dx/d*i,myPos.posY))
                                flagR = false;
                    if(dy != 0)
                        for (int i=1,d = (dy>0? dy:-dy); i<d && flagR; i++)
                            if(!b.empty(myPos.posX, myPos.posY+dy/d*i))
                                flagR = false;
                }
                return flagR || flagB;
            case KING:
                if(dx==2 && dy==0 && initial && b.empty(6,target.posY) && b.empty(7,target.posY))
                    return true;
                else if(dx==-2 && dy==0 && initial && b.empty(2,target.posY) && b.empty(3,target.posY) && b.empty(4,target.posY))
                    return true;
                else if((dx>-2 && dx<2) && (dy>-2 && dy<2))
                    return true;
                break;
        }
        return false;
    }

}