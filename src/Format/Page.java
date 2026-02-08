package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    public ArrayList<Row> getContentsOfRows(){
        Set<Integer> keySets = rows.keySet();
        ArrayList<Row> rowList = new ArrayList<>();

        for (Integer key : keySets)
            rowList.add(getRowContents(key));


        return rowList;
    }

    public Row getRowContents(Integer id){
        if (rows.containsKey(id)){
            return rows.getOrDefault(id, null);
        } else {
         //   throw new InvalidStatementException("Unable to get contents of Row");
            return null;
        }
    }

    public void setRowContents(Row row){
        rows.put(row.id, row);
    }

    public void deleteAllContents() {
        rows.clear();
    }
    public String deleteRowById(Integer id) throws InvalidStatementException {
        if (rows.containsKey(id)){
            String rowToString = rows.get(id).toString();
            rows.remove(id);

            return rowToString;
        } else {
            throw new InvalidStatementException("Nothing exists in this row");
        }

    }
}
