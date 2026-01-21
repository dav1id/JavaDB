import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import DatabaseExceptions.InvalidStatementException;

import static java.lang.Math.floor;

class Row implements Serializable {
    public String username;
    public String email;
    public Integer id;
    public Integer page;

    public Row(Integer id, String username, String email)  throws InvalidStatementException {
        this.username = username;
        this.email = email;
        this.id = id;

        page = (int) floor(id / 10);

        if (page > Table.TABLE_SIZE)
            throw new InvalidStatementException(); // create a unique exception here too
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

    public Table(String tableName) {
        this.tableName = tableName;
        pagesTable = new ArrayList<>();
    }

    /**
        Makes sure that the table was correctly created by creating meta text. Any table that does not have the text
        file meta will not be registered by the database. Also a way to expand and add specific contents to each table
     **/
    public void createMetaText() throws IOException{
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