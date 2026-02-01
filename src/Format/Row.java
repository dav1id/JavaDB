package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.Serializable;
import java.util.Comparator;

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

        page = (int) floor(id / 10);

        if (page > Table.TABLE_SIZE)
            throw new InvalidStatementException(); // create a unique exception here too
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
        if (row2.id == null)
            if (row2.getEmail() == null & row2.getUsername() != null)
                return username.compareTo(row2.getUsername());
            else if (row2.getEmail() != null & row2.getUsername() == null)
                return this.getEmail().compareTo(row2.getEmail());
            else
                c = email.compareTo(row2.getEmail());

        if (c == 0)
            return username.compareTo(row2.getUsername());


        return id.compareTo(row2.id);
    }
}
