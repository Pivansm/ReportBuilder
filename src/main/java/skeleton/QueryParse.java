package skeleton;

import javax.swing.*;
import java.io.*;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryParse {
    private static final String MULTI_PARAMT_FL = "@TFL1";
    private String query;

    public QueryParse(String query) {
        this.query = query;
    }

    private void parseQuery() {

        HashSet<String> hashSet = new HashSet<>();

        Pattern pattern = Pattern.compile(MULTI_PARAMT_FL);
        Matcher matcher = pattern.matcher(query);

        if(matcher.find()) {
            System.out.println("Найден: " + matcher.group(0));

            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                try {
                    BufferedReader b = new BufferedReader(new FileReader(file));
                    String readLine = "";
                    System.out.println("Reading file using Buffered Reader");

                    while ((readLine = b.readLine()) != null) {
                        System.out.println(readLine);
                        hashSet.add(readLine);
                    }

                    //String strMacros = String.join(",", hashSet);
                    String strMacros = hashSet.stream()
                            //.map(Person::getFirstName)
                            .collect(Collectors.joining(", ")); // ", "
                    System.out.println(strMacros);
                    query.replaceAll(MULTI_PARAMT_FL, strMacros);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        parseQuery();
        return query;
    }
}
