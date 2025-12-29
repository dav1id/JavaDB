import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 Loops through all available statements, and checks to see if the statement you have aligns with any of them
 @returns Statement identifier

 **/

class Tokenizer {

    private static String[] createRegex(String token){
        String[] splitList = token.split(" ");
        String lexeme = splitList[0].substring(1);
        splitList[0] = lexeme;

        return splitList;

    }
     public static Statement prepareStatement(String token) {
         String[] splitList = createRegex(token);

         for (Statement identifier : Statement.values()) {
             if (splitList[0].equals(identifier.toString())) {
                 return identifier;
             }
         }

         return Statement.FAILURE_STATEMENT;
     }

     public static Row prepareRowInsertStatement(String token) {
         String[] splitList = createRegex(token);
         Row row = null;

         try {
             if (splitList.length > 4)
                 throw new ArrayIndexOutOfBoundsException();

             row = new Row(Integer.valueOf(splitList[1]), splitList[2], splitList[3]);
         } catch (ArrayIndexOutOfBoundsException | IOException e){
             System.out.println(e.getMessage());
         }

         return row;
     }
}
