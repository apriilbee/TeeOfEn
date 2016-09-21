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
        Expression ub = new Expression(upperbound);
        System.out.println(eq);
        
    }
    
    public void print(){
        System.out.println("Upperbound: " + upperbound); 
        System.out.println("Lowerbound:" + lowerbound);
        System.out.println("Iterator:" + iterator);
        System.out.println("Units:" + units);
        System.out.println("Constant:" + constant);
    }

   

}
