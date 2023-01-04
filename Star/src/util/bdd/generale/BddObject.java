package util.bdd.generale;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Date;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import util.bdd.connection.ConnectionBase;



public class BddObject {
    String tabName;
    String primaryKey;
    String prefixe="OBJ";
    int longPK=7;
    String nomFonction="getseq";
    

    
/* CONSTRUCTOR */
        public BddObject(String tabName, String primaryKey) {
            set_tabName(tabName);
            set_primaryKey(primaryKey);
        }
    


/* The get and set method of the object */    
    
    //Get general
        public Object get(Object object, String field) throws Exception {
            String m = "get_"+field;
            Method method = object.getClass().getDeclaredMethod(m, new Class[0]);
            return method.invoke(object, new Object[0]);        
        }

        
        

    //GET
        public String get_tabName() {return tabName;}
        public String get_primaryKey() {return primaryKey;}
        public String get_prefixe() {return prefixe;}
        public int get_longPK() {return longPK;}
        public String get_nomFonction() {return nomFonction;}    
        
        
    //SET    
        public void set_tabName(String tabName) {
            this.tabName=tabName;
        }    
        public void set_primaryKey(String primaryKey) {
            this.primaryKey=primaryKey;
        }    
        public void set_prefixe(String prefixe) {
            this.prefixe=prefixe;
        }
        public void set_longPK(int longPK) {
            this.longPK=longPK;
        }
        public void set_nomFonction(String nomFonction) {
            this.nomFonction=nomFonction;
        }
        


/* GetNextSeq */
    //get sequence    
        public int getNextSeq(ConnectionBase cb) throws Exception {
            boolean wasConnected = true;
            int result = 0;
            if (cb.get_co() == null) {
                cb.preparingConnection();
                wasConnected = false;
            }    
            try {
                String request = "select " + get_tabName() + "seq.nextval from dual";
                ResultSet rs = cb.get_stmt().executeQuery(request);
                rs.next();
                result = rs.getInt(1);
            } catch (Exception e) {
                throw e;   
            } finally {
                if (!wasConnected) { cb.closeConnection(); }
            }    
            return result;
        }    

    //get sequence    
        public int getNextSeq2(ConnectionBase cb) throws Exception {
            boolean wasConnected = true;
            int result = 0;
            if (cb.get_co() == null) {
                cb.preparingConnection();
                wasConnected = false;
            }    
            try {
                String request = "select " + get_nomFonction() + " from dual";
//                System.out.println(request);
                ResultSet rs = cb.get_stmt().executeQuery(request);
                rs.next();
                result = rs.getInt(1);
            } catch (Exception e) {
                throw e;   
            } finally {
                if (!wasConnected) { cb.closeConnection(); }
            }    
            return result;
        }    
        

    //Completer 0
        public String completer0(int seq) {
            String result="";
            int longPK = get_longPK();
            int longpref = get_prefixe().length();
            String seqString = String.valueOf(seq);
            int long0 = longPK-longpref-seqString.length();
            for (int i=0; i < long0; i++) {
                result = result.concat("0");
            }
            return result.concat(seqString);
        }
        

    //Construction de la seq
        public String constructPK(ConnectionBase cb) throws Exception{
            int seq = getNextSeq2(cb);
            return get_prefixe().concat(completer0(seq));
        }
        
/* Method for interaction with database */

