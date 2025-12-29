import java.io.IOException;
import java.util.Scanner;

/**
 Loops through all available statements, and checks to see if the statement you have aligns with any of them
 @returns Statement identifier

 **/

class Tokenizer {
     static Statement prepareStatement(String token) {
         String lexeme = token.substring(0);

         for (Statement identifier : Statement.values()) {
             if (lexeme.equals(identifier.toString())) {
                 return identifier;
             }
         }

         return Statement.FAILURE_STATEMENT;
     }

     static Row prepareRowStatement(String token) throws IOException {
         String lexeme = token.substring(0);
         Scanner scanner = null;

         try {
              scanner = new Scanner(lexeme);
              scanner.useDelimiter(" ");

              Integer id = -1; String username = ""; String email = "";
              Row row = new Row(id, username, email);

              for (int i = 0; i < 3; ++i)
                  if (i == 1) {
                      id = Integer.valueOf(scanner.next());
                  } else if (i == 2){
                      username = scanner.next();
                  } else {
                      email = scanner.next();
                  }
              return row;

         } finally{
             if (scanner != null) scanner.close();
         }
     }
}
