import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

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

    public void removeLambdaProductions(){
        // Have a nullableVariables array
        // Add a Variable to the array when it has one of two productions
        // 1. A production that is just a lambda
        // 2. A production that is a combination of variables and lambda
        // Continue to loop through the productions until the nullableVariables array stops growing
        // Then loop through the productions again and remove any productions that contain a nullable variable
        
        ArrayList<Character> nullableVariables = new ArrayList<>();
        for(int i = 0; i < variables.length; i++){
            String[] currentProductions = productions.get(variables[i]);
            for(int j = 0; j < currentProductions.length; j++){
                if(currentProductions[j].equals("?")){
                    nullableVariables.add(variables[i]);
                    break;
                }
                int count = 0;
                for(int k = 0; k < currentProductions[j].length(); k++){
                    if (currentProductions[j].charAt(k) == '?'){
                        count++;
                    }
                    if (nullableVariables.contains(currentProductions[j].charAt(k))){
                        count++;
                
                    }
                }
                if (count == currentProductions[j].length()){
                    nullableVariables.add(variables[i]);
                    break;
                }
            }
        }

        System.out.println(nullableVariables);

        
        // Remove all the lambda productions
        for(int i = 0; i < variables.length; i++){
            String[] currentProductions = productions.get(variables[i]);
            ArrayList <String> temp = new ArrayList<>();
            for(int j = 0; j < currentProductions.length; j++){
                System.out.println("Current Production: " + currentProductions[j]);
                if(!currentProductions[j].equals("?")){
                    temp.add(currentProductions[j]);
                }
            }
            String [] tempArray = convertArrayListToArray(temp);
            productions.remove(variables[i]);
            productions.put(variables[i], tempArray);

        }

        // Replace nullable variables in the productions
        // Go through each production
        for(int i = 0; i < variables.length; i++){
            String[] currentProductions = productions.get(variables[i]);
            ArrayList<String> newProductions = new ArrayList<>();
            for(int j = 0; j < currentProductions.length; j++){
                ArrayList<Character> nullableVariablesInProduction = new ArrayList<>();
                for(int k = 0; k < currentProductions[j].length(); k++){
                    if(nullableVariables.contains(currentProductions[j].charAt(k))){
                        nullableVariablesInProduction.add(currentProductions[j].charAt(k));
                    }
                }
                // Create a combination of all the nullable variables in the production
                char[][] combinations = new char[(int)Math.pow(2, nullableVariablesInProduction.size())][nullableVariablesInProduction.size()];
                
                char[] tempNulls = convertArrayListToCharArray(nullableVariablesInProduction);
                combinations = generateCombinationsAsArray(tempNulls);


                System.out.println("Combinations: ");
                for (int k = 0; k < combinations.length; k++) {
                    for (int l = 0; l < combinations[k].length; l++) {
                        System.out.print(combinations[k][l] + " ");
                    }
                    System.out.println();
                }

                for (char[] combination : combinations) {
                    StringBuilder newProduction = new StringBuilder(currentProductions[j]);
                    for (char nullableVar : combination) {
                        int index = newProduction.indexOf(String.valueOf(nullableVar));
                        if (index != -1) {
                            newProduction.deleteCharAt(index);
                            newProductions.add(newProduction.toString());
                        } else {
                            newProductions.add(currentProductions[j]);
                        }
                    }
                    
                }
                
               
                
            }
            String [] newProductionsArray = convertArrayListToArray(newProductions);
            productions.remove(variables[i]);
            productions.put(variables[i], newProductionsArray);
        }
        // In Each production, we will iterate through the production and make a list of all the nullable variables in that specific production
        // We will create a list with all the possible combinatinos of the nullable variables
        // For each of the combinations, we will create a new production that is the same as the original production but with the nullable variables removed
        // We will add the new production to the list of productions

        
    }   

    public char[][] generateCombinationsAsArray(char[] input) {
        List<char[]> combinations = new ArrayList<>();
        int n = input.length;
    
        // Iterate through all non-empty combinations represented by 1 to (2^n - 1)
        for (int i = 1; i < (1 << n); i++) {
            List<Character> combination = new ArrayList<>();
    
            for (int j = 0; j < n; j++) {
                // Check if the j-th bit is set in i
                if ((i & (1 << j)) != 0) {
                    combination.add(input[j]);
                }
            }
    
            // Convert List<Character> to char[]
            char[] combinationArray = new char[combination.size()];
            for (int k = 0; k < combination.size(); k++) {
                combinationArray[k] = combination.get(k);
            }
    
            combinations.add(combinationArray);
        }
    
        // Convert List<char[]> to char[][]
        return combinations.toArray(new char[0][]);
    }
    

    public void removeUnitProductions(){
        // TODO: Implement this method
    }

    public void removeUselessSymbols(){
        // TODO: Implement this method
    }

    public void turnIntoChomskyNormalForm(){
        // TODO: Implement this method
    }

    public void turnIntoGreibachForm(){
        // TODO: Implement this method
    }

    public static char[] convertArrayListToCharArray (ArrayList<Character> list){
        char[] array = new char[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
    }

    public static String[] convertArrayListToArray (ArrayList<String> list){
        String[] array = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
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

}
