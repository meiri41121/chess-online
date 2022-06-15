package com.chess.model;

public class Movemant extends Move {

	private boolean eat;
    private boolean hazraha;
    private boolean firstMove;
    private char change;

    Movemant(Pos s, Pos t, boolean e, Boolean h, Boolean fm) {
		super(s, t);
		eat=e; 
		hazraha=h; 
		firstMove=fm; 
		change='P'; 
	}
    // Movemant(Movemant m)
    // {
    //     super(m.getSource(),m.getTarget());
    //     eat=m.eat; 
	// 	hazraha=m.hazraha; 
	// 	firstMove=m.firstMove; 
	// 	change=m.change; 
    // }
//  movemant(pos s, pos t):source(s),target(t),eat(false),hazraha(false),firstMove(false),change(false){}
//    pos getSource(){return source;}
//    pos getTarget(){return target;}
    boolean getEat(){return eat;}
    public Boolean getHazraha(){return hazraha;}
    boolean getFM(){return firstMove;}
    public char getChange(){return change;}
    void setChange(char c){change=c;}

}