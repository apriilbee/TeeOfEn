/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teeofen;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author testuser
 */
public class Expression {
    ArrayList<Object> terms;
    
    Expression(){
        terms = new ArrayList();
    }
    
    Expression(String exp){
        terms = new ArrayList();
        if(isNumeric(exp)){
            terms.add(exp);
        }
        else{
            if(!exp.contains("^")){
                terms.add(exp);
                terms.add("0");
            }
            else {
               //in case term is raised to power,
            }
           
        }
    }
    
    public void display(){
        ArrayList tmp = (ArrayList) terms.clone();
        //Collections.reverse(tmp);
        for(int i=0; i<tmp.size(); i++){
            System.out.print(tmp.get(i) + " ");
        }
        System.out.println("");
    }
    
    
    public Expression addExpressions(Expression e1, Expression e2){
        // n + n, 2n+n, n+2n, 3n+3n, 2+2, n+1/3 + n, n/3 + n
        ArrayList tmp = new ArrayList();
        if (e1.terms.size() > e2.terms.size())
            tmp = (ArrayList) e1.terms.clone();
        else
            tmp = (ArrayList) e2.terms.clone();
        
        Collections.reverse(tmp);
        
        ArrayList e1_tmp = (ArrayList) e1.terms.clone();
        Collections.reverse(e1_tmp);
        
        ArrayList e2_tmp = (ArrayList) e2.terms.clone();
        Collections.reverse(e2_tmp);
        
        for(int i=0; i<e1_tmp.size() && i<e2_tmp.size(); i++){
            Object op1 =  e1_tmp.get(i);
            Object op2 = e2_tmp.get(i);
            if(!isNumeric(String.valueOf(op1)) && !isNumeric(String.valueOf(op2))){
                String con1 = String.valueOf(op1).replaceAll("[^0-9.]", "");
                String con2 = String.valueOf(op2).replaceAll("[^0-9.]", "");
                String t = "";

                //n+n
                if(con1.isEmpty()&&con2.isEmpty()){
                    t = tmp.get(i) + "2";
                }
                
                //2n+2n
                else if (!con1.isEmpty() && !con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    Integer x = Integer.valueOf(con1) + Integer.valueOf(con2);
                    t = s + String.valueOf(x);
                }
                
                //2n+n
                else if (!con1.isEmpty() && con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    t = s + String.valueOf(Integer.valueOf(con1)+1);
                }
                
                //n+2n
                else if (con1.isEmpty() && !con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    t = s + String.valueOf(Integer.valueOf(con2)+1);
                }

                StringBuilder s = new StringBuilder(t);
                t = s.reverse().toString();
                tmp.set(i, t);
                
            }
            else if (isNumeric(String.valueOf(op1)) && isNumeric(String.valueOf(op2))){
                tmp.set(i, Integer.valueOf(String.valueOf(op1)) + Integer.valueOf(String.valueOf(op2)));
            }
        }
        Collections.reverse(tmp);
        //terms = (ArrayList<Object>) tmp.clone();
        
        Expression ret = new Expression();
        ret.terms = (ArrayList<Object>) tmp.clone();
        return ret;
    }
    
       public Expression subtractExpressions(Expression e1, Expression e2){
        // n + n, 2n+n, n+2n, 3n+3n, 2+2, n+1/3 + n, n/3 + n
        ArrayList tmp = new ArrayList();
        if (e1.terms.size() > e2.terms.size())
            tmp = (ArrayList) e1.terms.clone();
        else
            tmp = (ArrayList) e2.terms.clone();
        
        Collections.reverse(tmp);
        
        ArrayList e1_tmp = (ArrayList) e1.terms.clone();
        Collections.reverse(e1_tmp);
        
        ArrayList e2_tmp = (ArrayList) e2.terms.clone();
        Collections.reverse(e2_tmp);
        
        for(int i=0; i<e1_tmp.size() && i<e2_tmp.size(); i++){
            Object op1 =  e1_tmp.get(i);
            Object op2 = e2_tmp.get(i);
            
            if(!isNumeric((String) op1) && !isNumeric((String) op2)){
                String con1 = String.valueOf(op1).replaceAll("[^0-9.]", "");
                String con2 = String.valueOf(op2).replaceAll("[^0-9.]", "");
                String t = "";

                //n-n
                if(con1.isEmpty()&&con2.isEmpty()){
                    t = "0";
                }
                
                //2n-2n, 3n-1n, 
                else if (!con1.isEmpty() && !con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    Integer x = Integer.valueOf(con1) - Integer.valueOf(con2);
                    if(x==0)
                        t = "0";
                    else 
                        t = s + String.valueOf(x);
                }
                
                //2n-n
                else if (!con1.isEmpty() && con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    t = s + String.valueOf(Integer.valueOf(con1)-1);
                }
                
                //n-2n
                else if (con1.isEmpty() && !con2.isEmpty()){
                    String s = String.valueOf(tmp.get(i)).replaceAll("[^a-zA-Z.]", "");
                    t = s + String.valueOf(Integer.valueOf(con2)-1);
                }

                StringBuilder s = new StringBuilder(t);
                t = s.reverse().toString();
                tmp.set(i, t);
                
            }
            else if (isNumeric((String) op1) && isNumeric((String) op2)){
                tmp.set(i, Integer.valueOf((String) op1) - Integer.valueOf((String) op2));
            }
        }
        Collections.reverse(tmp);
        //terms = (ArrayList<Object>) tmp.clone();
        
        Expression ret = new Expression();
        ret.terms = (ArrayList<Object>) tmp.clone();
        return ret;
    }
    
    public static boolean isNumeric(String str)  {  
        try  {  
            double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  {  
          return false;  
        }  
        return true;  
    }
}
