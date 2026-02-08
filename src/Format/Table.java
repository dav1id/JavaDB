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

    public void createMetaText() throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(tableName + "/meta.txt");
            fileWriter.write(tableName);

        } finally {
            if (fileWriter != null) fileWriter.close();
        }
    }

    public void deleteTable(){
        for (Page page : pagesTable)
            page.deleteAllContents();
        System.out.println("Deleted all contents in this table");
    }
}