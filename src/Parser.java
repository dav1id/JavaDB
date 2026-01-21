import DatabaseExceptions.InvalidStatementException;

class Parser {
     static void executeStatement(Table table, Statement statement, String token) throws InvalidStatementException {
        switch(statement){
            case INSERT_STATEMENT:
                Row row = Tokenizer.prepareRowInsertStatement(token);
                vm.executeInsert(row, table);
                break;

            case SELECT_STATEMENT:
             //   vm.execute_select(row, table);
                break;
        }
    }
}
