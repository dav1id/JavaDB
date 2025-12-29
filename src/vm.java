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

import static java.lang.Math.floor;

class vm {
    static void executeInsert(Row row, Table table){
        Integer pageForRow = row.page;
        ArrayList<Page> pagesTable = table.getTable();
        Page page = pagesTable.get(pageForRow);

        page.setRowContents(row);
    }

    static Row executeSelect(Integer id, Table table) throws Exception{ // Assuming that
        Integer pageForRow = (int) floor(id/10);
        ArrayList<Page> pagesTable = table.getTable();

        if (pageForRow > table.TABLE_SIZE)
            throw new Exception();

        Page page = pagesTable.get(pageForRow);
        return page.getRowContents(id);
    }
}






