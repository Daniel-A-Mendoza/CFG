import java.util.Scanner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class RunGrammar{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String variables = getVariables(input);
        String terminals = getTerminals(input);
        char[] variableList = stringToCharList(variables);
        char[] terminalList = stringToCharList(terminals);
        char startSymbol = validateStartSymbol(input, variableList);
        makeStartFirst(variableList, startSymbol);
        System.out.println(Arrays.toString(variableList));
        HashMap<Character, String[]> productions = new HashMap<>();
        addProductions(input, productions, variableList, terminalList);
        CFG grammar1 = new CFG(startSymbol, variableList, terminalList, productions);
        System.out.println(grammar1);
        input.close();
    }

    public static void addProductions(Scanner input, HashMap<Character, String[]> productions, char[] variableList, char[] terminalList){
        System.out.println("You will now enter the productions.");
        System.out.println("Enter your productions by providing spaces between \" | \".");
        for(int i = 0; i < variableList.length; i++){
            System.out.println("Enter the Productions for " + variableList[i] + ":");
            System.out.print(variableList[i] + " --> ");
            String currentProductions = input.nextLine();
            productions.put(variableList[i], identifyAllProductions(currentProductions));
        }
    }

    public static String[] identifyAllProductions(String productions){
        Scanner splitProductions = new Scanner(productions);
        ArrayList<String> newResult = new ArrayList<String>();
        while(splitProductions.hasNext()){
            String currentPart = splitProductions.next();
            if (currentPart.equals("|")){
                continue;
            }
            else{
                newResult.add(currentPart);
            }
        }
        splitProductions.close();
        return convertArrayListToArray(newResult);
    }

    public static String[] convertArrayListToArray (ArrayList<String> list){
        String[] array = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }
        return array;
    }

    public static int findNumberOfProductions(String productions){
        int numberOfSymbols = 0;
        Scanner findMultipleProductions = new Scanner(productions);
        while (findMultipleProductions.hasNext()){
            String currentPart = findMultipleProductions.next();
            if(currentPart.equals("|")){
                numberOfSymbols += 1;
            }
        }
        findMultipleProductions.close();
        return numberOfSymbols+1;

    }

    public static void makeStartFirst(char[] list, char start){
        char temp = list[0];
        for(int i = 0 ; i < list.length; i++){
            if(list[i] == start){
                list[0] = list[i];
                list[i] = temp;
                return;
            }
        }
    }

    public static String getVariables(Scanner input){
        System.out.print("Enter your CFG's Variables: ");
        String userInput = input.nextLine();
        return userInput;
    }

    public static String getTerminals(Scanner input){
        System.out.print("Enter your CFG's Terminals: ");
        String userInput = input.nextLine();
        return userInput;
    }


    public static char[] stringToCharList(String input){
        String removedSpace = removeWhitespace(input);
        return removedSpace.toCharArray();
    }

    public static String removeWhitespace(String input){
        StringBuilder removedSpaces = new StringBuilder(); 
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if (!Character.isWhitespace(c)){
                removedSpaces.append(c);
            }
        }
        String newInput = removedSpaces.toString();
        return newInput;

    }

    public static char validateStartSymbol(Scanner input, char[] charList){
        boolean flag = true;
        char[] startSymbol = new char[1];
        do{
            System.out.print("Enter your CFG's Start Variable: ");
            String userInput = input.nextLine();
            String newString = removeWhitespace(userInput);
            char c = newString.charAt(0);
            if(inList(c, charList)){
                flag = false;
                startSymbol[0] = c;
            }
            else{
                System.out.println("Start variable is not in the Variables");
            }
        }while(flag);
        return startSymbol[0];
        
    }

    public static boolean inList (char a, char[] list){
        for(int i = 0; i < list.length; i++){
            if (a == list[i]){
                return true;
            }
        }
        return false;
    }


}
