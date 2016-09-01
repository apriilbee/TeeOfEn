/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teeofen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author testuser
 */
public class Tee2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter file name:");
        ProcessBlocks(sc.nextLine());
    }
    
    static Stack blockChecker = new Stack();
    static ArrayList<String> Block = new ArrayList();
    
    private static void ProcessBlocks(String file) throws FileNotFoundException{
        Scanner in = new Scanner(new FileReader(file));
        while(in.hasNext()){
            String line = in.nextLine();
            if(line.contains("for") && line.contains("{")){
                
                /*this if statement checks if there are lines outside the loop ("for" block) that needs
                to be calculated separately */
                if(!Block.isEmpty() && !isPartOfLoop(Block)){
                    calculateBlock(Block);
                    Block = new ArrayList();
                }
                
                blockChecker.push("{");
                Block.add(line); 
            }
            else if(line.contains("}")){
                blockChecker.pop();
                Block.add(line);
                if(blockChecker.empty()){
                    calculateBlock(Block);
                    Block = new ArrayList();
                }
            }
            else {
                if(!line.equals(""))
                    Block.add(line);
            }
        }
    }

  
    private static void calculateBlock(ArrayList<String> Block) {
        for (String line : Block) {
            System.out.println(line);
            StringTokenizer s = new StringTokenizer(line,"(;) ");
            ArrayList token_array = new ArrayList();
            while(s.hasMoreElements()){
                token_array.add(s.nextElement());
            }
//            if(token_array.contains("for")){
//                System.out.println(line);
//            }
           
        }
         System.out.println("done");
            System.out.println("");
    }

    private static boolean isPartOfLoop(ArrayList<String> Block) {
        for(int i=0; i<Block.size(); i++){
            if(Block.get(i).startsWith("for")){
                System.out.println("LIIIINE: " + Block.get(i));
                return true;
            }
        }
        return false;
    }
}
