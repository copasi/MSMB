package msmb.utility;

import java.io.IOException;

import javax.swing.text.TabableView;


public class MySyntaxException extends IOException {
   
    private int column;
    private String table;

    public MySyntaxException(int column, String message,String tableDescription) {
    	super(message);
      
       this.column = column;
       table = tableDescription;
    }

    public int getColumn() {
    	return this.column;
    }
    
    public String getTable() {
    	return this.table;
    }

	
    public MySyntaxException(String newMessage, MySyntaxException oldEx) {
    	super(newMessage);
      
       this.column = oldEx.column;
       table = oldEx.table;
    }

  
}