    //Insert Object into table    
        public void insert(ConnectionBase cb) throws Exception {
            //is connection was etablished
            boolean wasConnected = true; 
            
            // open connection with DB if wasn't connected 
            if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
            
            try {
                // get the request
                String request = get_requestInsert();
//                 System.out.println(request);
                // execute the request
                cb.get_stmt().executeUpdate(request);
                
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {

            // close connection with DB if wasn't connected    
            if (!wasConnected) {cb.closeConnection();}
            }
        }    

    //get object from table with a specific field
    public Vector<BddObject> select(ConnectionBase cb, String field, Object val) throws Exception {
        return select(cb, field, "=", val);
    }

    public Vector<BddObject> select(ConnectionBase cb, Object primaryKey) throws Exception {
        return select(cb, get_primaryKey(), primaryKey);
    }

    
    public Vector<BddObject> select(ConnectionBase cb, String field, String filtre, Object val) throws Exception {
        Vector<BddObject> result = new Vector<BddObject>();
        Constructor<?> constructor = get_constructor();
        Class<?>[] classes = get_classObject();
        String[] fields = new String[classes.length]; 
        int countField = fields.length;
        for (int i = 0; i < classes.length; i++) {
            fields[i] = classes[i].getSimpleName();
        }    

        //is connection established
        boolean wasConnected = true; 

        // open connection with DB if wasn't connected 
        if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
        
        try {
            // get the request
            String request = "select * from "+tabName+ " where "+field+filtre+verifValues(val);

            // execute the request
            // System.out.println(request);
            ResultSet rs = cb.get_stmt().executeQuery(request);

            //Transform the resultSet to Object
            while (rs.next()) {
                // The value of the parameter object
                Object[] parameterObject = new Object[countField];
                for (int i = 0; i < fields.length; i++) {
                    parameterObject[i] = get_resultSetMethod(fields[i]).invoke(rs, i+1);
                }    

                //Add the new Object in the return value
                result.add(((BddObject)constructor.newInstance(parameterObject)));
            }    

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        } finally {

            // close connection with DB if wasn't connected
            if (!wasConnected) {cb.closeConnection();}
        }    


        return result;
    }


    public Vector<BddObject> select(ConnectionBase cb, String[] field, String[] filtre, Object[] val) throws Exception {
        Vector<BddObject> result = new Vector<BddObject>();
        Constructor<?> constructor = get_constructor();
        Class<?>[] classes = get_classObject();
        String[] fields = new String[classes.length]; 
        int countField = fields.length;
        for (int i = 0; i < classes.length; i++) {
            fields[i] = classes[i].getSimpleName();
        }    

        //is connection established
        boolean wasConnected = true; 

        // open connection with DB if wasn't connected 
        if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
        
        try {
            // get the request
            String request = "select * from "+tabName+ " where "+field[0]+filtre[0]+verifValues(val[0]);
            for (int i = 1; i < field.length; i++) {
                request = request.concat(" and "+field[i]+filtre[i]+verifValues(val[i]));
            }

            // execute the request
            // System.out.println(request);
            ResultSet rs = cb.get_stmt().executeQuery(request);

            //Transform the resultSet to Object
            while (rs.next()) {
                // The value of the parameter object
                Object[] parameterObject = new Object[countField];
                for (int i = 0; i < fields.length; i++) {
                    parameterObject[i] = get_resultSetMethod(fields[i]).invoke(rs, i+1);
                }    

                //Add the new Object in the return value
                result.add(((BddObject)constructor.newInstance(parameterObject)));
            }    

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        } finally {

            // close connection with DB if wasn't connected
            if (!wasConnected) {cb.closeConnection();}
        }    


        return result;
    }

    
        
    //get all object in table    
    public Vector<BddObject> select(ConnectionBase cb) throws Exception {
        Vector<BddObject> result = new Vector<BddObject>();
        Constructor<?> constructor = get_constructor();
        Class<?>[] classes = get_classObject();
        String[] fields = new String[classes.length]; 
        int countField = fields.length;
        for (int i = 0; i < classes.length; i++) {
            fields[i] = classes[i].getSimpleName();
        }    
        
        //is connection etablished
        boolean wasConnected = true; 

        // open connection with DB if wasn't connected 
        if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
        
        try {
            // get the request
            String request = "select * from "+tabName;

            // execute the request
            // System.out.println(request);
            ResultSet rs = cb.get_stmt().executeQuery(request);

            //Transform the resultSet to Object
            while (rs.next()) {
                // The value of the parameter object
                Object[] parameterObject = new Object[countField];
                for (int i = 0; i < fields.length; i++) {
                    parameterObject[i] = get_resultSetMethod(fields[i]).invoke(rs, i+1);
                }    

                //Add the new Object in the return value
                result.add(((BddObject)constructor.newInstance(parameterObject)));
            }    

        } catch (Exception e) {

            throw e;
        } finally {

            // close connection with DB if wasn't connected
            if (!wasConnected) {cb.closeConnection();}
        }    


        return result;
    }    


        
    //delete object from table    
    public void delete(ConnectionBase cb, Object primaryKey) throws Exception {
        //is connection established
        boolean wasConnected = true; 

        // open connection with DB if wasn't connected 
        if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
        
        try {
            // get the request
            String request = get_deleteRequest(primaryKey);

            // execute the request
            // System.out.println(request);
            cb.get_stmt().executeUpdate(request);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {

            // close connection with DB if wasn't connected
            if (!wasConnected) {cb.closeConnection();}
        }    
    } 
    
    

    //update object from table
        public void update(ConnectionBase cb, Object primaryKey, String colName, Object updated) throws Exception {
            //is connection established
            boolean wasConnected = true; 

            // open connection with DB if wasn't connected 
            if (cb.get_co()==null) { wasConnected = false; cb.preparingConnection(); }        
            
            try {
                // get the request
                String request = get_updateRequest(primaryKey, colName, updated);

                // execute the request
                // System.out.println(request);
                cb.get_stmt().executeUpdate(request);

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {

                // close connection with DB if wasn't connected
                if (!wasConnected) {cb.closeConnection();}
            }   
        }






    


//Get the getMethod of the class ex:getInt, getString ...
    public Method get_resultSetMethod(String classes) throws Exception {
        char[] classesChar = classes.toCharArray();
        classesChar[0] = Character.toUpperCase(classesChar[0]);
        String methodName = "get".concat(String.valueOf(classesChar));
        Method result = ResultSet.class.getDeclaredMethod(methodName, int.class);
        return result;
    } 




//Get ConstructorObject
    public Constructor<?> get_constructor() throws Exception {
        Class<?>[] parameterType = get_classObject();
        Constructor<?> result = getClass().getConstructor(parameterType);
        return result;
    }







//Get field that you want
    public Object get(String field) throws Exception{
    	String part1 = field.substring(0, 1);
    	String part2 = field.substring(1);
    	part1 = part1.toUpperCase();
    	field = part1.concat(part2);
        String m = "get"+field;
        try {
            Method method = getClass().getDeclaredMethod(m, new Class[0]);
            return method.invoke(this, new Object[0]);        
        } catch (Exception e) {
            throw e;
        }
    }
    







//set form values(prepare la valeur a inserer)
    public String verifValues(Object object) {
        //initializing of the result
        String values = ""; 

        //Concat for String values
        if (object instanceof String) { values = values.concat("'"+String.valueOf(object)+"'"); }
                    
        //Concat for Timestamp values
        else if(object instanceof Timestamp) {
        	values = values.concat("TIMESTAMP '"+((Timestamp)object).toString()+"'");
        }
        
        //Concat for Date values
        else if (object instanceof Date) { 
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            values = values.concat("to_date('"+dateFormat.format((Date)object)+"','yyyy-MM-dd')");
        }

        //Concat for number values
        else {values = values.concat(String.valueOf(object));}
        
        return values;
    }




//prepare element values of the request insert
    public String get_elementValues() throws Exception {
        try {
            //initializing of the result
            String values = ""; 
        
            //get the declared field of the object
            String[] fields = get_fieldObject();
        
            for (int i = 0; i < fields.length; i++) {
           
                //Add the values terms
                String value = verifValues(get(fields[i]));
                values = values.concat(value);
            
                //Add a separator
                if (i!=fields.length-1) {values = values.concat(",");}
            }

            return values;

        } catch (Exception e) {
//            System.out.println("get_elementValue:"+e);
            throw e;
        }

    }


//Prepare the request delete
    public String get_deleteRequest(Object primaryKey) {
        String result = "delete from "+ get_tabName() + " where " + get_primaryKey() + "=" + verifValues(primaryKey);
        return result;
    }




//Prepare the request insert
    public String get_requestInsert() throws Exception{
        try {
            //initializing of result
            String request = "";
    
            request = request.concat("insert into "+ get_tabName() +" values ("); 
    
            //Add values 
            request = request.concat(get_elementValues());
            
            request = request.concat(")");
            return request;
        
        } catch (Exception e) {
            throw e;
        }
    }


//Prepare the request update
    public String get_updateRequest(Object primaryKey, String colName, Object updated) {
        String request = "update "+get_tabName()+" set "+colName+"="+verifValues(updated)+ " where "+get_primaryKey()+"="+verifValues(primaryKey);
        return request;
    }


//Get the field attributed to the object
    public String[] get_fieldObject() {
        Field[] f = getClass().getDeclaredFields();
        String[] result = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            result[i] = f[i].getName().split("\\.")[f[i].getName().split("\\.").length-1];       
        }
        return result;
    }




//Get the declared field of the object
    public Class<?>[] get_classObject() {
        Field[] fields = getClass().getDeclaredFields();
        Class<?>[] classes = new Class<?>[fields.length];
        for (int i = 0; i < fields.length; i++) {
            classes[i] = fields[i].getType();
        }
        return classes;
    }


    public void displayBddObject() {
        try {
            String[] str = get_fieldObject();
            for (int i = 0; i < str.length ; i++) {
                System.out.print(str[i]+":");
                System.out.println(get(str[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    public String get_valeur() {
        String result = "";
        try {
            String[] field = get_fieldObject();
            for(int i=0; i<field.length; i++) {
                result = result.concat(field[i]+":"+get(field[i])+";");
            }
            return result;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    
    public static void main(String[] args) {
    	Timestamp t = new Timestamp(new Date().getTime());
    	System.out.println(t.toString());
    }
}