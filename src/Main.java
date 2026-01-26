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

    FAILURE_STATEMENT {
        public String toString(){
            return "Statement tokenization has failed";
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

                table.createMetaText(); // Need to create meta text here, and can specify late
                Pager.serialize_pages(table); // Can call serialize_pages just to save the pages onto the table
            }

            String c;
            while (true){
                c = inputScanner.nextLine();

                if (c.equals(".exit")) {
             //       Pager.serialize_pages(table);
                    break;
                }

                try {
                    Statement statementSelect = Tokenizer.prepareStatement(c);
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