/*
            - Need to create a unique exception for invalid commands

            Interface -> SQL Command Processor -> Virtual Machine (Be where the data is going to be stored -> Simulating cloud)
                          |
                          -> Tokenizer -> Parser -> Code Generator -> Back to SQL Command Processor

                            1. Tokenizer - Tokenizer is going to use another switch to go through the list of items and
                            then return a statement that is eventually going to be executed

                            2. Parser - Will take the statement, and then execute it
*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.InputMismatchException;
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
        inputBuffer<Integer, String> input_buffer = new inputBuffer<>(
            0, 0, new ArrayDeque<>()
        );
        Scanner inputScanner = null;

        // Creating the table referenece
        Table table = null;

        try {
            // Generating scanner for user input, commands that are translated to statements
            inputScanner = new Scanner(System.in);
            String c = inputScanner.nextLine();

            /* Creating table, and checking if the table is found (for now, we will pre-set the name to Table 1).
               If it is found then we will deserialize the table, if it's not we will have to create the pages.
               Checks if the table is found by checking if theres a text file called meta, if it returns an exception
               then the table does not exist.
            */

            table = new Table("Table1");
            if (table.getTableFile()){
                vm.deserialize_pages(table);
            } else {
                table.createPages();
                if (!new File(table.tableName).mkdir()) throw new IOException(); // Creating the file here

                vm.serialize_pages(table); // Can call serialize_pages just to save the pages onto the table
            }

            while (!c.equals(".exit")){
                c = inputScanner.nextLine();
                Statement statementSelect = Tokenizer.prepareStatement(c); // can create custom exception based on statement
                Parser.executeStatement(statementSelect);

                input_buffer.setBufferInput(c);
            }
            System.out.println("Exited out of database");
        } finally {
            if (inputScanner != null)
                inputScanner.close();
            if (table != null)
                vm.serialize_pages(table);
        }
    }
}