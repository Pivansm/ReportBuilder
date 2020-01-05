package skeleton;

public class Report extends Entity {
    private String nameReport;

    public Report() {

    }

    public Report(int id, String nameReport) {
        super(id);
        this.nameReport = nameReport;
    }

    public String getNameReport() {
        return nameReport;
    }

    public void setNameReport(String nameReport) {
        this.nameReport = nameReport;
    }

    @Override
    public String toString() {
        return "" + nameReport;
    }
}
