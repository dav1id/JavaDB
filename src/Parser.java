class Parser {
     static void executeStatement(Statement statement){
        switch(statement){
            case INSERT_STATEMENT:
                System.out.println("Insert statement goes here");
                break;

            case SELECT_STATEMENT:
                System.out.println("Select statement goes here");
                break;
        }
    };
}
