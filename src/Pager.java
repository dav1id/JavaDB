import java.io.*;
import java.util.ArrayList;

class Pager {
    /**
     Saves the row to a specific page depending on the rows id. Each page has 10 rows, so math.float(id/10) will
     give us the page that we should store the row in. Serializing a row will be called on insert, and other methods
     that change the composition of the row
     **/

    static void serialize_pages(Table table) throws IOException {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objOutputStream = null;
        ArrayList<Page> arrayTable = table.getTable();

        try {
            for (int i = 0; i < arrayTable.size(); ++i){
                fileOutputStream = new FileOutputStream(table.tableName + "/" + i);
                objOutputStream = new ObjectOutputStream(fileOutputStream);

                Page page = arrayTable.get(i);
                objOutputStream.writeObject(page);
            }

        } finally{
            if (fileOutputStream != null) fileOutputStream.close();
            if (objOutputStream != null) objOutputStream.close();
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

    static void deserialize_page(){

    }

    /*
     Possible implementation of a way to serialize individual pages (maybe an auto-save feature paired with an
     internal clock, to see how long the program has been running for? But we can trust that the finally will
     always serialize the page in main
     */

}
