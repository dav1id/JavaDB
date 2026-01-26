package Format;

import DatabaseExceptions.InvalidStatementException;

import java.io.Serializable;

import static java.lang.Math.floor;

public class Row implements Serializable {
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

    public String getUser(){
        return username;
    }
}
