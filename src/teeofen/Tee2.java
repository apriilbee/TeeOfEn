/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teeofen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author testuser
 */
public class Tee2 {

    public static void main(String[] args) throws FileNotFoundException {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter file name:");
//        ProcessBlocks(sc.nextLine());
        ProcessBlocks("file.in.txt");
    }

    static Stack blockChecker = new Stack();
    static ArrayList<String> Block = new ArrayList();
    static ArrayList<Summation> Summation = new ArrayList();

    /**
     * Groups the file into several "for" blocks
     * @param file
     * @throws FileNotFoundException 
     */
    private static void ProcessBlocks(String file) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(file));
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.contains("for") && line.contains("{")) {

                /*this if statement checks if there are lines outside the loop ("for" block) that needs
                to be calculated separately */
                if (!Block.isEmpty() && !isPartOfLoop(Block)) {
                    calculateBlock(Block);
                    Block = new ArrayList();
                }
                blockChecker.push("{");
                Block.add(line);
            } else if (line.contains("}")) {
                blockChecker.pop();
                Block.add(line);
                if (blockChecker.empty()) {
                    //checkNestedLoop
                    calculateBlock(Block);
                    Block = new ArrayList();
                }
            } else if (!line.equals("")) {
                Block.add(line);
            }
        }
    }

    /**
     * Calculates running time per block (for loop)
     * @param Block 
     */
    private static void calculateBlock(ArrayList<String> Block) {
        Summation blockSummation = new Summation();
        int count = 0;
        for (String line : Block) {
            StringTokenizer s = new StringTokenizer(line, "(;) ");
            StringBuilder sb = new StringBuilder();
            
            if(line.contains("for")){
                for(int i=0; i<line.length()-1;i++){
                    if(line.charAt(i) == ' '){
                        if(String.valueOf(line.charAt(i-1)).matches("[+-/*=<>]") || String.valueOf(line.charAt(i+1)).matches("[+-/*=<>]")){}
                        else
                            sb.append(line.charAt(i));
                            
                    }
                    else{
                        sb.append(line.charAt(i));
                    }
                }
                s = new StringTokenizer(sb.toString(), "(;) ");
                System.out.println("Loop: " + sb);

            }
            
            ArrayList token_array = new ArrayList();
            while (s.hasMoreElements()) {
                Object tmp = s.nextElement();
                token_array.add(tmp);
            }
            if (token_array.contains("for")) {
                ArrayList<String> block = calculateBounds(token_array, sb.toString(), Block);
                count+= calculateUnitsInFor(token_array);
                blockSummation.setUpperbound(block.get(0));
                blockSummation.setLowerbound(block.get(1));
                blockSummation.setIterator(block.get(2));
                blockSummation.addConstant(block.get(3));
            } else {
                if(!line.equals("")){
                    count += calculateUnitTime(token_array, line);
                }
            }
           // System.out.println("End of line!");
        }
        blockSummation.addUnits(count);
        Summation.add(blockSummation);
        if(!blockSummation.getUpperbound().equals("")){
            blockSummation.print();
            blockSummation.calculateRunningTime();
            System.out.println("");
        }
        //System.out.println("End of block!\n");
    }

    /**
     * Checks if the specific line in file is part of a loop. Those that are 
     * not part of the loop are to be treated differently.
     * @param Block
     * @return 
     */
    private static boolean isPartOfLoop(ArrayList<String> Block) {
        for (int i = 0; i < Block.size(); i++) {
            if (Block.get(i).startsWith("for")) {
                return true;
            }
        }
        return false;
    }

    static int initialization = 0; 
    static int condition = 1;
    static int increment = 2; 
     
    
    /**
     * This function is for lines containing a for loop. It calculates the upperbound, lowerbound,
     * and increment/decrement of the loop.
     * 
     * returns an arraylist of upperbound (0), lowerbound (1), iterator (2)
     */
    private static ArrayList calculateBounds(ArrayList token_array, String line, ArrayList<String> Block) {
        ArrayList summation = new ArrayList();
        token_array.removeAll(new ArrayList(Arrays.asList("for", "int", "{")));
        
//        for(int i=0; i<token_array.size();i++){
//            System.out.println(token_array.get(i));
//        }

        String tmp_ubvariable = getValue(token_array.get(condition));
        String tmp_lbvariable = getValue(token_array.get(initialization));
        String iter = getValue(token_array.get(increment));
        // Swaps upperbound and lowerbound depending if the for loop starts at n and decrements 
        boolean reverse = false;
        if(tmp_lbvariable.matches("[a-zA-Z]") || tmp_lbvariable.matches("([-+]?[a-zA-Z]*\\.?[a-zA-Z]+[\\/\\+\\-\\*])+([-+]?[0-9]*\\.?[0-9]+)")){
            String tmp = tmp_lbvariable;
            tmp_lbvariable = tmp_ubvariable;
            tmp_ubvariable = tmp;
            reverse = true;
        }
        
        if(tmp_lbvariable.matches("[0-9]") && iter!=""){
            if(Integer.valueOf(tmp_lbvariable) > 1){
                //System.out.println("increment" + token_array.get(increment));
                if(String.valueOf(token_array.get(increment)).contains("+=")){
                    //iterator - assignment to make iterator divisible by assignment (WORST CASE)
                    int var = Integer.valueOf(iter) - Integer.valueOf(tmp_lbvariable);
                    tmp_lbvariable= String.valueOf(var);
                    if(var>0)
                        tmp_ubvariable += "+" + var;
                }
                else if(String.valueOf(token_array.get(increment)).contains("*=")){
                 //   System.out.println("*");
                }


            }
        }
        
        String upperbound = getUpperBound(token_array.get(increment), tmp_ubvariable); 
        String lowerbound;
        
        Integer x = ConditionOperator(token_array.get(condition),reverse);
        if(x > 0 && !upperbound.contains("log"))
            lowerbound = String.valueOf(Integer.valueOf(tmp_lbvariable) + x);
        else
            lowerbound = tmp_lbvariable;
        
        summation.add(upperbound);
        summation.add(String.valueOf(lowerbound));
        summation.add(token_array.get(2));
        summation.add(String.valueOf(calculateConstant(token_array)));
        
        return summation;
    }
    
    /**
     * Adds/Subtracts value to lowerbound depending on the condition operator
     * of the for loop. Reverse means loop starts at n and decrements.  
     * 
     * @param tmp
     * @param reverse
     * @return 
     */
    private static Integer ConditionOperator(Object tmp, boolean reverse){
        ArrayList<String> t = patternMatcher((String) tmp);
        String operator =  t.get(1);
        if(operator.equals("<")){
            if (reverse)
                return -1;
            return 1;
        }
        else if (operator.equals(">")){
            if (reverse)
                return 1;
            return -1;
        }
        
        return 0;
    }
    
    /**
     * @param tmp
     * @return operators after the operand
     */
    private static String getValue(Object tmp){
        ArrayList chunks = patternMatcher((String) tmp);
        List t = chunks.subList(2, chunks.size());
        String ub = "";
        for(int i=0; i<t.size(); i++){
            ub += t.get(i);
        }
        
        String[] up = ub.split(",");
        return up[0];
    }
    
    /**
     * 
     * @param tmp
     * @return operators before operand 
     */
    private static String getVariable(Object tmp){
        ArrayList chunks = patternMatcher((String) tmp);
        List t = chunks.subList(0,2);
        String var = "";
        for(int i=0; i<t.size(); i++){
            var += t.get(i);
        }
        
        return var;
    }
    
    
    
    /**
     * Gets the upperbound of the for loop by checking the increment/decrement that leads to termination of loop. 
     * @param tmp
     * @param var
     * @return 
     */
    private static String getUpperBound(Object tmp, String var) {
        ArrayList chunks = patternMatcher((String) tmp);

        if(chunks.contains("++") || chunks.contains("--")){
            return var;
        }
        
        else if(chunks.contains("+=") || chunks.contains("-=")){
            return var + "/" + chunks.get(2);
        }
        else {
            return "log" + chunks.get(2) + " " + var;
        }
        
    }
    
    /** 
     *   Returns an arraylist of the operator and operands. 
     *   Example input: i+=2 
     *  Output: [i,+=,2]
     */
    private static ArrayList patternMatcher(String line){
        Pattern pattern = Pattern.compile("[A-Z]+|[a-z]+|[0-9]+|[-+*/=]+|[<>=]+|[%,]");
        Matcher matcher = pattern.matcher(line);
        ArrayList chunks = new ArrayList();
        while (matcher.find()) {
           //System.out.println(matcher.group(0));
           chunks.add(matcher.group(0));
        }
        return chunks;
    }  


    /**
     * Counts the number of operations in a specific line. 
     * @param token_array
     * @param line
     * @return 
     */
    private static int calculateUnitTime(ArrayList<String> token_array, String line) {
        //System.out.println(line);
        
        /* First pattern matcher checks operations in each line. However, it sees negative 
        sign as an operation. The 2nd pattern matcher removes it from the count. */
        
        Pattern pattern = Pattern.compile("[-+*/=]+|[<>=]+|[%]");
        Matcher matcher = pattern.matcher(line);
     
        int count = 0;
        while (matcher.find())
              count++;
        

        Pattern p = Pattern.compile("= -[0-9]{0,10}");
        Matcher match = p.matcher(line);
        while (match.find())
            count--;
        
       
        return count;
    }

    /**
     * Counts the units that are not part of loop 
     * @param line 
     */
    private static int calculateConstant(ArrayList token_array) {
        int count = 0;
        for(int i = 0; i<token_array.size()-1; i++){
            String tmp =  (String) token_array.get(i);
            count += calculateUnitTime(token_array, tmp);
        }
        return count;
    }
    
    private static int calculateUnitsInFor(ArrayList token_array) {
        int count = 0;
        for(int i = 1; i<token_array.size(); i++){
            String tmp =  (String) token_array.get(i);
            count += calculateUnitTime(token_array, tmp);
        }
        return count;
    }
}