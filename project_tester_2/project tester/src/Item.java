/**
   An item with a key and a value.
*/
public class Item implements Comparable<Item>
{
   public String key;
   public String value;

   /**
      Constructs an Item object.
      @param k the key string
      @param v the value of the item
   */
   public Item(String k, String v)
   { 
      key = k;
      value = v;
   }
   
   public String getKey()
   { 
      return key;
   }
   

   public String getValue()
   { 
      return value;
   }

   public int compareTo(Item otherObject)
   {
      Item other = (Item) otherObject;
      return key.compareTo(other.key);     //return key.compareTo(other.key);
   }
}
