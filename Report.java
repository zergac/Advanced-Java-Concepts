package sample;
import java.util.ArrayList;

public class Report {

    private String month;
    private String type;
    private int count;
    ArrayList<String[]> typeNMonth;

    public Report(String m, String t, int c){
        month = m;
        type = t;
        count = c;
    }

    public ArrayList<String[]> getTypeNMonth() {
        return typeNMonth;
    }

    public String getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTypeNMonth(ArrayList<String[]> typeNMonth) {
        this.typeNMonth = typeNMonth;
    }
}
