/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author Drago
 */
public abstract class Piece {
    
    private final boolean color;
    protected char identifier;
    
    public Piece(boolean setColor)
    {
        color = setColor;
    }
    
    public boolean getColor()
    {
        return color;
    }
    
    public char getIdentifier()
    {
        return identifier;
    }
}