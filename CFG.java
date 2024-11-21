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
        // Flag to track if any changes were made in the current iteration
        boolean changed;

        // Continue iterating until no more changes are needed
        do {
            changed = false;
            // Iterate over each variable in the grammar
            // Get the current productions for the variable
            // Create a new list to store the updated productions
            for(char variable : variables) {
                    String[] currentProductions = (String[]) productions.get(variable);
                    ArrayList<String> newProductions = new ArrayList<>();

                    // Iterate over each production of the variable
                    // Check if the production is a unit production (i.e., it has only one character and that character is a variable)
                    // if it's a unit production, replace it with non-unit productions
                    for(String production : currentProductions) {
                        if (production.length() == 1 && isVariable(production.charAt(0))) {
                            char unitVariable = production.charAt(0);
                            String[] unitProductions = (String[]) productions.get(unitVariable);

                            // Iterate over each production of the unit variable
                            // Ignore the empty production (?)
                            // Add the non-unit production to the new list
                            for (String unitProduction : unitProductions) {
                                if (! unitProduction.equals("?") && unitProduction.length() > 1) {
                                    newProductions.add(unitProduction);
                                    changed = true;
                                }
                            }
                        } else {
                            // If it's not a unit production, add it to the new list as is
                            newProductions.add(production);
                        }
                    }
                    // Convert the new list back to an array
                    // Update the productions for the variable
                    String[] newProductionsArray = convertArrayListToArray(newProductions);
                    productions.remove(variable);
                    productions.put(variable, newProductionsArray);
            }
        } while (changed); // Continue until no more changes are needed
    }

    public void removeUselessSymbols(){
        // Find all reachable variables
        ArrayList<Character> reachableVariables = new ArrayList<>();
        reachableVariables.add(startSymbol);
        Boolean changed;
        do {
            changed = false;
            for (char variable : variables) {
                if (!reachableVariables.contains(variable)) {
                    String[] currentProductions = (String[]) productions.get(variable);
                    for (String productions : currentProductions) {
                        boolean allReachable = true;
                        for (char c : productions.toCharArray()) {
                            if (isVariable(c) &&! reachableVariables.contains(c)) {
                                allReachable = false;       break;
                            }
                        }
                        if (allReachable) {
                            reachableVariables.add(variable);
                            changed = true;     break;
                        }
                    }
                }
            }
        } while (changed);

        // Find all Productive Values
        ArrayList<Character> productiveVariables = new ArrayList<>();
        do {
            changed = false;
            for(char variable : variables) {
                if(!productiveVariables.contains(variable)) {
                    String[] currentProductions = (String[]) productions.get(variable);
                    for (String production : currentProductions) {
                        boolean allProductive = true;
                        for (char c : production.toCharArray()) {
                            if (isVariable(c) &&! productiveVariables.contains(c)) {
                                allProductive = false;      break;
                            } else if (isTerminal(c) &&! productiveVariables.contains(variable)) {
                                allProductive = false;      break;
                            }
                        }
                        if (allProductive) {
                            productiveVariables.add(variable);
                            changed = true;     break;
                        }
                    }
                }
            } 
        } while (changed);

        //Remove useless variables and their productions
        for (char variable : variables) {
            if (!reachableVariables.contains(variables) ||! productiveVariables.contains(variable)) {
                productions.remove(variable);    
            }
        }

        // Update variable array
        char[] newVariables = convertArrayListToCharArray(reachableVariables);
        variables = newVariables;
    }


    public void turnIntoChomskyNormalForm(){
        // TODO: Implement this method
        // 1. Remove Lambda and unit productions
        removeLambdaProductions();
        removeUnitProductions();

        // 2. Replace mixed strings with non-terminals
        HashMap<String, Character> newVariables = new HashMap<>();
        int newVariablesIndex = 0;
        for (char variable : variables) {
            String[] currentProductions = (String[]) productions.get(variable);
            ArrayList<String> newProductions = new ArrayList<>();

            for (String production : currentProductions) {
                if (production.length() > 1) {
                    for (int i = 0; i < production.length() - 1; i++) {
                        char c1 = production.charAt(i);
                        char c2 = production.charAt(i + 1);
                        if (isVariable(c1) && isTerminal(c2) || isTerminal(c1) && isVariable(c2)) {
                            // Replace mixed Strings
                            String key = String.valueOf(c1) + c2;
                            if (!newVariables.containsKey(key)) {
                                newVariables.put(key, (char) ('A' + newVariablesIndex));
                                newVariablesIndex++;
                            }
                            char newVariable = newVariables.get(key);
                            String newProduction = production.substring(0, i) + newVariable + production.substring(i + 2);
                            newProductions.add(newProduction);
                        } else {
                            newProductions.add(production);
                        }
                    }
                } else {
                    newProductions.add(production);
                }
            }
            String[] newProductionsArray = convertArrayListToArray(newProductions);
            productions.remove(variable);
            productions.put(variable, newProductionsArray);
        }

        // 3. Shorten Strings of length greater than 2
        for (char variable : variables) {
            String[] currentProductions = (String[]) productions.get(variable);
            ArrayList<String> newProductions = new ArrayList<>();
            for (String production : currentProductions) {
                if (production.length() > 2) {
                    char[] chars = production.toCharArray();
                    char[] temp = new char[chars.length - 1];
                    System.arraycopy(chars, 0, temp, 0, chars.length - 1);
                    String tempStr = new String(temp);
                    char newVariable = (char) ('A' + newVariablesIndex);
                    newVariablesIndex++;
                    newProductions.add(String.valueOf(chars[0]) + newVariable);
                    productions.put(newVariable, new String[] {tempStr});
                } else {
                    newProductions.add(production);
                }
            }
            String[] newProductionsArray = convertArrayListToArray(newProductions);
            productions.remove(variable);
            productions.put(variable, newProductionsArray);
        }
        // Update variables array
        char[] newVariablesArray = convertHashMapKeysToCharArray(newVariables);
        char[] combinedVariables = combineArrays(variables, newVariablesArray);
        variables = combinedVariables;
    }

    private char[] combineArrays(char[] arr1, char[] arr2) {
        char[] result = new char[arr1.length + arr2.length];
        System.arraycopy(arr2, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    private char[] convertHashMapKeysToCharArray(HashMap<String, Character> map) {
        char[] array = new char[map.size()];
        int i = 0;
        for (Character value : map.values()) {
            array[i++] = value;
        }
        return array;
    }

    public void turnIntoGreibachForm() {
        // TODO: Implement this method
        // 1. Remove lambda and unit productions.
        removeLambdaProductions();
        removeUnitProductions();

        // 2. convert to CNF
        turnIntoChomskyNormalForm();

        // 3. Left-Recursion elimination and conversion to GNF
        for (char variable : variables) {
            String[] currentProductions = (String[]) productions.get(variable);
            ArrayList<String> newProductions = new ArrayList<>();
            for (String production : currentProductions) {
                if (production.length() > 1) {
                    char firstChar = production.charAt(0);
                }
            }
        }
    }

    private boolean isVariable(char c) {
        // Iterate over each variable in the grammar
        // If The character is a variable, TRUE
        // If The character is not a variable, FALSE
        for (char variable : variables) {
            if (c == variable) {
                return true;
            }
        }
        return false;
    }

    private boolean isTerminal(char c) {
        // Iterate over each terminal for the grammar
        // If The character is a terminal, TRUE
        // If The character is not a terminal, FALSE
        for (char terminal : terminals) {
            if (c == terminal) {
                return true;
            }
        }
        return false;
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
