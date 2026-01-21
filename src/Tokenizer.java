import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 Loops through all available statements, and checks to see if the statement you have aligns with any of them
 @returns Statement identifier

 **/

class Tokenizer {
     public static Statement prepareStatement(String token) throws InvalidStatementException {
         String[] splitList = token.split(" ");
         String removedLexeme = splitList[0].substring(1);

         char[] charList = new char[splitList[0].length()];
         splitList[0].getChars(0, splitList[0].length(), charList, 0);

        if (charList[0] == '.')
             for (Statement identifier : Statement.values()) {
                 if (removedLexeme.equals(identifier.toString())) {
                     return identifier;
                 }
             }

        throw new InvalidStatementException();
     }

     public static Row prepareRowInsertStatement(String token) throws InvalidStatementException {
         String[] splitList = token.split(" ");
         Row row = null;

         try {
             if (splitList.length > 4)
                 throw new ArrayIndexOutOfBoundsException();

             row = new Row(Integer.valueOf(splitList[1]), splitList[2], splitList[3]);
         } catch (ArrayIndexOutOfBoundsException | IOException | NumberFormatException e){
             throw new InvalidStatementException();
         }

         return row;
     }
}
