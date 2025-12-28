/**
 Loops through all available statements, and checks to see if the statement you have aligns with any of them
 @returns Statement identifier

 **/

// Need to seperate statement id user email
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
}
