/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 *
 * @author HP-
 */
public class DBMS {

    //The path we are going to save our files in
    public final static String workingDirectory = "C://Users//HP-//Documents//NetBeansProjects//Database//src//eg//edu//alexu//csd//oop//db";
    //Array List to save the names' of the tables we have
    private ArrayList<String> tablesNames;
    public DBMS(){
        initialize();
    }

    public void initialize() {
        this.tablesNames = new ArrayList<>();
        //Check if the path have any xml files and add them in the array list
        File folder = new File(workingDirectory);
        File[] listOfFiles = folder.listFiles();
        try {
            for (File file : listOfFiles) {

                if (file.isFile()) {
                    String name = file.getName();
                    if (name.endsWith(".xml")) {
                        String tableName = name.substring(0, name.length() - 4);
                        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(tableName + ".xml"));

                        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                        Schema schema = factory.newSchema(new File(tableName + ".xsd"));

                        Validator validator = schema.newValidator();
                        validator.validate(new StAXSource(reader));

                        //no exception thrown, so valid
//                        System.out.println("Document is valid");

                        tablesNames.add(tableName);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR\n" + e.toString());
        }
    }

    /*
    Check if the this table is in the Arraylist, if it is delete it and delete the xml file as well
     */
    public boolean dropTable(String tableName) {

        File folder = new File(workingDirectory);
        File[] listOfFiles = folder.listFiles();
        boolean flag=false;
        for (File file : listOfFiles) {

            if (file.isFile()) {
                String name = file.getName();
                if (name.equals(tableName + ".xml") || name.equals(tableName + ".xml")) {
                    tablesNames.remove(name);
                    file.delete();
                    flag=true;
                }
            }
        }
        return flag;
    }

    /**/
    public boolean createTable(String tableName, String[] columnTypes, String[] columnNames) {
        System.out.println("creating table");
        if (tablesNames.contains(tableName)) {
            System.out.println("table here already");
            return false;
        }
        try {
            File schema = new File(workingDirectory +"//"+ tableName + ".xsd");
            schema.createNewFile();
            java.io.FileWriter fw = new java.io.FileWriter(schema);
            fw.write(generateXSD(tableName, columnNames));
            fw.close();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            FileWriter fileWriter = new FileWriter(workingDirectory + "//" + tableName + ".xml");
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(fileWriter);
            
            xMLStreamWriter.writeStartDocument();
            //xMLStreamWriter.writeDTD("<!DOCTYPE table SYSTEM \"" + tableName + ".dtd\">");
            xMLStreamWriter.writeStartElement("table");
            xMLStreamWriter.writeNamespace("xsi", "http://www.w3.org/2000/10/XMLSchema-instance");
            xMLStreamWriter.writeAttribute("http://www.w3.org/2000/10/XMLSchema-instance", "noNamespaceSchemaLocation",
                    "path_to_your.xsd");
            xMLStreamWriter.writeStartElement("row");

            for (int i = 0; i < columnNames.length; i++) {
                xMLStreamWriter.writeStartElement(columnNames[i]);
                xMLStreamWriter.writeCharacters(columnTypes[i]);
                xMLStreamWriter.writeEndElement();
            }

            xMLStreamWriter.writeEndElement();//row
            xMLStreamWriter.writeEndElement();//table

            xMLStreamWriter.writeEndDocument();

            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            fileWriter.close();
            //System.out.println(xmlString);
        } catch (Exception e) {
            System.out.println("CreateTable : " + e.toString());
            return false;
        }
        System.out.println("table Created");
        return true;
    }

    public static void main(String[] args) {

        
        
//        try{
//            File schema = new File(workingDirectory + "\\try" + ".xsd");
//            System.out.println(schema.exists());
//            System.out.println(schema.createNewFile());}catch(Exception e){}
        //DBMS db = new DBMS();
//        db.delete("mazen", "Name", '=', "Mazen");
        //System.out.println(db.insert("mazen", new String[]{"Nada", "4302", "3.9"}, new String[]{"Name", "ID", "GPA"}));
        //System.out.println(db.createTable("mazen", new String[]{"String", "int", "String"}, new String[]{"Name", "ID", "GPA"}));
        //DBMS sd = new DBMS();
//        String[][] queries = (String[][]) db.query("mazen", "GPA", '<', "3");
//        if (queries != null) {
//            System.out.println(queries.length);
//            for (String[] row : queries) {
//                for (String element : row) {
//                    System.out.print(element + " ");
//                }
//                System.out.println("");
//            }
//        } else {
//            System.out.println("null");
//        }
        //System.out.println(db.dropTable("mazen"));
    }

    public int insert(String tableName, String[] values, String[] columns) {
        //Return False if the table's name is invalid
        System.out.println("inserting");
        if (!tablesNames.contains(tableName)) {
            System.out.println("table not here");
            return 0;
        }
        String file = workingDirectory + "//" + tableName + ".xml";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileWriter fileWriter = new FileWriter(workingDirectory + "//temp.xml");
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(fileInputStream);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            boolean isWritten = false;
            while (eventReader.hasNext()) {
                System.out.println("looping");
                XMLEvent event = eventReader.nextEvent();
                writer.add(event);

                if (event.getEventType() == XMLEvent.END_ELEMENT && !isWritten) {
                    if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                        writer.add(eventFactory.createStartElement("", null, "row"));

                        for (int i = 0; i < values.length; i++) {
                            writer.add(eventFactory.createStartElement("", null, columns[i]));
                            writer.add(eventFactory.createCharacters(values[i]));
                            writer.add(eventFactory.createEndElement("", null, columns[i]));
                        }
                        isWritten = true;
                        writer.add(eventFactory.createEndElement("", null, "row"));
                        System.out.println("done");

                    }
                }

            }
            //writer.add(eventFactory.createEndElement("", null, "table"));

            writer.close();
            eventReader.close();
            fileWriter.close();
            fileInputStream.close();

            System.out.println("out of condition");
        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }

        File folder = new File(workingDirectory);
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                String name = f.getName();
                if (name.equals(tableName + ".xml")) {
                    System.out.println("deleted? " + f.delete());
                }
            }
        }

        for (File f : listOfFiles) {
            if (f.isFile()) {
                String name = f.getName();
                if (name.equals("temp.xml")) {
                    System.out.println("Renamed? " + f.renameTo(new File(file)));
                }
            }
        }

        return 1;
    }

    public Object[][] query(String tableName, String column, char condition, Object Value) {
        String[][] types = (String[][]) findTypes(tableName);
        if (types == null) {
            return null;
        }

        String type = null;
        for (String[] columnData : types) {
            if (columnData[0].equalsIgnoreCase(column)) {
                type = columnData[1];
                break;
            }
        }
        if (type == null) {
            return null;
        }
        String stringValue = null;

        if (type.equals("int")) {
            stringValue = Value.toString();
        } else {
            stringValue = (String) Value;
        }

        int conditionValue;
        List<List<String>> listOfLists = new ArrayList<List<String>>();
        switch (condition) {
            case '>':
                conditionValue = 1;
                break;
            case '<':
                conditionValue = -1;
                break;
            case '=':
                conditionValue = 0;
                break;

            default:
                return null;
        }

        String file = workingDirectory + "//" + tableName + ".xml";
        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(fileInputStream);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            ArrayList<String> row = new ArrayList<>();
            boolean found = false;
            String lastColumn = null;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.getEventType() == XMLEvent.END_ELEMENT) {
                    if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                        if (found) {
                            listOfLists.add(new ArrayList<>());
                            for (String element : row) {
                                listOfLists.get(listOfLists.size() - 1).add(element);
                            }
                        }
                        found = false;
                        row = new ArrayList<>();
                        lastColumn = null;
                    }
                }
                if (event.getEventType() == XMLEvent.START_ELEMENT) {
                    String element = event.asStartElement().getName().toString();
                    if (element.equalsIgnoreCase("row") || element.equalsIgnoreCase("table")) {
                        continue;
                    }
                    lastColumn = element;
                }
                if (event.getEventType() == XMLEvent.CHARACTERS) {
                    row.add(event.asCharacters().getData());
                    int compareInt = event.asCharacters().getData().compareToIgnoreCase(stringValue);
                    if (compareInt != 0) {
                        compareInt /= Math.abs(event.asCharacters().getData().compareToIgnoreCase(stringValue));
                    }
                    System.out.println(event.asCharacters().getData() + " " + compareInt);

                    if (lastColumn.equalsIgnoreCase(column) && compareInt == conditionValue) {
                        found = true;
                        //   System.out.println("found");
                    }
                }
            }

            //writer.add(eventFactory.createEndElement("", null, "table"));
            eventReader.close();
            fileInputStream.close();

            System.out.println("out of condition");
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        if (listOfLists.size() == 0) {
            return null;
        }
        String[][] queries = new String[listOfLists.size()][types.length];
        int i = 0;
        for (i = 0; i < listOfLists.size(); i++) {
            for (int k = 0; k < types.length; k++) {
                queries[i][k] = listOfLists.get(i).get(k);
            }
        }
        return queries;
    }

    public Object[][] findTypes(String tableName) {
        if (!tablesNames.contains(tableName)) {
            return null;
        }
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> columnTypes = new ArrayList<>();
        String[][] types = null;

        String file = workingDirectory + "//" + tableName + ".xml";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(fileInputStream);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            while (eventReader.hasNext()) {
                System.out.println("looping");
                XMLEvent event = eventReader.nextEvent();

                if (event.getEventType() == XMLEvent.END_ELEMENT) {
                    if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                        break;
                    }
                }

                if (event.getEventType() == XMLEvent.START_ELEMENT) {
                    if (event.asStartElement().getName().toString().equalsIgnoreCase("table")) {
                        continue;
                    }
                }

                if (event.getEventType() == XMLEvent.START_ELEMENT) {
                    if (event.asStartElement().getName().toString().equalsIgnoreCase("row")) {
                        while (true) {
                            event = eventReader.nextEvent();
                            if (event.getEventType() == XMLEvent.END_ELEMENT) {
                                if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                                    break;
                                }
                            }
                            if (event.getEventType() == XMLEvent.START_ELEMENT) {
                                columnNames.add(event.asStartElement().getName().toString());
                            }

                            if (event.getEventType() == XMLEvent.CHARACTERS) {
                                columnTypes.add(event.asCharacters().getData());
                            }
                        }
                        break;

                    }
                }

            }
            types = new String[columnNames.size()][2];
            for (int i = 0; i < columnNames.size(); i++) {
                types[i][0] = columnNames.get(i);
                types[i][1] = columnTypes.get(i);
            }
            //writer.add(eventFactory.createEndElement("", null, "table"));

            ;
            eventReader.close();
            fileInputStream.close();

        } catch (Exception e) {
            System.out.println(e.toString());

        }

        return types;
    }

    public int delete(String tableName, String column, char condition, Object Value) {
        int counter = 0;
        String[][] types = (String[][]) findTypes(tableName);
        if (types == null) {
            return counter;
        }

        String type = null;
        for (String[] columnData : types) {
            if (columnData[0].equalsIgnoreCase(column)) {
                type = columnData[1];
                break;
            }
        }
        if (type == null) {
            return counter;
        }
        String stringValue = null;

        if (type.equals("int")) {
            stringValue = Value.toString();
        } else {
            stringValue = (String) Value;
        }

        int conditionValue = 0;

        switch (condition) {
            case '>':
                conditionValue = 1;
                break;
            case '<':
                conditionValue = -1;
                break;
            case '=':
                conditionValue = 0;
                break;
        }

        String file = workingDirectory + "//" + tableName + ".xml";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileWriter fileWriter = new FileWriter(workingDirectory + "//temp.xml");
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inFactory.createXMLEventReader(fileInputStream);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            boolean found = false;
            String lastColumn = null;

            while (eventReader.hasNext()) {
                System.out.println("looping");
                XMLEvent event = eventReader.nextEvent();
                ArrayList<String> row = new ArrayList<>();

                if ((event.getEventType() == XMLEvent.START_ELEMENT)) {
                    if (event.asStartElement().getName().toString().equalsIgnoreCase("row")) {
                        while (true) {

                            event = eventReader.nextEvent();

                            if (event.getEventType() == XMLEvent.START_ELEMENT) {
                                String element = event.asStartElement().getName().toString();

                                lastColumn = element;
                            }
                            if (event.getEventType() == XMLEvent.END_ELEMENT) {
                                if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                                    System.out.println("end element start");
                                    if (found) {
                                        counter++;
                                        found = false;
                                        row = new ArrayList<>();
                                        event = eventReader.nextEvent();
                                        break;
                                    }
                                    writer.add(eventFactory.createStartElement("", null, "row"));
                                    System.out.println("row : " + row.size() + ", table : " + types.length);
                                    for (int i = 0; i < row.size(); i++) {

                                        writer.add(eventFactory.createStartElement("", null, types[i][0]));
                                        writer.add(eventFactory.createCharacters(row.get(i)));
                                        writer.add(eventFactory.createEndElement("", null, types[i][0]));
                                        System.out.println("Ending");
                                    }
                                    writer.add(eventFactory.createEndElement("", null, "row"));
                                    event = eventReader.nextEvent();
                                    System.out.println("done");
                                    row = new ArrayList<>();
                                    lastColumn = null;
                                    System.out.println("end element end");
                                }
                            }
                            if (event.getEventType() == XMLEvent.CHARACTERS) {
                                row.add(event.asCharacters().getData());
                                int compareInt = event.asCharacters().getData().compareToIgnoreCase(stringValue);
                                if (compareInt != 0) {
                                    compareInt /= Math.abs(event.asCharacters().getData().compareToIgnoreCase(stringValue));
                                }

                                if (lastColumn.equalsIgnoreCase(column) && compareInt == conditionValue) {
                                    found = true;
                                    System.out.println("found");
                                }

                            }
                        }
                    }

                }
                System.out.println("###" + event.toString());
                writer.add(event);
            }

            //writer.add(eventFactory.createEndElement("", null, "table"));
            writer.close();
            eventReader.close();
            fileWriter.close();
            fileInputStream.close();

            System.out.println("out of condition");

            File folder = new File(workingDirectory);
            File[] listOfFiles = folder.listFiles();

            for (File f : listOfFiles) {
                if (f.isFile()) {
                    String name = f.getName();
                    if (name.equals(tableName + ".xml")) {
                        System.out.println("deleted? " + f.delete());
                    }
                }
            }

            for (File f : listOfFiles) {
                if (f.isFile()) {
                    String name = f.getName();
                    if (name.equals("temp.xml")) {
                        System.out.println("Renamed? " + f.renameTo(new File(file)));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return counter;
        }

        return counter;
    }

    public String generateXSD(String tableName, String[] columnNames) {
        String Start = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "   <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">\n"
                + "         <!-- XML Schema Generated from XML Document on Mon Apr 24 2017 20:13:27 GMT+0200 (Egypt Standard Time) -->\n"
                + "         <!-- with XmlGrid.net Free Online Service http://xmlgrid.net -->\n"
                + "         <xs:element name=\"table\">\n"
                + "               <xs:complexType>\n"
                + "                     <xs:sequence>\n"
                + "                           <xs:element name=\"row\" maxOccurs=\"unbounded\">\n"
                + "                                 <xs:complexType>\n"
                + "                                       <xs:sequence>";
        for (String column : columnNames) {
            Start += "                                             <xs:element name=\"" + column + "\" type=\"xs:string\"></xs:element>";
        }
        return Start += " </xs:sequence>\n"
                + "                                 </xs:complexType>\n"
                + "                           </xs:element>\n"
                + "                     </xs:sequence>\n"
                + "               </xs:complexType>\n"
                + "         </xs:element>\n"
                + "   </xs:schema>";
    }
}
/*Validating
DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
domFactory.setValidating(true);
DocumentBuilder builder = domFactory.newDocumentBuilder();
builder.setErrorHandler(new ErrorHandler() {
    @Override
    public void error(SAXParseException exception) throws SAXException {
        // do something more useful in each of these handlers
        exception.printStackTrace();
    }
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        exception.printStackTrace();
    }
});
Document doc = builder.parse("employee.xml");*/
//listOfLists.add((ArrayList<String>)row.clone());
