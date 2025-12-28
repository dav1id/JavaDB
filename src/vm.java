/*
Serialization using objectoutputstream, objectinputstream

We can make a hash set as the pages in a table, and then make the id of the rows define what page they will be on

We can divide it into tables based on the # of rows, and then we can serialize the tables. Based on the id's that we want
to access, we can implement spatiality by only accessing the tables containing the id range (or if the id is close to the end,
then we will load two tables at once)

Rows class - has id, username, email
Table class - Contains a list of hashsets, each that is seriliazable -> Can make it implement HashSet and add
more methods to it possibly (for a page)

3 pages, 30 rows. 10 rows per page. Row id will go up to 30. Math.floor(row_id/10) to find the page that it will
belong to.

Each page is going to have an array list that will store the contents of the row. Can acccess the row to be selected
from a page, by deserializing the page, and then doing math.floor(row_id/10) - 1 to get an index from 0 to 9

Add row is going to add a row by modulus 3 (I will have 3 pages)
*/

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.floor;

class Row {
    public String username; public String email;
    public short id;

    public Row(String username, String email, short id){
        this.username = username;
        this.email = email;
        this.id = id;
    }
}

class Page implements Serializable { // Need a way to remove the index from the row, and then save it in my hashmap as index, (username, email)
    private final HashMap<Short, Row> rows;// 10 rows

    public Page(){
        rows = new HashMap<>();
    }

    public Row getRowContents(Row row){
        return rows.get(row.id);
    }

    public void setRowContents(Row row){
        rows.put(row.id, row);
    }
}

class Table {
    static public final Integer TABLE_SIZE = 3;// Number of pages
    static public final Integer ROW_SIZE = 3; // id, username, and the email
    static public final Integer PAGE_SIZE = 10; // 10 rows

    private final ArrayList<Page> table;
    public String tableName;

    public Table(String tableName){
        this.tableName = tableName;
        table = new ArrayList<>();
    }

    public ArrayList<Page> getTable(){
        return table;
    }

    public boolean getTableFile() throws IOException {
        FileReader fileReader = null;

        try{
            fileReader = new FileReader(tableName + "/meta.txt");
            // Eventually can add some meta contents for more complex and unique tables. But for now only acts to check if file found
        } catch (FileNotFoundException e){
            return false;
        } finally{
            if (fileReader != null) fileReader.close();
        }
        return true;
    }

    public void createPages() throws IOException {
        for (int i = 0; i < TABLE_SIZE; ++i)
            table.add(new Page());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(tableName + "/meta.txt");
        } finally {
            if (fileWriter != null) fileWriter.close();
        }
    }

}

class vm {
    /**
        Saves the row to a spcific page depending on the rows id. Each page has 10 rows, so math.float(id/10) will
        give us the page that we should store the row in. Serializing a row will be called on insert, and other methods
        that change the composition of the row
     **/

    static void serialize_row(Row row, Table table){
        short idxInTable = (short)(floor(row.id / 10));
        Page page = table.getTable().get(idxInTable);
        page.setRowContents(row);
    }

    static Row deserialize_row(Row row, Table table){
        short idxInTable = (short)(floor(row.id / 10));
        Page page = table.getTable().get(idxInTable);
        return page.getRowContents(row);
    }

    /**
        Serializes all the pages into the table at the end of the program execution
     **/

    static void serialize_pages(Table table) throws IOException {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objOutputStream = null;
        ArrayList<Page> arrayTable = table.getTable();

        for (int i = 0; i < arrayTable.size(); ++i){
            try {
                fileOutputStream = new FileOutputStream(table.tableName + "/" + i);
                objOutputStream = new ObjectOutputStream(fileOutputStream);

                Page page = arrayTable.get(i);
                objOutputStream.writeObject(page);

            } finally{
                if (fileOutputStream != null) fileOutputStream.close();
                if (objOutputStream != null) objOutputStream.close();
            }
        }
    }

    /**
     Deserializes the pages at the beginning of the program
     **/

    static void deserialize_pages(Table table) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objInputStream = null;
        ArrayList<Page> arrayTable = table.getTable();

        for (int i = 0; i < table.TABLE_SIZE; ++i){
            try {
                fileInputStream = new FileInputStream(table.tableName + "/" + i);
                objInputStream = new ObjectInputStream(fileInputStream);

                Page page = (Page) objInputStream.readObject();
                arrayTable.add(page);
            } finally {
                if (fileInputStream != null) fileInputStream.close();
                if (objInputStream != null) objInputStream.close();
            }
        }
    }



    /**
     Possible implementation of a way to serialize individual pages (maybe an auto-save feature paired with an
     internal clock, to see how long the program has been running for? But we can trust that the finally will
     always serialize the page in main
     **/

    static void serialize_page(ArrayList<Page> table){
        // Call serialize_page at the end of main try and call (in finally to close/destroy the table)
    }
}
