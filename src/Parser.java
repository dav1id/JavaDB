import DatabaseExceptions.InvalidStatementException;

import Format.Table;
import Format.Row;


class Parser {
     static void executeStatement(Table table, Statement statement, String token) throws InvalidStatementException {
        switch(statement){
            case INSERT_STATEMENT:
                Row row = Tokenizer.prepareRowInsertStatement(token);
                vm.executeInsert(row, table);
                break;

            case SELECT_STATEMENT:
                Row selectedRow = vm.executeSelect(token, table);
                System.out.println(selectedRow);
                break;
        }
    }
}
