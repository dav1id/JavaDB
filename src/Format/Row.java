package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Math.floor;

public class Row implements Serializable, Comparable<Row> {
    private String username;
    private String email;
    public Integer id;
    public Integer page;

    public Row(Integer id, String username, String email) throws InvalidStatementException {
        this.username = username;
        this.email = email;
        this.id = id;

        if (id == null){
            page = 0;
        } else {
            page = (int) floor(id / 10);

            if (page > Table.TABLE_SIZE)
                throw new InvalidStatementException("Index out of range");
        }
    }

    public String getEmail(){
        return email;
    }
    public String getUsername(){
        return username;
    }

    public void setEmail(String email){this.email = email;}

    public void setUsername(String username){this.username = username;}

    public int compareTo(Row row2){
        int c = 0;

        if (getEmail() == null & getUsername() != null)
            return username.compareTo(row2.getUsername());
        else if (getEmail() != null & getUsername() == null)
            return getEmail().compareTo(row2.getEmail());
        else
            c = email.compareTo(row2.getEmail());

        if (c == 0)
            return username.compareTo(row2.getUsername());
        return id.compareTo(row2.id);
    }

    public String toString(){
        try {
            return id.toString() + " " + username + " " + email;
        } catch(NullPointerException e){
            return null;
        }
    }

    /**
     Compares all existing rows and returns the rows that match. In the logic used with comparisons, a temporary row is
     created that has a null-value assigned to the username or email.

     @param row Simple dataset of id, email, and username
     @param rowList ArrayList of type Row
     @param pagesTable Pointer to the table's array of Pages
     @return ArrayList<Row>
     **/

    public static ArrayList<Row> rowComparison(Row row, ArrayList<Row> rowList, ArrayList<Page> pagesTable) throws InvalidStatementException{
        Page page;

        for (int i = 0; i < Table.TABLE_SIZE; ++i) { //Learn a way to go through this quicker
            page = pagesTable.get(i);
            ArrayList<Row> contents = page.getContentsOfRows();

            for (Row rowContent : contents)
                if (row.compareTo(rowContent) == 0)
                    rowList.add(rowContent);
        }

        return rowList;
    }

    public static Integer pageForRow(Integer id){
        return (int) floor((double) id / 10);
    }
}
