package br.com.zanthus;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import veridis.biometric.BiometricIdentification;
import veridis.biometric.BiometricTemplate;
import veridis.biometric.samples.applet.Base64;



public class dbAccess {
	
	protected static Connection con = null;
	protected static Statement stm = null;
	protected static int minimumThreshold = 40;
	
	/*Function to add template to database*/
	public static boolean AddTemplate(BiometricTemplate template, String id) throws SQLException{
		try{
			if(con == null)
				connectToDB();
			if(stm == null)
				stm = con.createStatement();
		
			String query = "INSERT INTO cadastro  VALUES (\""+id+"\",'"+Base64.encodeBytes(template.getData())+"')";
			stm.execute(query);
			
			return true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}

	
	}
	/*Function to get Template from database*/
	public static BiometricTemplate getTemplate(BiometricTemplate template, String id) throws SQLException{

		if(con == null)
			connectToDB();
		if(stm == null)
			stm = con.createStatement();
		

		String query = "SELECT * FROM cadastro where id = \""+id+"\"";
		

		ResultSet res = stm.executeQuery(query); 
		if(res.next()){		
			try{
				
				BiometricTemplate tempBD = new BiometricTemplate((Base64.decode(res.getString("template"))));
	
				return tempBD;
			}
			catch(Exception e ){
				System.out.println(e.getLocalizedMessage());
				return null;
			}
		}
		return null;
	}
	
	/*Function to verify if the given template matches with the template corresponding to the given id*/
	public static int verificaIdentificador(BiometricTemplate template, String id) throws SQLException{

		if(con == null)
			connectToDB();
		if(stm == null)
			stm = con.createStatement();
		

		String query = "SELECT * FROM cadastro where id = \""+id+"\"";
		

		ResultSet res = stm.executeQuery(query); 
		if(res.next()){		
			try{
				
				BiometricTemplate tempBD = new BiometricTemplate((Base64.decode(res.getString("template"))));
	
				if (tempBD.match(template) > minimumThreshold)
					return 1;
			}
			catch(Exception e ){
				System.out.println(e.getLocalizedMessage());
				return -1;
			}
		}
		return 0;
	}
	
	/*Given a ID, tries to find correspondent template*/
	public static String  findIDTemplate(BiometricTemplate template) throws SQLException{
		
		
		if(con == null)
			connectToDB();
		if(stm == null)
			stm = con.createStatement();
		
		String query = "SELECT * FROM cadastro";
		ResultSet res = stm.executeQuery(query); 


		/*prepares identification context to perform match faster*/
		BiometricIdentification temp = new BiometricIdentification(template);
		
		while(res.next()){	
				try{
					//retrieves template 
					BiometricTemplate tempBD = new BiometricTemplate((Base64.decode(res.getString("template"))));
		
					//checks if match score is higher than minimum threshold					
					if( temp.match(tempBD) > 40)
						return res.getString("id");

				}
				catch(Exception e ){
					System.out.println(e.getLocalizedMessage());
					return null;
				}
			}
		
		return null;
		
	}
	
	public static void connectToDB(){
		try{ 
		 Class.forName("org.sqlite.JDBC");
		
		 con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("java.io.tmpdir") + "banco.db");
		 stm = con.createStatement();
		 stm.executeUpdate("DROP TABLE IF EXISTS cadastro");
		 stm.executeUpdate("CREATE TABLE cadastro (" +
				 "id varchar(100) PRIMARY KEY NOT NULL," +
				 "template varchar(200) NOT NULL);");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	
	}

}