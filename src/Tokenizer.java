import DatabaseExceptions.InvalidStatementException;
import Format.Row;

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

        throw new InvalidStatementException("Unknown identifier");
     }

     public static Row prepareRowInsertStatement(String token) throws InvalidStatementException {
         String[] splitList = token.split(" ");
         Row row = null;

         try {
             if (splitList.length > 4)
                 throw new ArrayIndexOutOfBoundsException();

             row = new Row(Integer.valueOf(splitList[1]), splitList[2], splitList[3]);
         } catch (ArrayIndexOutOfBoundsException | NumberFormatException e){
             throw new InvalidStatementException("Cannot parse into a row");
         }

         return row;
     }

}
