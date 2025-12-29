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
    public Integer id;
    public Integer page;

    public Row(Integer id, String username, String email) throws IOException {
        this.username = username;
        this.email = email;
        this.id = id;

        page = (int) floor(id / 10);

        if (page > Table.TABLE_SIZE)
            throw new IOException(); // create a unique exception here too
    }
}

class Page implements Serializable {
    private final HashMap<Integer, Row> rows;// 10 rows

    public Page(){
        rows = new HashMap<>();
    }

    public Row getRowContents(Integer id){
        return rows.get(id);
    }

    public void setRowContents(Row row){
        rows.put(row.id, row);
    }
}

class Table {
    static public final Integer TABLE_SIZE = 3;// Number of pages
    static public final Integer ROW_SIZE = 3; // id, username, and the email
    static public final Integer PAGE_SIZE = 10; // 10 rows

    private final ArrayList<Page> pagesTable;
    public String tableName;

    public Table(String tableName){
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
        } finally{
            if (fileReader != null) fileReader.close();
        }
        return true;
    }

    public boolean createPages() throws IOException {
        for (int i = 0; i < TABLE_SIZE; ++i)
            pagesTable.add(new Page());

        return new File(tableName).mkdir();
    }
}

class vm {
    static void execute_insert(Row row, Table table){
        Integer pageForRow = row.page;
        ArrayList<Page> pagesTable = table.getTable();
        Page page = pagesTable.get(pageForRow);

        page.setRowContents(row);
    }

    static Row execute_select(Integer id, Table table) throws Exception{ // Assuming that
        Integer pageForRow = (int) floor(id/10);
        ArrayList<Page> pagesTable = table.getTable();

        if (pageForRow > table.TABLE_SIZE)
            throw new Exception();

        Page page = pagesTable.get(pageForRow);
        return page.getRowContents(id);
    }
}






