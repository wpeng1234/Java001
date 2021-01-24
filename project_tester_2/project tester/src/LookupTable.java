import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class LookupTable
{ 
   private ArrayList<Item> Item_key;
   //private ArrayList<Item> Price_key;


   public LookupTable()
   {
	 
	   Item_key = new ArrayList<Item>();      
           
   }

   public void read(Scanner in) {	   	  
      	 

	   String ItemName = null, ItemPrice = null;
     
	  while(in.hasNextLine()) {
			 ItemName = in.nextLine();
			 ItemPrice = in.nextLine();
			 Item_key.add(new Item(ItemName,ItemPrice));

		 }
          Collections.sort(Item_key);
	  }
   
 
  public void ListMenu()
  {
      int MenuQty = Item_key.size();
      for(int i = 0; i < MenuQty - 1; i++)
      {
          String key = Item_key.get(i).getKey();
          int n = 60 - key.length();
          String str="";
          for (int j = 0; j < n; j++) {
              str+=" ";
          }
          System.out.println(Item_key.get(i).getKey() + str + Item_key.get(i).getValue());
      }
  }
   public double lookup_by_item(String n)
   {       
	   Collections.sort(Item_key);	   

	   int index_found = Collections.binarySearch(Item_key, new Item(n, null)); //passing in item key which is n 
//           System.out.println(index_found);
           index_found = (-1 * index_found) - 1;
	   //System.out.println("index found = " + " " + index_found); //show if the search works
	   
	   double item=0;

	   if(index_found >= 0) {


           item = Double.valueOf(Item_key.get(index_found).getValue());
           //	  itemn = Item_key.get(index_found).getValue();

		    
   }
	return item;
           
   
   }


   }


