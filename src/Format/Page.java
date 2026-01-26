package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.Serializable;
import java.util.HashMap;

public class Page implements Serializable {
    private final HashMap<Integer, Row> rows;// 10 rows

    public Page(){
        rows = new HashMap<>();
    }

    /**
     Gets the contents of the row by checking if the row exists, and returns null if there is no value associated with
     the id. Values that are not associated with the id are handled with the NullTableValueException

     @param id The id that is used to get or set the contents inside the Page's hashmap
     @return Row
     */

    public Row getRowContents(Integer id) throws InvalidStatementException {

        if (rows.containsKey(id)){
            return rows.getOrDefault(id, null);
        } else {
            throw new InvalidStatementException();
        }
    }

    public void setRowContents(Row row){
        rows.put(row.id, row);
    }
}
