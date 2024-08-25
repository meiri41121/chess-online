package com.chess.model.chessClasses;
import java.util.*;

public class Game{

    private int value = 0;
    private Board b = new Board();
    private List<Piece> piecesW = new ArrayList<Piece>();
    private List<Piece> piecesB = new ArrayList<Piece>();
    private List<Piece> eaten = new ArrayList<Piece>();
    private List<Movemant> moves = new ArrayList<Movemant>();
    private List<Move> validMovesList = new ArrayList<Move>();
    private boolean col=false;
    private int index = 0;
    private int difficulty = 1;
    Scanner myObj = new Scanner(System.in);
    
    public int getValue(){ return value; }
    public Board getB(){ return b; }
    public List<Piece> getPiecesW(){ return piecesW; }
    public List<Piece> getPiecesB(){ return piecesB; }
    public List<Piece> getEaten(){ return eaten; }
    public List<Movemant> getMoves(){ return moves; }
    public String getTurn(){ updateCol();return (col?"BLACK":"WHITE") + " move: " + (check()?" [check!!]":""); }
    public boolean getCol(){return col;}

    public Game(int difficulty){
        setGame();
        if(difficulty>0 && difficulty<6)
            this.difficulty = difficulty;
        boolean pcColor=(new Random().nextInt(2))!=0?true:false;
        //boolean pcColor = true;
        if(!pcColor)
            doBestMove(difficulty);
    }

    public void recoveryGame(){
        b = new Board();
        piecesW = new ArrayList<Piece>();
        piecesB = new ArrayList<Piece>();
        eaten = new ArrayList<Piece>();
        moves = new ArrayList<Movemant>();
        col=false;
        setGame();
    }

    void setGame(){
        for(int i=0;i<8;i++)
            piecesW.add(new Piece(new Pos(i+1,2), Player.PAWN, false));
        piecesW.add(new Piece(new Pos(1,1), Player.ROOK, false));
        piecesW.add(new Piece(new Pos(8,1), Player.ROOK, false));
        piecesW.add(new Piece(new Pos(2,1), Player.KNIGHT, false));
        piecesW.add(new Piece(new Pos(7,1), Player.KNIGHT, false));
        piecesW.add(new Piece(new Pos(3,1), Player.BISHOF, false));
        piecesW.add(new Piece(new Pos(6,1), Player.BISHOF, false));
        piecesW.add(new Piece(new Pos(4,1), Player.QUEEN, false));
        piecesW.add(new Piece(new Pos(5,1), Player.KING, false));
        for(int i=0;i<8;i++)
            piecesB.add(new Piece(new Pos(i+1,7), Player.PAWN, true));
        piecesB.add(new Piece(new Pos(1,8), Player.ROOK, true));
        piecesB.add(new Piece(new Pos(8,8), Player.ROOK, true));
        piecesB.add(new Piece(new Pos(2,8), Player.KNIGHT, true));
        piecesB.add(new Piece(new Pos(7,8), Player.KNIGHT, true));
        piecesB.add(new Piece(new Pos(3,8), Player.BISHOF, true));
        piecesB.add(new Piece(new Pos(6,8), Player.BISHOF, true));
        piecesB.add(new Piece(new Pos(4,8), Player.QUEEN, true));
        piecesB.add(new Piece(new Pos(5,8), Player.KING, true));
    }

