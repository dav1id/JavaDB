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