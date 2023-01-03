import java.util.Objects;

public class Price {

    double value = 0;

    private double convertPrice(String str) {
        if (!(Objects.equals(str, ""))) {
            if(str.contains("–")){
                String[] p = str.split("–");
                str = p[0];
            }
            String s = str;
            String n;
            if(str.contains("₺")){
            int x = str.indexOf("₺");
            int y = str.lastIndexOf("₺");
            if(x==y && x == 0){
                s = str.substring(x + 1);
            } else {
              s = str.substring(x + 1, y);
            }}
            if (s.contains(",")&&s.contains(".")) {
                String[] p = s.split(",");
                n = p[0] +p[1];

            } else if(s.contains(",")){
                n = s.replaceAll(",", ".");
            }
            else {
                n = s;
            }
            value = Double.parseDouble(n.trim());
            return value;
        }
        return 1;
    }

    public double getPrice(String str){
        return convertPrice(str);
    }
}
