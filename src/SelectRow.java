import DatabaseExceptions.InvalidStatementException;
import Format.Page;
import Format.Row;
import Format.Table;

import java.util.ArrayList;

import static java.lang.Math.floor;

public class SelectRow {

    /**
        Select every row in table.
        @return ArrayList<Row> ArrayList of all the rows that fit selection criteria
     **/
    public ArrayList<Row> selectAll(Table table){
        ArrayList<Row> rowList = new ArrayList<>();
        ArrayList<Page> pagesList = table.getTable();

        for (Page page : pagesList)
            rowList.addAll(page.getContentsOfRows());

        return rowList;
    }

    /**

     @return ArrayList<Row> ArrayList of all the rows that fit selection criteria
     **/
    public ArrayList<Row> getRowSingleSelect(String[] contents, Table table) throws InvalidStatementException {
        ArrayList<Page> pagesTable = table.getTable();
        ArrayList<Row> rowList = new ArrayList<>();

        Row row = new Row(null, null, null);

        try {
            Integer id = Integer.parseInt(contents[1]);
            int pageForRow = (int) floor((double) id / 10);

            Page page = pagesTable.get(pageForRow);
            rowList.add(page.getRowContents(id));

        } catch(NumberFormatException e) {
            if (contents[1].equals("*"))
                return selectAll(table);

            char[] listOfCharacters = new char[contents[1].length()];
            contents[1].getChars(0, contents[1].length(), listOfCharacters, 0);

            for (char c : listOfCharacters) {
                if (c == '@')
                    row.setEmail(contents[1]); // .select template1o@shaw.ca
            }

            row.setUsername(contents[1]); // .select template2o@shaw.ca
            rowList = Row.rowComparison(row, rowList, pagesTable);
        }
        return rowList;
    }

    /**

     @return ArrayList<Row> ArrayList of all the rows that fit selection criteria
     **/
    public ArrayList<Row> getRowMultiSelect(String[] contents, Table table) throws InvalidStatementException{
        Row row = new Row(null, null, null);
        ArrayList<Row> rowList = new ArrayList<>();
        Page page;

        ArrayList<Page> pagesTable = table.getTable();

        try { // .select 40 david1o@shaw.ca. Will just return the value with the id
            Integer id = Integer.parseInt(contents[1]);
            //   Integer pageForRow = (int) floor((double) id / 10);

            page = pagesTable.get(Row.pageForRow(id));
            rowList.add(page.getRowContents(id));

        } catch(NumberFormatException e){ //name always comes before email
            row.setUsername(contents[1]);
            row.setEmail(contents[2]);
            rowList = Row.rowComparison(row, rowList, pagesTable);

        }
        return rowList;
    }

    /**
        Handler for the other select methods (single-select for one row parameter with select statement, or multi-select
        for more than one row parameter).

        @param token User input as a string
        @param table Collection of pages
        @return ArrayList<Row> ArrayList of all the rows that fit selection criteria
     **/
    public ArrayList<Row> getRowContents(String token, Table table) throws InvalidStatementException {
        String[] splitList = token.split(" ");
        ArrayList<Row> rowList;

        if (splitList.length == 2){ // .select 40, or .select david, or .select david1o@shaw.ca
            rowList = getRowSingleSelect(splitList, table);

        } else { // .select 40 david, .select david davetobi@yahoo.com
            rowList = getRowMultiSelect(splitList, table);
        }

        return rowList;
    }
}
