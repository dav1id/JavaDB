import DatabaseExceptions.InvalidStatementException;
import Format.Page;
import Format.Row;
import Format.Table;

import java.util.ArrayList;
import java.util.Scanner;

public class delete {
    /*
        Delete can delete a row by id, or can delete multiple rows by their name or email.
        Deleting by name or email is going to ask the user for verification on if they really want to remove the
        row

    */
    public String responseInput(){
        Scanner inputScanner = null;

        try {
            inputScanner = new Scanner(System.in);

            String c = "";
            while (inputScanner.hasNext()){
                c = inputScanner.next();

                if (c.equals("Y") ||  c.equals("N"))
                    return c;
            }

            System.out.println("Invalid Statement: Type either Y or N");
            return responseInput();

        } finally{
            if (inputScanner != null) inputScanner.close();
        }
    }

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

        if (isEmail){
            row.setEmail(string);
        } else {
            row.setEmail(string);
        }

        ArrayList<Row> rowList = new ArrayList<>();
        rowList = Row.rowComparison(row, rowList, pagesTable);

        Page page;
        Integer pageForRow = 0;
        for (Row tempRow : rowList){
            pageForRow = Row.pageForRow(tempRow.id);
            page = pagesTable.get(pageForRow);

            rowsAsStrings.add(tempRow.toString());
            page.deleteRowById(tempRow.id);
        }

        return rowsAsStrings;
    }

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
            Scanner inputScanner = null;
            Boolean isEmail = false;

            if (contents[1].equals("*")){
                System.out.println("Are you sure you want to delete this database?");
                String input = responseInput();

                if (input.equals("Y"))
                    table.deleteTable();
                return null; // might look into seperating main into another function, so I can call that function to restart the whole process again
            }

            deletedRows = deleteByString(contents[1], table);
        }

        return deletedRows;
    }
}
