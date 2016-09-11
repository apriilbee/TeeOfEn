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
        int count = 0;
        for (String line : Block) {
            StringTokenizer s = new StringTokenizer(line, "(;) ");
            ArrayList token_array = new ArrayList();
            while (s.hasMoreElements()) {
                token_array.add(s.nextElement());
            }
            if (token_array.contains("for")) {
                calculateBounds(token_array, line, Block);
            } else {
                count += calculateUnitTime(token_array, line);
            }
            //System.out.println("End of line!");
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
     */
    private static void calculateBounds(ArrayList token_array, String line, ArrayList<String> Block) {
        token_array.removeAll(new ArrayList(Arrays.asList("for", "int", "{")));
        
        for (int i = 0; i < token_array.size(); i++) {
            System.out.print(token_array.get(i) + "; ");
            patternMatcher((String) token_array.get(i));
        }
        System.out.println("\n-------------");

        String tmp_ubvariable = getValue(token_array.get(condition));
        String tmp_lbvariable = getValue(token_array.get(initialization));
        
        // Swaps upperbound and lowerbound depending if the for loop starts at n and decrements 
        boolean reverse = false;
        if(tmp_lbvariable.matches("^[a-zA-Z]*$")){
            String tmp = tmp_lbvariable;
            tmp_lbvariable = tmp_ubvariable;
            tmp_ubvariable = tmp;
            reverse = true;
        }
        
        String upperbound = getUpperBound(token_array.get(increment), tmp_ubvariable); 
        Integer lowerbound = Integer.valueOf(tmp_lbvariable) + ConditionOperator(token_array.get(condition),reverse); 
        
        //can also alter upperbound
        System.out.println("Upperbound: " + upperbound); 
        // change String.valueOf(lowerbound) to tmp_lbvariable if no need to add or subtract 1.
        System.out.println("Lowerbound: " + getVariable(token_array.get(initialization)) + String.valueOf(lowerbound));
        System.out.println("Iterator: " + token_array.get(2));
        
        System.out.println("\n\n");
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
//        System.out.println(operator);
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
            return "log " + var + " base " +  chunks.get(2);
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
    private static int calculateUnitTime(ArrayList token_array, String line) {
        String regex = "+"; //add regex for all operators here
        for (Object tmp : token_array) {
            //System.out.println("Token:" + tmp);
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(regex);
//            
            int count = 0;
//            while (matcher.find())
//                count++;
        }
        //System.out.println("");
        return 0;
    }
    
}