/**
 *
 * @Wuchen Peng
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CoffeeShopProject
{
   private static  int orderID=0;
   public static void main(String[] args) throws IOException
   {
      Scanner in = new Scanner(System.in);
      int count=0;
      LookupTable table = new LookupTable();
      FileReader reader = new FileReader("Coffee Menu.txt");
      table.read(new Scanner(reader));
      //System.out.println(table.);
      
      System.out.println("***************************************");
      System.out.println("******* MIS Coffee Shop Program *******");
      System.out.println("***************************************");
      
      boolean more = true;
      while (more)
      {  
    	 System.out.println();
         System.out.println("(V)iew Menu"); // print out from the text file
         System.out.println("(I)tem price lookup"); // look up by item method
         System.out.println("(P)lace Order"); //create table using derby.java and put new orders in it with automatic generated order ID
         System.out.println("(Q)uit?");
         String cmd = in.nextLine();
           
         if (cmd.equalsIgnoreCase("Q"))
            more = false;
         else if (cmd.equalsIgnoreCase("I"))
         { 
            System.out.println("Enter item name to get price:");
            String n = in.nextLine();
            System.out.println( table.lookup_by_item(n) + "\n");
         }
         else if (cmd.equalsIgnoreCase("V"))
         {                         
             table.ListMenu();
         }
         else if (cmd.equalsIgnoreCase("P")) {
            List<OrdersBean> ordersBeans=new ArrayList<>();
            OrdersBean ordersBean=new OrdersBean();
            double totalprice=0;
            double price=0;
            StringBuilder items=new StringBuilder();
            while (true){
               if (count==0) {
                  System.out.println("Please enter order name:");
               }else {
                  System.out.println("Please enter order item or enter \"n\" to finish chect-out");
               }
               Scanner scanner = new Scanner(System.in);
               String orderName = scanner.nextLine();
               if ("n".equalsIgnoreCase(orderName)) {
                  String s = items.toString();
                  if (s.endsWith(",")) {
                     s = s.substring(0, s.length() - 1);
                  }
                  System.out.println("Please enter Barista ID:");
                  int BaristaID = scanner.nextInt();
                  SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                  ordersBean.setOrderPrice(totalprice);
                  ordersBean.setOrderItems("\""+s+"\"");
                  ordersBean.setBaristaID(BaristaID);
                  ordersBean.setTime(sdf.format(new Date()));

                  ordersBeans.add(ordersBean);
                  PointsToCsvFile(ordersBeans);
                  return;
               }
               System.out.println("Please enter quantity:");
               int quantity = scanner.nextInt();
               items.append(orderName+"*"+quantity);
               items.append(",");
               price=table.lookup_by_item(orderName);
               totalprice=totalprice+price * quantity;
               count++;
            }

         }
      
      }
     
   }

   public static boolean readCSV(File file) {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String line = null;
         while ((line = reader.readLine()) != null) {
            orderID++;
         }
         if (orderID>0) {
            return true;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public static void PointsToCsvFile(List<OrdersBean> ordersBeans){
      if (ordersBeans!=null && ordersBeans.size() > 0){
         
         String[] headArr = new String[]{"OrderID:","BaristaID","OrderItems","Order Price","Time"};
         
         LocalDateTime localDateTime = LocalDateTime.now();
         DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
         String filePath = "CsvDirectory"; 
         String fileName = "CSV_"+ df.format(localDateTime) +".csv";
         File csvFile = null;
         BufferedWriter csvWriter = null;
         try {
            csvFile = new File(filePath + File.separator + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
               parent.mkdirs();
            }
            csvFile.createNewFile();

            
            csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile,true), "UTF-8"), 1024);

            if (!readCSV(csvFile)) {
               
               csvWriter.write(String.join(",", headArr));
               csvWriter.newLine();
               orderID=1;
            }

            for (OrdersBean ordersBean : ordersBeans) {
               ordersBean.setOrderID(orderID);
               insertDB(ordersBean);
               csvWriter.write(ordersBean.toString());
               csvWriter.newLine();
            }
            csvWriter.flush();
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            try {
               csvWriter.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }


   public static void insertDB(OrdersBean ordersBean) {
      try {
         SimpleDataSource.init("database.properties");
         Connection connection = SimpleDataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement("INSERT INTO Orders VALUES(?,?,?,?,?)");
         try {
            PreparedStatement statement1 = connection.prepareStatement("DROP TABLE Orders");
            statement1.execute();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         PreparedStatement statement2 = connection.prepareStatement("CREATE TABLE Orders (OrderID VARCHAR(50),BaristaID VARCHAR(50),OrderItems VARCHAR(50),OrderPrice VARCHAR(50),Time VARCHAR(50))");
         statement2.execute();
            int orderID = ordersBean.getOrderID();
            int baristaID = ordersBean.getBaristaID();
            String orderItems = ordersBean.getOrderItems();
            double orderPrice = ordersBean.getOrderPrice();
            String time = ordersBean.getTime();
         statement.setInt(1,orderID);
         statement.setInt(2,baristaID);
         statement.setString(3,orderItems);
         statement.setDouble(4,orderPrice);
         statement.setString(5,time);
         statement.execute();
         PreparedStatement statement3 = connection.prepareStatement("select * from Orders");
         ResultSet resultSet = statement3.executeQuery();
         while (resultSet.next()) {
            String string = resultSet.getString(1);
            System.out.println("OrderID = " + string);
            String string1 = resultSet.getString(2);
            System.out.println("BaristaID = " + string1);
            String string2 = resultSet.getString(3);
            System.out.println("OrderItems = " + string2);
            String string3 = resultSet.getString(4);
            System.out.println("Order Price = " + string3);
            String string4 = resultSet.getString(5);
            System.out.println("Time = " + string4);
         }
      }catch (Exception e){
         e.printStackTrace();
      }
   }

   public static List<OrdersBean> readCSVForDb(File file) {
      List<OrdersBean> ordersBeans=new ArrayList<>();
      try {
         int count=0;
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String line = null;
         while ((line = reader.readLine()) != null) {

            String item[] = line.split(",");

            String last = item[item.length - 1];
            if (count>0) {
               OrdersBean ordersBean = new OrdersBean();
               ordersBean.setOrderID(Integer.parseInt(item[1]));
               ordersBean.setBaristaID(Integer.parseInt(item[2]));
               ordersBean.setOrderItems(item[3]);
               ordersBean.setOrderPrice(Double.valueOf(item[4]));
               ordersBean.setTime(item[5]);
               ordersBeans.add(ordersBean);
            }
            count++;
         }

      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      return ordersBeans;
   }


}

