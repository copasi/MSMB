package gui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import utility.Constants;


import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;

import debugTab.DebugConstants;
import debugTab.DebugMessage;


public class PrintTablesToPDF {

    String modelName = new String("Model name");
    
	
    public String getModelName() {	return modelName;	}
	public void setModelName(String modelName) {this.modelName = modelName;	}



	public void createPdf(File file, Vector<Object> tablesAndLastTabInfo)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document(PageSize.LETTER.rotate());
        // step 2
        PdfWriter writer  = PdfWriter.getInstance(document, new FileOutputStream(file));
        HeaderFooter event = new HeaderFooter();
        
        
        float side = 20;
        Rectangle area = new Rectangle(side, side, 792-side, 612-side);
        writer.setBoxSize("area", area);
        writer.setPageEvent(event);
        
        // step 3
        document.open();
        // step 4
        Chapter chapter = new Chapter(new Paragraph("Model definition"), 1);
        chapter.add(Chunk.NEWLINE);
     	
        int i = 0;
        for(; i < tablesAndLastTabInfo.size(); i++) {
        	Object element = tablesAndLastTabInfo.get(i);
        	
        	if(element instanceof CustomTableModel) {
        		CustomTableModel tablemodel = (CustomTableModel) element;
        		int col = tablemodel.getColumnCount();

        		Paragraph title = new Paragraph(tablemodel.getTableName());

        		Section section = chapter.addSection(title);
        		section.add(Chunk.NEWLINE);
        	     	
        		section.setBookmarkTitle(title.getContent());
        		section.setIndentation(30);
        		section.setBookmarkOpen(false);
        		section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

        		PdfPTable table = new PdfPTable(col);

        		Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        		Phrase ttable = new Phrase(tablemodel.getTableName(), font);

        		PdfPCell cell = new PdfPCell(ttable);
        		cell.setBackgroundColor(BaseColor.BLACK);
        		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        		cell.setColspan(tablemodel.getColumnCount());
        		table.addCell(cell);


        		table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        		Vector<String> header = null;
        		if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.SPECIES.description)==0) header = Constants.species_columns;
        		else if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) header = Constants.compartments_columns;
        		else if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.EVENTS.description)==0) header = Constants.events_columns;
        		else if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) header = Constants.functions_columns;
        		else if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) header = Constants.globalQ_columns;
        		else if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.REACTIONS.description)==0) header = Constants.reactions_columns;

        		if(header!=null) header.add(0, "#");

        		for (int i1 = 0; i1 < tablemodel.getColumnCount(); i1++) {
        			if(header!=null) 	table.addCell(header.get(i1));
        			else table.addCell("");
        		}
        		table.getDefaultCell().setBackgroundColor(null);
        		table.setHeaderRows(2);

        		for(int i1 = 0; i1 < tablemodel.getRowCount()-1; i1++) {
        			for(int j = 0; j < tablemodel.getColumnCount(); j++) {
        				String value = tablemodel.getValueAt(i1, j).toString();
        				if(value.length() <=0) value = " ";
        				PdfPCell cell1 = new PdfPCell(new Phrase(value));
        				//   cell.setBackgroundColor(new BaseColor(Color.yellow.getRGB()));
        				
        				table.addCell(cell1);
        			}
        		}
        		section.add(table);

        		section.newPage();


        	} else {
        		break;
        	}
        	
        }
        document.add(chapter);
     	
    	Font font = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
		
    	Chapter chapter2 = new Chapter(new Paragraph("Model properties"), 2);
    	chapter2.add(Chunk.NEWLINE);
     	
     	Object element = tablesAndLastTabInfo.get(i);
        i++;
     	Chunk p = new Chunk("Unit Volume: "+element.toString(), font);
     	chapter2.add(p);
     	chapter2.add(Chunk.NEWLINE);

     	element = tablesAndLastTabInfo.get(i);
        i++;
     	p = new Chunk("Unit Time: "+element.toString(), font);
     	chapter2.add(p);
     	chapter2.add(Chunk.NEWLINE);

     	element = tablesAndLastTabInfo.get(i);
        i++;
     	p = new Chunk("Unit Quantity: "+element.toString(), font);
     	chapter2.add(p);
     	chapter2.add(Chunk.NEWLINE);
     	chapter2.add(Chunk.NEWLINE);
     	chapter2.add(Chunk.NEWLINE);
     	
     	element = tablesAndLastTabInfo.get(i);
        i++;
        String interpretation0 = new String();
        if((boolean) element) interpretation0 = "Concentration";
        else interpretation0 = "Particle Number";
     	p = new Chunk("If no reference is specified, in expressions the "+interpretation0 +" is used for calculations", font);
     	chapter2.add(p);
     	chapter2.add(Chunk.NEWLINE);
     	
     	element = tablesAndLastTabInfo.get(i);
        i++;
        String interpretation = new String();
        if((boolean) element) interpretation = "Concentration";
        else interpretation = "Particle Number";
     	p = new Chunk("Quantity column for initial value is interpreted as "+interpretation, font);
     	chapter2.add(p);
     	
		
     	document.add(chapter2);
     	
     	
     	element = tablesAndLastTabInfo.get(i);
     	i++;
     	
     	Chapter chapter3 = new Chapter(new Paragraph("Debug messages"), 2);
     	chapter3.add(Chunk.NEWLINE);
    	if(element instanceof HashMap<?, ?> ) {
    		HashMap<String, DebugMessage>  debugMessages = (HashMap<String, DebugMessage> ) element;
    		printDebugMessages(chapter3,debugMessages);
    	    document.add(chapter3);
    	}
    	
      
      
        
        
        
        // step 5
        document.close();
    }
 

    
    private void printDebugMessages(Chapter chapter, HashMap<String, DebugMessage> debugMessages) {
    		Font font = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
		Vector<String> priorities = new Vector<String>();
		priorities.add(DebugConstants.PriorityType.MAJOR.description);
		priorities.add(DebugConstants.PriorityType.PARSING.description);
		priorities.add(DebugConstants.PriorityType.INCONSISTENCIES.description);
		priorities.add(DebugConstants.PriorityType.MISSING.description);
		priorities.add(DebugConstants.PriorityType.EMPTY.description);
		priorities.add(DebugConstants.PriorityType.DEFAULTS.description);
		priorities.add(DebugConstants.PriorityType.MINOR.description);
		priorities.add(DebugConstants.PriorityType.MINOR_EMPTY.description);
		priorities.add(DebugConstants.PriorityType.SIMILARITY.description);
			
		for(String priorityDescr : priorities) {
			Paragraph title = new Paragraph("Priority: "+priorityDescr);
	    	Section section = chapter.addSection(title);
	    	section.add(Chunk.NEWLINE);
			Iterator it = MainGui.debugMessages.keySet().iterator();
			Vector<DebugMessage> sorted = new Vector<DebugMessage>();
			while(it.hasNext()) {
				String key = (String) it.next();
				if(key.contains("@"+DebugConstants.PriorityType.getIndex(priorityDescr)+"_")) {
						sorted.add((DebugMessage)MainGui.debugMessages.get(key));
      			} 
			}
			Collections.sort(sorted);
			for (DebugMessage x : sorted) {
				Chunk p = new Chunk(x.getShortDescription(), font);
				section.add(p);
				section.add(Chunk.NEWLINE);
				p = new Chunk(x.getCompleteDescription(), font);
				section.add(p);
				section.add(Chunk.NEWLINE);
			}
    	} 
           
	
	}



	/** Inner class to add a header and a footer. */
    class HeaderFooter extends PdfPageEventHelper {
       
    
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
           Rectangle rect = writer.getBoxSize("area");
           
           PdfPTable table = new PdfPTable(2);
           try {
             table.setWidths(new int[]{24, 24});
             table.setTotalWidth(rect.getWidth());
             table.setLockedWidth(true);
             table.getDefaultCell().setFixedHeight(20);
             table.getDefaultCell().setBorder(Rectangle.BOTTOM);
             table.addCell(new Phrase(modelName));
             table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
             table.addCell(String.format("pag. %d", writer.getPageNumber()));
             table.writeSelectedRows(0, -1, 20f, 40f,  writer.getDirectContent());
           }
           catch(DocumentException de) {
             throw new ExceptionConverter(de);
           }
           
       /*    ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("pag. %d", writer.getPageNumber())),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getTop(), 0);*/
        
           Date timestamp = new Date();
           
           SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
           
           
          
          ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER,  new Phrase(df.format(timestamp), FontFactory.getFont("Arial", 10)),
               rect.getRight(),  rect.getHeight() / 2, -90);
         
        }
        
    }
 
}