import java.util.HashMap;
import java.util.ArrayList;
public class CFG {
    private char startSymbol;
    private char[] variables;
    private char[] terminals;
    private HashMap<Character, String[]> productions;

    public CFG(char startSymbol, char[] variables, char[] terminals, HashMap<Character, String[]> productions){
        this.startSymbol = startSymbol;
        this.variables = variables;
        this.terminals = terminals;
        this.productions = productions;
    }

    public String toString(){
        String result = "";
        result += "Grammar Composition: \n";
        result += ("Variables: ");
        for(int i = 0; i < variables.length; i++){
            result += variables[i];
            if(i == variables.length-1){
                continue;
            }
            result += ", ";
        }
    

        result += "\nTerminals: ";
        for(int i = 0; i < terminals.length; i++){
            result += terminals[i];
            if(i == terminals.length-1){
                continue;
            }
            result += ", ";
        }

        result += "\nStart Symbol: " + startSymbol;
        result += "\nProductions: \n";

        for(int i = 0; i < variables.length; i++){
           result += (variables[i] + " --> " );
           String[] temp = productions.get(variables[i]);
           for(int j = 0; j < temp.length; j++){
                result += temp[j];
                if(j == temp.length - 1){
                    continue;
                }
                else{
                    result += " | ";
                }
           }
           result += "\n";
        }

        return result;

    }

    public static void removeLambda(){
        
    }
}
