package DatabaseExceptions;

/*
    Have to manually convert old data to new data for incompatible stream exceptions. Because I make changes to the serialized
    classes like Page or Row, I'm unable to run my scripts on old data that doesn't have those changes. So here I'm going to
    have to use a lot of try and catch to handle this -> Will probably just create new pages and add all the info from the old
    table to the new table
*/

public class oldDataHandling {
}

