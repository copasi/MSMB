package debugTab;

import utility.Constants;

public class FoundElement implements Comparable<FoundElement> {
	String tableDescription;
	int row = 0;
	int col = 0;
	
	public FoundElement(String tableDescr, int row, int col) {
		tableDescription = new String(tableDescr);
		this.row = row;
		this.col = col;
	}

	public String getTableDescription() {	return tableDescription; }

	public void setTableDescription(String tableDescription) {	this.tableDescription = tableDescription;	}

	public int getRow() {	return row;	}

	public void setRow(int row) {	this.row = row;	}

	public int getCol() {	return col;	}

	public void setCol(int col) {	this.col = col;	}

	@Override
	public int compareTo(FoundElement o) {
		int ret = this.tableDescription.compareTo(o.getTableDescription()) + this.row - o.getRow() + this.col - o.getCol();
		return ret;
	}
	
		@Override 
		public int hashCode() {
	        return (27 * (7 + this.row)*(13+this.col)  + Constants.TitlesTabs.getIndexFromDescription(this.tableDescription));
	    }
		
	@Override
	public String toString() {
		return new String("("+row+","+col+")");
	}
}
