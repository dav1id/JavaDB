import DatabaseExceptions.InvalidStatementException;
import java.io.IOException;
import java.util.Scanner;

import Format.Table;

enum Statement{
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

    DELETE_STATEMENT{
        public String toString(){
            return "delete";
        }
    };

    public abstract String toString();
}

public class Main {
    public static void main(String[] args) throws Exception {
        inputBuffer<Integer, String> input_buffer = new inputBuffer<>(0, 0);

        Scanner inputScanner = null;
        Table table = null;

        try {
            inputScanner = new Scanner(System.in);

            /* Creating table, and checking if the table is found (for now, we will pre-set the name to Table 1).
               If it is found then we will deserialize the table, if it's not we will have to create the pages.
               Checks if the table is found by checking if there's a text file called meta, if it returns an exception
               then the table does not exist.
            */

            table = new Table("Table1");
            if (table.getTableFile()){
                Pager.deserialize_pages(table);
            } else {
                if (!table.createTable())
                    throw new IOException();

                table.createMetaText(); // Meta-text for possibly creating unique tables in the future
                Pager.serialize_pages(table); // Saving the pages into memory after creating them is necessary for code function later
            }

            String c;
            while (true){
                c = inputScanner.nextLine();

                if (c.equals(".exit")) {
                    break;
                }

                try {
                    Statement statementSelect = Tokenizer.prepareStatement(c);
                    Parser.executeStatement(table, statementSelect, c);
                    input_buffer.setBufferInput(c);

                } catch (InvalidStatementException e){
                    System.out.printf("Invalid Statement: %s\n", e.getMessage());
                }
            }

            System.out.printf("Exited out of database\n");
        } finally {
            if (inputScanner != null)
                inputScanner.close();
            if (table != null)
                Pager.serialize_pages(table);
        }
    }
}