/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teeofen;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author testuser
 */
public class Summation {
    private String upperbound = "";
    private String lowerbound = ""; 
    private String iterator = "";
    private Integer units = 0; 
    private Integer constant = 0;

    /**
     * @return the upperbound
     */
    public String getUpperbound() {
        return upperbound;
    }

    /**
     * @param upperbound the upperbound to set
     */
    public void setUpperbound(String upperbound) {
        this.upperbound = upperbound;
    }

    /**
     * @return the lowerbound
     */
    public String getLowerbound() {
        return lowerbound;
    }

    /**
     * @param lowerbound the lowerbound to set
     */
    public void setLowerbound(String lowerbound) {
        this.lowerbound = lowerbound;
    }

    /**
     * @return the iterator
     */
    public String getIterator() {
        return iterator;
    }

    /**
     * @param iterator the iterator to set
     */
    public void setIterator(String iterator) {
        this.iterator = iterator;
    }

    /**
     * @return the units
     */
    public Integer getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(Integer units) {
        this.units = units;
    }
    
     /**
     * @return the constant
     */
    public Integer getConstant() {
        return constant;
    }

    /**
     * @param constant the constant to set
     */
    public void setConstant(String constant) {
        
        this.constant = Integer.valueOf(constant);
    }
    
    public void addUnits(int count){
        units += count;
    }
    
    public void addConstant(String count){
        constant += Integer.valueOf(count);
    }
    
    public void calculateRunningTime(){
        String eq = units + "*" + "(" + "(" + upperbound + ")" + "-" + lowerbound + "+" + "1" + ")" + "+"+ constant;
        System.out.println(eq);
        
        Expression ub = new Expression(upperbound);
        Expression lb = new Expression(lowerbound);
        Expression con = new Expression(String.valueOf(constant));
        Expression un = new Expression(String.valueOf(units));
        Expression one = new Expression("1");
        
        Expression x = new Expression();
        // units(upperbound - lowerbound + 1) + constant
        System.out.print("T(n) = ");
       x.addExpressions(con, x.multiplyExpressions(un, x.addExpressions(x.subtractExpressions(ub, lb),one))).display();
//        Expression t1 = new Expression("n");
//        Expression t2 = new Expression("2n");
//        x.multiplyExpressions(t1, t2).display();
    }
    
    public void print(){
        System.out.println("Upperbound: " + upperbound); 
        System.out.println("Lowerbound:" + lowerbound);
        System.out.println("Iterator:" + iterator);
        System.out.println("Units:" + units);
        System.out.println("Constant:" + constant);
    }

   

}
