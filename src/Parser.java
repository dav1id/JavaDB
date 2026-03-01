import DatabaseExceptions.InvalidStatementException;

import Format.Table;
import Format.Row;


class Parser {

    /**
        Second step in processing user input. Receives the statement as input from PrepareStatement, and calls the correct
        methods depending on statement selection.

        @param table Collection of Pages
        @param statement Enum of the different statements (select, delete, insert)
        @param token User input
     **/
     static void executeStatement(Table table, Statement statement, String token) throws InvalidStatementException {
        switch(statement){
            case INSERT_STATEMENT:
                Row row = Tokenizer.prepareRowInsertStatement(token);
                vm.executeInsert(row, table);
                break;

            case SELECT_STATEMENT:
                vm.executeSelect(token, table);
                break;
            case DELETE_STATEMENT:
                vm.executeDelete(token, table);
                break;

        }
    }
}
