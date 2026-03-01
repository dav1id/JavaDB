import DatabaseExceptions.InvalidStatementException;
import Format.Page;
import Format.Row;
import Format.Table;

import java.io.Console;
import java.util.ArrayList;

public class DeleteRow {
    /**
     Verifies the user is trying to delete contetns of the database by email or username. Sets the value of username to be null,
     and the value of email to be a value, if the user tries to delete by email, and vice-versa.

     Calls the row comparison to compare rows having the same username, email, or id. Returns a string array list of deleted rows.

     @param string User input as a string... ".delete 1 joe joedoe@gmail.com
     @param table Collection of Pages
     **/

    public ArrayList<String> deleteByString(String string, Table table) throws InvalidStatementException {
        boolean isEmail = false;
        char[] listOfCharacters = new char[string.length()];
        string.getChars(0, string.length(), listOfCharacters, 0);

        ArrayList<Page> pagesTable = table.getTable();
        ArrayList<String> rowsAsStrings = new ArrayList<>();

        Row row = new Row(null, null, null);

        for (char c : listOfCharacters) {
            if (c == '@')
                isEmail = true;
        }

        String printStatement;
        if (isEmail){
            row.setEmail(string);
            printStatement = "Are you sure you want to delete rows with email (Y/N): ";

        } else {
            row.setUsername(string);
            printStatement = "Are you sure you want to delete rows with username (Y/N): ";

        }

        Console console = System.console();
        String response = console.readLine(printStatement);

        if (response.equals("Y")){
            ArrayList<Row> rowList = new ArrayList<>();
            rowList = Row.rowComparison(row, rowList, pagesTable);

            Page page;
            Integer pageForRow;
            if (!(rowList.isEmpty()))
                for (Row tempRow : rowList){
                    pageForRow = Row.pageForRow(tempRow.id);
                    page = pagesTable.get(pageForRow);

                    rowsAsStrings.add(tempRow.toString());
                    page.deleteRowById(tempRow.id);
                }

                return rowsAsStrings;
        }else {
            return null;
        }
    }


    /** 
        Splits the delete command into two different methods depending on if it's deleting by id, or by username or email.

        Uses a try and catch NumberFormatException block to see if delete command is deleting by id, username or email. If by id,
        calls the method deleteRowById() in page. If by username or email, calls deleteByString() method. Also includes a way to delete
        the entire table with the command 'delete *'.
    
    @param String Token 
    @param Table table
    @returns ArrayList<String>
    **/
    public ArrayList<String> deleteRow(String token, Table table) throws InvalidStatementException {
        String[] contents = token.split(" ");
        ArrayList<String> deletedRows = new ArrayList<>();

        ArrayList<Page> pagesTable = table.getTable();
        Page page;

        try {
            Integer id = Integer.parseInt(contents[1]);
            page = pagesTable.get(Row.pageForRow(id));

            String rowToString = page.deleteRowById(id);
            deletedRows.add(rowToString);

        } catch (NumberFormatException e){
            if (contents[1].equals("*")){
                Console console = System.console();
                String input = console.readLine("Are you sure you want to delete this database? ");

                if (input.equals("Y"))
                    table.deleteTable();
                return null; // might look into separating main into another function, so I can call that function to restart the whole process again
            }

            deletedRows = deleteByString(contents[1], table);
        }
        return deletedRows;
    }
}
