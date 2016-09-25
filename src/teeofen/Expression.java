/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teeofen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

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
            if(!exp.contains("^") && !exp.contains("log") && !exp.contains("+") && !exp.contains("-")){
                terms.add(exp);
                terms.add("0");
            }
            else if(exp.contains("+")){
                ArrayList a = new ArrayList();
                StringTokenizer s = new StringTokenizer(exp, "+");
                while(s.hasMoreElements()){
                   a.add(s.nextElement());
                }
                for(int i=0;i<a.size();i++){
                   terms.add(a.get(i));
                } 
                
                if(exp.contains("/")){
                    String denom = exp.substring(exp.indexOf("/")+1,exp.length());
                    for(int i=0; i<terms.size()-1; i++){
                       String tmp =   String.valueOf(terms.get(i)).concat("/" + denom);
                       terms.set(i, tmp);
                    }
                }
            }
            else if(exp.contains("-")){
                ArrayList a = new ArrayList();
                StringTokenizer s = new StringTokenizer(exp, "-");
                while(s.hasMoreElements()){
                   a.add(s.nextElement());
                }
                for(int i=0;i<a.size();i++){
                   terms.add(a.get(i));
                } 
            }
            else {
                ArrayList a = new ArrayList();
                StringTokenizer s = new StringTokenizer(exp, "+");
                while(s.hasMoreElements()){
                   a.add(s.nextElement());
                }
                for(int i=0;i<a.size();i++){
                   terms.add(a.get(i));
                }
                if(a.size()==1){
                    terms.add("0");
                }
            }
           
        }
    }
    
    public void display(){
        ArrayList tmp = (ArrayList) terms.clone();
        ArrayList tmp2 = new ArrayList();
        Collections.reverse(tmp);
        for(int i=0; i<tmp.size(); i++){
            if(isNumeric(String.valueOf(tmp.get(i)))){
                if(Integer.valueOf(String.valueOf(tmp.get(i))) > 0 && tmp.size()>1){
                   // System.out.print("+" + tmp.get(i));
                   tmp2.add("+" + tmp.get(i));
                }
                else if (Integer.valueOf(String.valueOf(tmp.get(i))) == 0 ) {
                    
                }
                else{
                    tmp2.add(tmp.get(i) + " ");
                    //System.out.print(tmp.get(i) + " ");
                }
            }
            else{
                if (String.valueOf(tmp.get(i)).matches("^[0][a-zA-Z]")){
                    
                }
                else 
                    if(i > 1){
                       // System.out.print(tmp.get(i) + "^" + i + " ");
                        System.out.println(tmp.get(i));
                       tmp2.add(tmp.get(i) + "^" + i + " ");
                    }
                    else{
                        tmp2.add(tmp.get(i) + " ");
                       // System.out.print(tmp.get(i) + " ");
                    }
            }
        }
        Collections.reverse(tmp2);
        for(int i=0; i<tmp2.size(); i++){
            System.out.print(tmp2.get(i));
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
       
    public Expression multiplyExpressions(Expression e1, Expression e2){
        Expression ret = new Expression();
        //check sa if reverse or dili ang both exps i-print lungs laters
        // n*n -> move both possition + 1 or -1 (pos 1+1 -> 2 so n should be at pos 2 para n^2)
        /// n*6 -> concatenate nlungs -> 6n  (pos 1+0 -> 1 so 6n should be at pos 1)
        /// 4*6 -> multiply lungs (both pos 0, so pos 0 rapd ila product)
        // 2n*3n -> 6n, multiply ang constants kilid nila, pos 1 + pos 1 -> pos 2
        // n/3 * 2 -> 2n/3, check if 2 is divisible by 3 
        
        ArrayList prod = new ArrayList();
        ArrayList re1 = (ArrayList) e1.terms.clone();
        ArrayList re2 = (ArrayList) e2.terms.clone();
        Collections.reverse(re1);
        Collections.reverse(re2);

        
        for(int i=0; i<re1.size(); i++){
            for(int j=0; j<re2.size(); j++){
                Object ans = "";
                int pos = i+j;
                Object op1 = re1.get(i);
                Object op2 = re2.get(j);
                // start of something
                if(isNumeric(String.valueOf(op1)) && isNumeric(String.valueOf(op2))){
                    Integer a = Integer.valueOf(String.valueOf(op1)) * Integer.valueOf(String.valueOf(op2));
                    ans = (Object)a;
                }
                else {
                    if(!isNumeric(String.valueOf(op1)) && isNumeric(String.valueOf(op2))){
                        String con = String.valueOf(op1).replaceAll("[^0-9.]", "");
                        if(con.isEmpty()){
                           String var = String.valueOf(op2);
                           var = var.concat(String.valueOf(op1));
                           ans = (Object)var;
                        }
                        else{
                          if(!String.valueOf(op1).contains("log")){
                            String s = String.valueOf(op1).replaceAll("[^a-zA-Z.]","");    
                            Integer tmp_i = Integer.valueOf(String.valueOf(op2)) * Integer.valueOf(con);
                            String var = String.valueOf(tmp_i);
                            var = var.concat(s);
                            ans = (Object)var;
                          }
                          else{
                           
                                String var = String.valueOf(op2) + " ";
                                var = var.concat(String.valueOf(op1));
                                ans = (Object)var;
                            
                          }
                        }
                    }
                    else if(isNumeric(String.valueOf(op1)) && !isNumeric(String.valueOf(op2))){
                        String con = String.valueOf(op2).replaceAll("[^0-9.]", "");
                        //3 * n
                        if(con.isEmpty()){
                           String var = String.valueOf(op1);
                           var = var.concat(String.valueOf(op2));
                           ans = (Object)var;
                        }
                        // 3 * 6n
                        else{
                          if(!String.valueOf(op2).contains("log") && !String.valueOf(op2).contains("/")){
                            String s = String.valueOf(op2).replaceAll("[^a-zA-Z.]","");    
                            Integer tmp_i = Integer.valueOf(String.valueOf(op1)) * Integer.valueOf(con);
                            String var = String.valueOf(tmp_i);
                            var = var.concat(s);
                            ans = (Object)var;
                          }
                          else if(String.valueOf(op2).contains("/")){
                            //handle gcf and stuff here
                            //6 * n/3 AND 6*(n+1)/3 AND     6*(n+1)/4 -> mao ni i-gcf
                            String var = "";
                            String numerator = String.valueOf(op2).substring(0,String.valueOf(op2).indexOf("/"));
                            String denom = String.valueOf(op2).substring(String.valueOf(op2).indexOf("/")+1,String.valueOf(op2).length());
                            Integer num = Integer.valueOf(String.valueOf(op1));
                            Integer denominator = Integer.valueOf(denom);
                            
                            //if variable ra
                            if(numerator.matches("[a-zA-Z]")){
                                //6 n/3
                                if(num%denominator == 0){
                                    var = String.valueOf(num/denominator) +  String.valueOf(op2).replaceAll("[^a-zA-Z.]","");
                                    ans = (Object) var;
                                }
                                //5 n/3
                                else{
                                    var = num + String.valueOf(op2).replaceAll("[^a-zA-Z.]","") + "/" + denom;
                                    ans = (Object) var;
                                }
                            }
                            else if(numerator.matches("[0-9]")){
                                Integer mult = Integer.valueOf(num) * Integer.valueOf(numerator);
                                if(mult%denominator!=0)
                                    ans = (Object) mult + "/" + denom;
                                else
                                    ans = mult/denominator;
                                System.out.println(ans);
                                
                            }
                            //else if naay coefficient
                            else{
                              
                            }
                            
                          }
                          else{
                            String var = String.valueOf(op1) + " ";
                            var = var.concat(String.valueOf(op2));
                            ans = (Object)var;
                          }
                        }
                    }
                    else if(!isNumeric(String.valueOf(op1)) && !isNumeric(String.valueOf(op2))){
                        String con1 = String.valueOf(op1).replaceAll("[^0-9.]", "");
                        String con2 = String.valueOf(op2).replaceAll("[^0-9.]", "");
                        String var = String.valueOf(op2).replaceAll("[^a-zA-Z.]","");
                        
                        //n*n
                        if(con1.isEmpty() && con2.isEmpty()){
                           ans = (Object)var;
                        }
                        
                        //n*3n
                        else if(con1.isEmpty() && !con2.isEmpty()){
                            ans = op2;
                        }
                        else if(!con1.isEmpty() && con2.isEmpty()){
                            ans = op1;
                        }
                        else{
                            if(String.valueOf(op2).contains("/")){
                                
                            }
                            else if(String.valueOf(op2).contains("/")){
                                
                            }
                        }
                          
                    }
                }
                
                //end of something
                try {
                    if(prod.size() >= pos){
                        try{
                            Object[] tmp = prod.toArray();
                            if(tmp[pos]!="0"){
                                Expression et = addExpressions(new Expression(String.valueOf(prod.get(i))), new Expression(String.valueOf(ans)));
                                Collections.reverse(et.terms);
                                ans = et.terms.get(pos);
                                prod.set(pos, ans);
                            }
                        } catch (Exception e) {
                            prod.add(pos, ans );
                        }
                    }
                } catch (Exception e){
                    for(int k=0; i<prod.size(); k++){
                        prod.add("0");
                    }
                    prod.set(pos, ans);
                }
            }
        }
        
        Collections.reverse(prod);
        ret.terms = prod; 
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

    private void distribute(Expression expression, Expression e2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

// multiplyExpressions /
// nested
// logarithmic  /
// power