package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Math.floor;

public class Table implements Serializable {
    static public final Integer TABLE_SIZE = 3;// Number of pages
    static public final Integer ROW_SIZE = 3; // id, username, and the email
    static public final Integer PAGE_SIZE = 10; // 10 rows

    private final ArrayList<Page> pagesTable;
    public String tableName;

    public Table(String tableName) {
        this.tableName = tableName;
        pagesTable = new ArrayList<>();
    }

    /*
        We recieve a string, token, that includes the select 50

        Need to seperate this into a String list, and if the number of contents in the string list is greater than 2,
        then we know that this is a specific select, if it's less we know it's a broad select. Broad select means only
        one parameter -> Have to check if the one parameter is an integer or a string. If it's a string then
        we need to check if it's an email (includes @, using a for loop), or if it's a username (includes no @).

        Need to select the page if it's an id, or loop through all pages if it's a string. We are going to print out the rows

    */

    public ArrayList<Row> getRowSingleSelect(String[] contents, Table table) throws InvalidStatementException {
        ArrayList<Page> pagesTable = table.getTable();
        ArrayList<Row> rowList = new ArrayList<>();

        try{
            Integer id = Integer.parseInt(contents[1]);
            Integer pageForRow = (int) floor((double) id / 10);

            Page page = pagesTable.get(pageForRow);
            rowList.add(page.getRowContents(id));

        } catch(NumberFormatException e) {
            char[] listOfCharacters = new char[contents[1].length()];
            contents[1].getChars(0, contents[1].length(), listOfCharacters, 0);
            Boolean containsEmail = false;

            for (char c : listOfCharacters) {
                if (c == '@')
                    containsEmail = true;
            }

            Page page = pagesTable.getFirst();

            for (int i = 0; i < Table.TABLE_SIZE; ++i) {
                page = pagesTable.get(i);

                for (int j = 0; j < Table.PAGE_SIZE; ++j) {
                    Row row = page.getRowContents(j);

                    if (containsEmail)
                        if (row.getEmail().equals(contents[1]))
                            rowList.add(row);
                        else if (row.getUser().equals(contents[1]))
                            rowList.add(row);
                }
            }
        } finally{
            return rowList;
        }
    }

    public ArrayList<Row> getRowMultiSelect(String[] contents, Table table) throws InvalidStatementException{
        ArrayList<Row> rowList = new ArrayList<>();
        return rowList;
    }

    public void getRowContents(String token, Table table) throws InvalidStatementException {
        Row row = new Row(null, null, null);
        String[] splitList = token.split(" ");
        ArrayList<Row> rowList;


        if (splitList.length > 2){
            rowList = getRowSingleSelect(splitList, table);
            System.out.println(rowList);

        } else {
            rowList = getRowMultiSelect(splitList, table);
        }

        System.out.println(rowList);
    }

    public void createMetaText() throws IOException {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(tableName + "/meta.txt");
            fileWriter.write(tableName);

        } finally {
            if (fileWriter != null) fileWriter.close();
        }
    }

    public ArrayList<Page> getTable(){
        return pagesTable;
    }

    public boolean getTableFile() throws IOException {
        FileReader fileReader = null;

        try{
            fileReader = new FileReader(tableName + "/meta.txt");
            // Eventually can add some meta contents for more complex and unique tables. But for now only acts to check if file found
        } catch (FileNotFoundException e){
            return false;
        } finally {
            if (fileReader != null) fileReader.close();
        }
        return true;
    }

     /**
     Makes the directory along with the number of pages set by the table
     **/

    public boolean createTable() {
        for (int i = 0; i < TABLE_SIZE; ++i)
            pagesTable.add(new Page());

        return new File(tableName).mkdir();
    }
}
