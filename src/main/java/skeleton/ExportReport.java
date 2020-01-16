package skeleton;

import javax.swing.*;
import java.io.File;

public class ExportReport {
    String strRecord;
    JTable table;
    String strNameReport;

    public ExportReport(JTable table) {
        this.table = table;
    }

    public void exportXmlExcel() {


        File file = new File(strNameReport);
        //if(file.exists())

        if(table.getRowCount() > 0 ) {

            strRecord = "<?xml version=\"1.0\"?>\n";
            strRecord += "<?mso-application progit=\"Excel.Sheet\"?>\n";
            strRecord += "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" ";
            strRecord += "xmlns:o=\"urn:schemas-microsoft-com:office:office\" ";
            strRecord += "xmlns:x=\"urn:schemas-microsoft-com:office:excel\" ";
            strRecord += "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" ";
            strRecord += "xmlns:html=\"http://www.w3.org/TR/REC-html40\">";
            
        }

    }

    public void exportCsv() {

    }
}