    Move doRandomMove()
    {
        Random rand = new Random();
        int random_number = rand.nextInt(validMovesList.size());
        Move m = validMovesList.get(random_number);
        move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY);
        return new Move(m.getSource(),m.getTarget());
    }
    
    public String doBestMove(){ return doBestMove(difficulty);}
    String doBestMove(int n)
    {
        Move p = getBestMove(n);
        move(p.getTarget().posX,p.getTarget().posY,p.getSource().posX,p.getSource().posY);
        return ("pc move:" + (char)(p.getSource().posX+'a'-1) + p.getSource().posY + " -> " + (char)(p.getTarget().posX+'a'-1) + p.getTarget().posY);
    }
    
    Move getBestMove(){return getBestMove(1);}
    Move getBestMove(int n)
    {
        allValidMoves();
        List<Move> updateValidMovesList = new ArrayList<Move>(validMovesList);
        index=0;
        Move p = doRandomMove();
        if(n==0) return p;
        int a=-5000,b=5000,v=BM(n-1,a,b);
        undo();
        for(Move m : updateValidMovesList)
        {
            move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY);//add move, col dont change
            int val = BM(n-1,a,b);
            undo();
            if((col && val<v) || (!col && val>v))
            {
                v = val;
                p = new Move(m.getSource(),m.getTarget());
            }
            a = (!col && v>a)? v:a;
            b = (col && v<b)? v:b;
            if(a>b) break;
        }
        System.out.println(index);;
        return p;
    }
    
    int BM(int n,int a,int b)
    {
        index++;
        if (n==0) return value;
        int v=value;
        allValidMoves();
        if(validMovesList.isEmpty())
            v = check()? (col? 1000:-1000): 0;
        List<Move> updateValidMovesList = new ArrayList<Move>(validMovesList);
        for(Move m : updateValidMovesList)
        {
            move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY);//not change col
            int val=BM(n-1,a,b);
            undo();  // col return back the right
            if((col && val<v) || (!col && val>v))
                v=val;
            a = (!col && v>a)? v:a;
            b = (col && v<b)? v:b;
            if(a>b) break;
        }
        return v;
    }

    public String play(String mov){
        String result = "";
        boolean flag=false;
        if("print".equals(mov))
        {   
            allValidMoves();
            for(Move m: validMovesList)
                result+=((char)(m.getSource().posX+'a'-1)+"" + m.getSource().posY + " -> " + (char)(m.getTarget().posX+'a'-1) + m.getTarget().posY)+".\n";
            return result;
        }
        else if("help".equals(mov)){
            Move p=getBestMove(4);
            result+=((char)(p.getSource().posX+'a'-1)+"" + p.getSource().posY + " -> " + (char)(p.getTarget().posX+'a'-1) + p.getTarget().posY);
            return result;
        }
        else if(mov.length()==2 && validTarget(mov.charAt(0), mov.charAt(1)))//PAWN move [example 'd4']
            flag = move(mov.charAt(0)-'a'+1, mov.charAt(1)-'1'+1, Player.PAWN, 0, 0);
        else if(mov.length()==3 && validTarget(mov.charAt(1), mov.charAt(2)) && what(mov.charAt(0))!=Player.PAWN)//other piece move [example 'Nf3']
            flag = move(mov.charAt(1)-'a'+1, mov.charAt(2)-'1'+1, what(mov.charAt(0)), 0, 0);
        else if(mov.length()==4 && mov.charAt(1)=='x' && validTarget(mov.charAt(0),'2') && validTarget(mov.charAt(2), mov.charAt(3))) //PAWN attack [example 'dxe5']
            flag = move(mov.charAt(2)-'a'+1, mov.charAt(3)-'1'+1, Player.PAWN, mov.charAt(0)-'a'+1, 0);
        else if(mov.length()==5 && what(mov.charAt(0))!=Player.PAWN && mov.charAt(0)!='K' &&validTarget(mov.charAt(1), mov.charAt(2)) && validTarget(mov.charAt(3), mov.charAt(4)))//when there is several options to move [example 'Nf3d4']
            flag = move(mov.charAt(3)-'a'+1, mov.charAt(4)-'1'+1, what(mov.charAt(0)), mov.charAt(1)-'a'+1, mov.charAt(2)-'1'+1);
        else if(mov.compareTo("undo")==0)
            if(moves.size()>1) {undo();undo();return "undo";}
        return flag? "true":"false";
    }

    public String play2(String mov){
        if(mov.compareTo("undo")==0){
            if(moves.size()>1) {undo();undo();return "undo";}
            else return "false";
        }
        char sourceX = mov.charAt(0);
        char sourceY = mov.charAt(1);
        char targetX = mov.charAt(4);
        char targetY = mov.charAt(5);
        boolean flag = move(targetX - 'A' + 1, Character.getNumericValue(targetY), sourceX - 'A' + 1, Character.getNumericValue(sourceY));
        return flag? "true":"false";
    }
    
    boolean validTarget(char letter, char num)
    { return letter>='a' && letter<='h' && num>='1' && num<='8'; }
    
    Player what(char c)
    {
        if(c=='N') return Player.KNIGHT;
        else if(c=='B') return Player.BISHOF;
        else if(c=='R') return Player.ROOK;
        else if(c=='Q') return Player.QUEEN;
        else if(c=='K') return Player.KING;
        return Player.PAWN;
    }

    public void recoveryMove(int targetX, int targetY, int sourceX, int sourceY, char became){
        updateCol();
        for(Piece p : col?piecesB:piecesW)
            if(p.getPos().equal(new Pos(sourceX, sourceY)))
            {
                if(Math.abs(p.getPos().posX - targetX) == 2 && p.getName() == Player.KING){//hazraha..
                    for(Piece r : col?piecesB:piecesW)
                        if(r.getName()==Player.ROOK && r.getInit() && r.getPos().posX==targetX+1 || r.getPos().posX==targetX-2)
                            {
                                moves.add(0, new Movemant(new Pos(r.getPos().posX,targetY), new Pos(r.getPos().posX==8?6:4,targetY),false,false,r.getInit()));
                                b.move(r.getPos().posX,targetY,r.getPos().posX==8?6:4, targetY);
                                value += r.move(r.getPos().posX==8?6:4, targetY);
                                moves.add(0, new Movemant(new Pos(p.getPos().posX,targetY),new Pos(targetX, targetY),false, true, p.getInit()));
                                b.move(p.getPos().posX,targetY,targetX, targetY);
                                value += p.move(targetX, targetY);
                                break;
                            }
                }
                else{
                    boolean flagP = false, flag = false;  //flagP = Pawn goes to kill
                    if(p.getName()==Player.PAWN && p.getPos().posX!=targetX && b.empty(targetX, targetY))
                        flagP = true;
                    if(!b.empty(targetX, targetY) || flagP)
                        for(Piece die : col?piecesW:piecesB)
                            if((die.getPos().posX==targetX && die.getPos().posY==targetY) || (flagP && die.getPos().equal(moves.get(0).getTarget())))
                                {kill(die); flag=true; break;}
                    moves.add(0, new Movemant(p.getPos(), new Pos(targetX, targetY), flag, false, p.getInit()));
                    b.move(p.getPos().posX,p.getPos().posY,targetX, targetY);
                    value += p.move(targetX, targetY);
                    if(((!col && targetY==8) || (col && targetY==1)) && p.getName()==Player.PAWN){
                        value += p.become(what(became));
                        moves.get(0).setChange(became);
                    }
                }                
            }
    }

    boolean move(int targetX, int targetY, int sourceX, int sourceY)//get players name
    {
        updateCol();
        for(Piece p : col?piecesB:piecesW)
            if(p.getPos().equal(new Pos(sourceX,sourceY)))
                return move(targetX, targetY, p.getName(), sourceX, sourceY);
        return false;
    }

    boolean move(int targetX, int targetY, Player name, int sourceX, int sourceY)
    {
        updateCol();
        for(Piece p : col?piecesB:piecesW)
        {
            if(p.getName()==name && (sourceX==0 || p.getPos().posX==sourceX) && (sourceY==0 || p.getPos().posY==sourceY))
                if(validMove(p, new Pos(targetX,targetY)))
                {
                    if(Math.abs(p.getPos().posX - targetX) == 2 && name == Player.KING){//hazraha..
                        if(!(underthreat(p.getPos().posX, targetY, col) || underthreat(targetX,targetY, col) || underthreat((targetX+p.getPos().posX)/2,targetY, col)))
                        for(Piece r : col?piecesB:piecesW)
                            if(r.getName()==Player.ROOK && r.getInit() && (r.getPos().posX==targetX+1 || r.getPos().posX==targetX-2))
                                {
                                    moves.add(0, new Movemant(new Pos(r.getPos().posX,targetY), new Pos(r.getPos().posX==8?6:4,targetY),false,false,r.getInit()));
                                    b.move(r.getPos().posX,targetY,r.getPos().posX==8?6:4, targetY);
                                    value += r.move(r.getPos().posX==8?6:4, targetY);
                                    moves.add(0, new Movemant(new Pos(p.getPos().posX,targetY),new Pos(targetX, targetY),false, true, p.getInit()));
                                    b.move(p.getPos().posX,targetY,targetX, targetY);
                                    value += p.move(targetX, targetY);
                                    return true;
                                }
                        return false;
                    }
                    boolean flagP = false, flag = false;  //flagP = Pawn goes to kill
                    if(p.getName()==Player.PAWN && p.getPos().posX!=targetX && b.empty(targetX, targetY))
                        flagP = true;
                    if(!b.empty(targetX, targetY) || flagP)
                        for(Piece die : col?piecesW:piecesB)
                            if((die.getPos().posX==targetX && die.getPos().posY==targetY) || (flagP && die.getPos().equal(moves.get(0).getTarget())))
                                {kill(die); flag=true; break;}
                    moves.add(0, new Movemant(p.getPos(), new Pos(targetX, targetY), flag, false, p.getInit()));
                    b.move(p.getPos().posX,p.getPos().posY,targetX, targetY);
                    value += p.move(targetX, targetY);
                    if(((!col && targetY==8) || (col && targetY==1)) && name==Player.PAWN){
                        value += p.become(what('Q'));
                        moves.get(0).setChange('Q');
                    }
                    return true;
                }
        }
        return false;
    }

    boolean underthreat(int targetX, int targetY, boolean col) {
        int n = b.getVal(targetX,targetY);
        b.setVal(targetX,targetY, col?2:1);
        for(Piece p : col?piecesW:piecesB)
            if(p.validMove(b, new Pos(targetX,targetY)))
                {b.setVal(targetX,targetY, n); return true;}
        b.setVal(targetX,targetY, n);
        return false;
    }

    void kill(Piece die) {
        value-=die.getValue();
        eaten.add(0, new Piece(die.getPos(),die.getName(),die.getCol(),die.getInit()));
        b.setVal(die.getPos().posX, die.getPos().posY, 0);
        if(die.getCol()) piecesB.remove(die);
        else piecesW.remove(die);
    }

    void updateCol(){
        if(moves.isEmpty())
            col = false;
        else
            col = b.getVal(moves.get(0).getTarget().posX,moves.get(0).getTarget().posY)==1 ? true : false;
    }

    boolean validMove(Piece p, Pos target) {
        boolean flagP=false, flag=false;
        if(!moves.isEmpty())
            if(p.getName()==Player.PAWN && moves.get(0).getFM() && moves.get(0).getTarget().posY==(p.getCol()?4:5) && moves.get(0).getSource().posY==(p.getCol()?2:7) && new Pos(moves.get(0).getTarget().posX,(p.getCol()?3:6))==target && Math.abs(p.getPos().posX-target.posX)==1 && p.getPos().posY==(p.getCol()?4:5))
                {b.setVal(target.posX, target.posY, col?1:2); flagP=true;}
        if(p.validMove(b, target))
        {
            if(p.getName()==Player.KING && Math.abs(p.getPos().posX-target.posX) == 2){//hatzraha
                if(!(underthreat(p.getPos().posX,p.getPos().posY, col) || underthreat(target.posX,target.posY, col) || underthreat((target.posX+p.getPos().posX)/2,target.posY, col)))
                    for(Piece r : col?piecesB:piecesW)
                        if(r.getName()==Player.ROOK && r.getInit() && (r.getPos().posX==target.posX+1 || r.getPos().posX==target.posX-2))
                            return true;
                return false;}
            if(!b.empty(target.posX, target.posY))//eating
                for(Piece die : col?piecesW:piecesB)
                    if(die.getPos().posX==target.posX && (die.getPos().posY==target.posY || (flagP && die.getPos().posY==(getCol()?4:5))))
                    {kill(die); flag=true; break;}
            b.move(p.getPos().posX,p.getPos().posY,target.posX, target.posY);
            moves.add(0, new Movemant(p.getPos(),target,flag,false,p.getInit()));
            value += p.move(target.posX, target.posY);
            flag=check(p.getCol());
            undo();
            return !flag;
        }
        return false;
    }

    boolean undo() {
        updateCol();
        if(moves.isEmpty()) return false;
        Movemant m = moves.get(0);
        moves.remove(0);
        for(Piece p : col?piecesW:piecesB)
            if(p.getPos().posX==m.getTarget().posX && p.getPos().posY==m.getTarget().posY){
                value += p.move(m.getSource().posX, m.getSource().posY,m.getFM());
                if(m.getChange()!='P') value += p.become(Player.PAWN);
                break;
            }
        b.move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY);
        if(m.getEat()){
            if(col)piecesB.add(eaten.get(0));
            else piecesW.add(eaten.get(0));
            b.getBack(eaten.get(0).getPos().posX,eaten.get(0).getPos().posY, col?2:1);
            value += eaten.get(0).getValue();
            eaten.remove(0);
        }
        updateCol();
        return m.getHazraha()? undo():true;
    }

    boolean check() {return check(col);}
    boolean check(boolean col) {
        for(Piece p : col?piecesB:piecesW)
            if(p.getName()==Player.KING)
                return underthreat(p.getPos().posX,p.getPos().posY, col);
        return false;
    }

    public String printPieceByPosition(int x,int y, int c)
    {
        if(c==0) return null;
        boolean co = c==1?false:true;
        String s="";
        for (Piece i : co?piecesB:piecesW)
            if(i.getPos().posX==x && i.getPos().posY==y)
                switch (i.getName()) {
                    case PAWN:
                        s = co ? "/images/blackPawn.png" : "/images/whitePawn.png";
                        break;
                    case KNIGHT:
                        s = co ? "/images/blackKnight.png" : "/images/whiteKnight.png";
                        break;
                    case BISHOF:
                        s = co ? "/images/blackBishop.png" : "/images/whiteBishop.png";
                        break;
                    case ROOK:
                        s = co ? "/images/blackRock.png" : "/images/whiteRock.png";
                        break;
                    case QUEEN:
                        s = co ? "/images/blackQueen.png" : "/images/whiteQueen.png";
                        break;
                    case KING:
                        s = co ? "/images/blackKing.png" : "/images/whiteKing.png";
                        break;
                }
        if(s=="")
            System.out.println("error\n");
        //s+=co?"d":"l";
        return s;
    }

    public boolean checkMat()
    {
        allValidMoves();
        if(!check() && checkPat()) return true;
        if(!(validMovesList.isEmpty())) return false;
        //print();
        //System.out.print(col?"\nWHITE":"\nBLACK" + " Player win!!!\n");//
        //System.out.print(" Player win!!!\n");//        
        return true;
    }
    
    boolean checkPat()
    {
        if(validMovesList.isEmpty()){
            //print();
            //System.out.println("Pat!! game over\n");
            return true;
        }
        //if()//if its repeat himself
        return false;
    }

    void allValidMoves()
    {
        updateCol();
        validMovesList.clear();
        for(Piece p : col?piecesB:piecesW)
            for(int i=1;i<9;i++)
                for(int j=1;j<9;j++){
                    if(validMove(p, new Pos(i,j)))
                        validMovesList.add(new Move(p.getPos(),new Pos(i,j)));}
    }
    
    public void change(String newPawn) {
        updateCol();
        for(Piece p : col?piecesW:piecesB)
            if(p.getPos().posX == moves.get(0).getTarget().posX && p.getPos().posY == moves.get(0).getTarget().posY && moves.get(0).getChange()!='P')
                {value += p.become(what(newPawn.charAt(0))); break;}
    }

}
