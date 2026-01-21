/*
            - Need to create a unique exception for invalid commands

            Interface -> SQL Command Processor -> Virtual Machine (Be where the data is going to be stored -> Simulating cloud)
                          |
                          -> Tokenizer -> Parser -> Code Generator -> Back to SQL Command Processor

                            1. Tokenizer - Tokenizer is going to use another switch to go through the list of items and
                            then return a statement that is eventually going to be executed

                            2. Parser - Will take the statement, and then execute it
*/

import java.io.IOException;
import java.util.Scanner;

enum Statement{ // Can soon implement methods into statement maybe
    INSERT_STATEMENT {
        public String toString(){
            return "insert";
        }
    },

    SELECT_STATEMENT {
        public String toString(){
            return "select";
        }
    },

    FAILURE_STATEMENT {
        public String toString(){
            return "Statement tokenization has failed";
        }
    };

    void printStatement(){
        System.out.println(this);
    }
    public abstract String toString();

}

public class Main {
    public static void main(String[] args) throws Exception {
        inputBuffer<Integer, String> input_buffer = new inputBuffer<>(0, 0);

        Scanner inputScanner = null;
        Table table = null;

        try {
            // Generating scanner for user input, commands that are translated to statements
            inputScanner = new Scanner(System.in);

            /* Creating table, and checking if the table is found (for now, we will pre-set the name to Table 1).
               If it is found then we will deserialize the table, if it's not we will have to create the pages.
               Checks if the table is found by checking if theres a text file called meta, if it returns an exception
               then the table does not exist.
            */

            table = new Table("Table1");
            if (table.getTableFile()){
                Pager.deserialize_pages(table);
            } else {
                if (!table.createTable())
                    throw new IOException();

                table.createMetaText(); // Need to create meta text here, and can specify late
                Pager.serialize_pages(table); // Can call serialize_pages just to save the pages onto the table
            }

            String c;
            while (true){
                c = inputScanner.nextLine();

                if (c.equals(".exit"))
                    break;

                try {
                    Statement statementSelect = Tokenizer.prepareStatement(c); // can create custom exception based on statement
                    Parser.executeStatement(table, statementSelect, c);
                    input_buffer.setBufferInput(c);

                } catch (InvalidStatementException e){
                    System.out.println(String.format("Invalid Statement: %s", c));
                }
            }

            System.out.println(String.format("Exited out of database"));
        } finally {
            if (inputScanner != null)
                inputScanner.close();
            if (table != null)
                Pager.serialize_pages(table);
        }
    }
}