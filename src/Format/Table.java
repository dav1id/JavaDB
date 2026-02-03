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

    /*
        We recieve a string, token, that includes the select 50

        Need to seperate this into a String list, and if the number of contents in the string list is greater than 2,
        then we know that this is a specific select, if it's less we know it's a broad select. Broad select means only
        one parameter -> Have to check if the one parameter is an integer or a string. If it's a string then
        we need to check if it's an email (includes @, using a for loop), or if it's a username (includes no @).

        Need to select the page if it's an id, or loop through all pages if it's a string. We are going to print out the rows
    */


    public ArrayList<Row> rowComparison(Row row, ArrayList<Row> rowList) throws InvalidStatementException{
        Page page;
        for (int i = 0; i < Table.TABLE_SIZE; ++i) {
            page = pagesTable.get(i);

            for (int j = 0; j < Table.PAGE_SIZE; ++j) {
                Row row2 = page.getRowContents(j);

                if (row.compareTo(row2) == 0)
                    rowList.add(row2);
            }
        }

        return rowList;
    };
    public ArrayList<Row> getRowSingleSelect(String[] contents, Table table) throws InvalidStatementException {
        ArrayList<Page> pagesTable = table.getTable();
        ArrayList<Row> rowList = new ArrayList<>();

        Row row = new Row(null, null, null);

        try {
            Integer id = Integer.parseInt(contents[1]);
            int pageForRow = (int) floor((double) id / 10);

            Page page = pagesTable.get(pageForRow);
            rowList.add(page.getRowContents(id));

        } catch(NumberFormatException e) {
            char[] listOfCharacters = new char[contents[1].length()];
            contents[1].getChars(0, contents[1].length(), listOfCharacters, 0);
            Boolean containsEmail = false;

            for (char c : listOfCharacters) {
                if (c == '@')
                    row.setEmail(contents[1]); // .select template1o@shaw.ca
            }
            row.setUsername(contents[2]); // .select template2o@shaw.ca
            rowList = rowComparison(row, rowList);
        }
        return rowList;
    }

    /*
        First need to get the contents of the string, and then use my own unique comparator to compare the contents.
        So if I got .select david david1o@shaw.ca. I need to compare all rows and then select the row depending on
        the contents of the comparator.

        .select david david1o@shaw.ca
        .select 40 david1o@shaw.ca
    */

    public ArrayList<Row> getRowMultiSelect(String[] contents, Table table) throws InvalidStatementException{
        Row row = new Row(null, null, null);
        ArrayList<Row> rowList = new ArrayList<>();
        Page page;

        ArrayList<Page> pagesTable = table.getTable();

        try { // .select 40 david1o@shaw.ca. Will just return the value with the id
            Integer id = Integer.parseInt(contents[1]);
            Integer pageForRow = (int) floor((double) id / 10);

            page = pagesTable.get(pageForRow);
            rowList.add(page.getRowContents(id));

        } catch(NumberFormatException e){ //name always comes before email
            row.setUsername(contents[1]);
            row.setEmail(contents[2]);
            rowList = rowComparison(row, rowList);

        }
        return rowList;
    }


    public ArrayList<Row> getRowContents(String token, Table table) throws InvalidStatementException {
        String[] splitList = token.split(" ");
        ArrayList<Row> rowList;

        if (splitList.length == 2){ // .select 40, or .select david, or .select david1o@shaw.ca
            rowList = getRowSingleSelect(splitList, table);

        } else {
            rowList = getRowMultiSelect(splitList, table);
        }

        return rowList;
    }

    public void createMetaText() throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(tableName + "/meta.txt");
            fileWriter.write(tableName);

        } finally {
            if (fileWriter != null) fileWriter.close();
        }
    }
}