package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser implements Database {

    String query;
    Pattern queryPattern;
    Matcher matcher;
    String tableName;
    ArrayList<String> elements;
    ArrayList<String> values;
    ArrayList<String> dataTypes;
    String URI;

    public Parser() {
        URI="C://Users//HP-//Documents//NetBeansProjects//Database//src//eg//edu//alexu//csd//oop//db";
    }
    
    public Parser(String URI) {  // Only One Instance ????
        this.URI=URI;
    }

    public void setQuery(String query) {
        this.query = query;
        elements = new ArrayList<>();
        dataTypes = new ArrayList<>();
        values = new ArrayList<>();
        
    }
    public static void main(String[] args) {
        String q="CREATE TABLE table_name (Student_name varchar, GPA varchar, ID int);";
        System.out.println(Pattern.matches("(\\s+|)(CREATE)(\\s+)(TABLE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\(){1}((\\s*)([a-zA-Z0-9._%]+)(\\s+)(varchar|int)(\\s*)(\\,|))+(\\s*)(\\)){1}(\\s*)(;)(\\s*)", q));
    }
    public boolean validateCommand() {

        if (Pattern.matches("(\\s+|)(CREATE)(\\s+)(TABLE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\(){1}((\\s*)([a-zA-Z0-9._%]+)(\\s+)(varchar|int)(\\s*)(\\,|))+(\\s*)(\\)){1}(\\s*)(;)(\\s*)", query) || Pattern.matches("(\\s+|)(DROP)(\\s+)(TABLE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)", query)) {
            try {
                this.executeStructureQuery(query);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } //* NOTE: THIS ONE IS SCREWED UP FOR ME AND I CAN'T SEE ITS OUTPUT FOR SOME REASON SO I'M NOT SURE IF IT WORKS - THE METHOD CALL INSIDE AS WELL *//
        else if (Pattern.matches("(\\s+|)(SELECT)(\\s+)((\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|))+(\\s+)(FROM)(\\s+)([a-zA-Z0-9._%]+)(\\s+)(WHERE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(>|<|=)(\\s*)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)", query)) {
            try {
                this.executeRetrievalQuery(query);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (Pattern.matches("(\\s+|)(DELETE)(\\s+)(FROM)(\\s+)([a-zA-Z0-9._%]+)(\\s+)(WHERE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(>|=|<)(\\s*)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)", query) || Pattern.matches("(\\s+|)(INSERT)(\\s+)(INTO)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\(){1}((\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|))+(\\s*)(\\)){1}(\\s+)(VALUES)(\\s*)(\\(){1}((\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|))+(\\s*)(\\)){1}(\\s*)(;)(\\s*)", query)) {
            try {
                this.executeUpdateQuery(query);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Command");
        }
        return false;
    }

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        // TODO Auto-generated method stub
        setQuery(query);
        System.out.println("Matches create or drop");
        if (Pattern.matches("(\\s+|)(CREATE)(.)+", query)) {
            queryPattern = Pattern.compile("(\\s+|)(CREATE)(\\s+)(TABLE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\(){1}((\\s*)([a-zA-Z0-9._%]+)(\\s+)(varchar|int)(\\s*)(\\,|))+(\\s*)(\\)){1}(\\s*)(;)(\\s*)");
            matcher = queryPattern.matcher(query);
            if (matcher.find()) {
                tableName = matcher.group(6);
                System.out.println(tableName);
            }
            /* Check if name already exists 
                if it already exists display error message / return false 
                else continue 
             */
            queryPattern = Pattern.compile("(\\s*)([a-zA-Z0-9._%]+)(\\s+)(varchar|int)(\\s*)(\\,|)");
            matcher = queryPattern.matcher(query);
            while (matcher.find()) {
                System.out.println(matcher.group(2));
                elements.add(matcher.group(2));
                System.out.println(matcher.group(4));
                dataTypes.add(matcher.group(4));
            }
             
         return new DBMS(URI).createTable(tableName,dataTypes.toArray(new String[dataTypes.size()]),elements.toArray(new String[elements.size()]));
            
        } else {
            queryPattern = Pattern.compile("(\\s+|)(DROP)(\\s+)(TABLE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)");
            matcher = queryPattern.matcher(query);
            if (matcher.find()) {
                tableName = matcher.group(6);
                System.out.println(tableName);
            }
            return new DBMS(URI).dropTable(tableName);
            /* 
          Check if file name exists 
	            if exists then return DBMS.deleteFile();
	            else return false
             */
        }        
    }

    @Override
    public Object[][] executeRetrievalQuery(String query) throws SQLException {
        // TODO Auto-generated method stub
        setQuery(query);
        System.out.println("Matches select");

        queryPattern = Pattern.compile("(\\s+|)(SELECT)(\\s+)((\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|))+(\\s+)(FROM)(\\s+)([a-zA-Z0-9._%]+)(\\s+)(WHERE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(>|<|=)(\\s*)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)");
        matcher = queryPattern.matcher(query);

        String leftSide = null, condition = null, rightSide = null;

        if (matcher.find()) {
            tableName = matcher.group(12);
            leftSide = matcher.group(16);
            condition = matcher.group(18);
            rightSide = matcher.group(20);
        }
        /*
		 Check if file name exists
		       if doesn't exist display error message / return false
		       else continue
         */
        System.out.println(tableName);
        System.out.println(leftSide);
        System.out.println(condition);
        System.out.println(rightSide);

        queryPattern = Pattern.compile("(\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|)");
        matcher = queryPattern.matcher(query.substring(query.indexOf('T') + 1, query.indexOf('F') - 1));
        while (matcher.find()) {
            elements.add(matcher.group(2));
            System.out.println(matcher.group(2));
        }
        /*
		 Validate columns in DBMS 
         */
        return new DBMS(URI).query(tableName, rightSide, condition.charAt(0), leftSide);
    }

    @Override
    public int executeUpdateQuery(String query) throws SQLException {
        // TODO Auto-generated method stub
        if (Pattern.matches("(\\s+|)(DELETE)(\\s+)(FROM)(\\s+)([a-zA-Z0-9._%]+)(\\s+)(WHERE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(>|=|<)(\\s*)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)", query) || Pattern.matches("(\\s+|)(INSERT)(\\s+)(INTO)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\()((\\s*)([a-zA-Z0-9._%]+)(\\,*))+(\\))(\\s*)(VALUES)(\\s*)(\\()((\\s*)([a-zA-Z0-9._%]+)(\\,*))+(\\))(\\s*)(\\;)", query)){}
        else{return -1;}
        setQuery(query);
        System.out.println("Matches delete or insert");

        if (Pattern.matches("(\\s+|)(DELETE)(.)+", query)) {
            queryPattern = Pattern.compile("(\\s+|)(DELETE)(\\s+)(FROM)(\\s+)([a-zA-Z0-9._%]+)(\\s+)(WHERE)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(>|=|<)(\\s*)([a-zA-Z0-9._%]+)(\\s*)(;)(\\s*)");
            matcher = queryPattern.matcher(query);

            String leftSide = null, condition = null, rightSide = null;

            if (matcher.find()) {
                tableName = matcher.group(6);
                leftSide = matcher.group(10);
                condition = matcher.group(12);
                rightSide = matcher.group(14);
            }

            System.out.println(tableName);
            System.out.println(leftSide);
            System.out.println(condition);
            System.out.println(rightSide);
            return new DBMS(URI).delete(tableName,rightSide , condition.charAt(0), leftSide);
            /*	
		check in DBMS for tableName
		      if exists (check two sides of condition**) return DBMS.delete(); 
		      else display error message / return 0;	
		**idk what needs to be done with the conditions operands, up to you in the DBMS Class
             */
        } else {
            System.out.println("insert");
            queryPattern = Pattern.compile("(\\s+|)(INSERT)(\\s+)(INTO)(\\s+)([a-zA-Z0-9._%]+)(\\s*)(\\()((\\s*)([a-zA-Z0-9._%]+)(\\,*))+(\\))(\\s*)(VALUES)(\\s*)(\\()((\\s*)([a-zA-Z0-9._%]+)(\\,*))+(\\))(\\s*)(\\;)");
            matcher = queryPattern.matcher(query);
            if (matcher.find()) {
                tableName = matcher.group(6);
                System.out.println(tableName);
            }
            queryPattern = Pattern.compile("(\\s*)([a-zA-Z0-9._%]+)(\\s*)(\\,|)");
            matcher = queryPattern.matcher(query);
            int occurrences = 0;
            while (matcher.find()) {
                if (!(matcher.group(2).equals("INSERT") || matcher.group(2).equals("INTO") || matcher.group(2).equals("VALUES") || matcher.group(2).equals(tableName))) {
                    elements.add(matcher.group(2));
                    occurrences++;
                }
            }

            if (occurrences % 2 == 1) {
                return 0; //if values =/= columns
            }
            for (int i = occurrences / 2; elements.size() > occurrences / 2;) {
                values.add(elements.remove(i));
            }

            for (int i = 0; i < values.size(); i++) {
                System.out.println(elements.get(i) + " " + values.get(i));
            }
            System.out.println("Elements: "+elements+"\nValues: "+values);
            return new DBMS(URI).insert(tableName, elements.toArray(new String[elements.size()]), values.toArray(new String[values.size()]));
            /*  
        check if tableName exists
           if doesn't exist display error message / return 0
           else check if elements & value types in table are compatible
               if so return DBMS.insert();   
               else display error message / return 0;
             */
        }
        
    }

}
//(\s*)(CREATE)(\s*)(TABLE)(\s*)([a-zA-Z0-9._%]+)(\s+)(\()(\s*)((\s*)([a-zA-Z0-9._%]+)(\s+)(varchar|int)(\s*)(,|))+(\s*)(\)|)(;)(\s*)