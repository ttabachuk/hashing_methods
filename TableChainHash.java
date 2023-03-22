// File: Table.java
// Complete documentation is available from the Table link in:
//   http://www.cs.colorado.edu/~main/docs


/******************************************************************************
* A <CODE>Table</CODE> is an open-address hash table with a fixed capacity.
* The purpose is to show students how an open-address hash table is
* implemented. Programs should generally use java.util.Hashtable
* rather than this hash table.
*
* <dt><b>Java Source Code for this class:</b><dd>
*   <A HREF="../../../../edu/colorado/collections/Table.java">
*   http://www.cs.colorado.edu/~main/edu/colorado/collections/Table.java
*   </A>
*
* @author Michael Main, Ammaad Denmark, Tim Tabachuk
*   <A HREF="mailto:main@colorado.edu"> (main@colorado.edu) </A>
*
******************************************************************************/
public class TableChainHash< K , E >
{
   // Invariant of the Table class:
   //   1. The number of items in the table is in the instance variable manyItems.
   //   2. The preferred location for an element with a given key is at index
   //      hash(key). If a collision occurs, then next-Index is used to search
   //      forward to find the next open address. When an open address is found
   //      at an index i, then the element itself is placed in data[i] and the
   //      element�s key is placed at keys[i].
   //   3. An index i that is not currently used has data[i] and key[i] set to
   //      null.
   //   4. If an index i has been used at some point (now or in the past), then
   //      hasBeenUsed[i] is true; otherwise it is false.
   private int manyItems;
   private boolean[ ] hasBeenUsed;

   private Node<E>[] table;
   private Node<K>[] keys;
   private int collisions;

   /**
   * Initialize an empty table with a specified capacity.
   * @param <CODE>capacity</CODE>
   *   the capacity for this new open-address hash table
   * <dt><b>Postcondition:</b><dd>
   *   This table is empty and has the specified capacity.
   * @exception OutOfMemoryError
   *   Indicates insufficient memory for the specified capacity. 
   **/   
   public TableChainHash(int capacity)
   {
      // The manyItems instance variable is automatically set to zero.
      // which is the correct initial value. The three arrays are allocated to
      // be the specified capacity. The boolean array is automatically
      // initialized to falses, and the other two arrays are automatically
      // initialized to all null.
      if (capacity <= 0)
         throw new IllegalArgumentException("Capacity is negative");
         table = (Node<E>[]) new Node[capacity];
         keys = (Node<K>[]) new Node[capacity];
         hasBeenUsed = new boolean[capacity];
   }
   
   
   /**
   * Determines whether a specified key is in this table.
   * @param <CODE>key</CODE>
   *   the non-null key to look for
   * <dt><b>Precondition:</b><dd>
   *   <CODE>key</CODE> cannot be null.
   * @return
   *   <CODE>true</CODE? (if this table contains an object with the specified 
   *   key); <CODE>false</CODE> otherwise. Note that <CODE>key.equals( )</CODE>
   *   is used to compare the <CODE>key</CODE> to the keys that are in the 
   *   table.
   * @exception NullPointerException
   *   Indicates that <CODE>key</CODE> is null.
   **/
   public boolean containsKey(K key)
   {
      return findIndex(key) != -1;
   }
   
   
   private int findIndex(K key)
   // Postcondition: If the specified key is found in the table, then the return
   // value is the index of the specified key. Otherwise, the return value is -1.
   {
      int count = 0;
      int i = hash(key);
      
      while (count < table.length && hasBeenUsed[i])
      {
         if (key.equals(keys[i]))
            return i;
         count++;
         i = nextIndex(i);
      }
      
      return -1;
   }
      
   
   /** Retrieves an object for a specified key.
   * @param <CODE>key</CODE>
   *   the non-null key to look for
   * <dt><b>Precondition:</b><dd>
   *   <CODE>key</CODE> cannot be null.
   * @return
   *   a reference to the object with the specified <CODE>key</CODE (if this 
   *   table contains an such an object);  null otherwise. Note that 
   *   <CODE>key.equals( )</CODE> is used to compare the <CODE>key</CODE>
   *   to the keys that are in the table.
   * @exception NullPointerException
   *   Indicates that <CODE>key</CODE> is null.
   **/
   public E get(K key)
   {
      int index = hash(key);
      // cursor nodes to find our desired data
      Node<E> tableCursor = null;
      Node<K> keyCursor = null;
      
      if (keys[index] == null)
         return null;
      else {
         // create cursor nodes to find where to place new Node
         tableCursor = table[index];
         keyCursor = keys[index];

         while(!keyCursor.getData().equals(key)) {
            tableCursor = tableCursor.getLink();
            keyCursor = keyCursor.getLink();
         }
         return tableCursor.getData();
      }
   }
   
   
   private int hash(Object key)
   // The return value is a valid index of the table�s arrays. The index is
   // calculated as the remainder when the absolute value of the key�s
   // hash code is divided by the size of the table�s arrays.
   {
      return Math.abs(key.hashCode( )) % table.length;
   }
   
   
   private int nextIndex(int i)
   // The return value is normally i+1. But if i+1 is data.length, then the 
   // return value is zero instead.
   {
      if (i+1 == table.length)
         return 0;
      else
         return i+1;
   }
   
   
   /**
   * Add a new element to this table, using the specified key.
   * @param <CODE>key</CODE>
   *   the non-null key to use for the new element
   * @param <CODE>element</CODE>
   *   the new element that�s being added to this table
   * <dt><b>Precondition:</b><dd>
   *   If there is not already an element with the specified <CODE>key</CODE>,
   *   then this table�s size must be less than its capacity 
   *   (i.e., <CODE>size() < capacity()</CODE>). Also, neither <CODE>key</CODE>
   *   nor </CODE>element</CODE> is null.
   * <dt><b>Postcondition:</b><dd>
   *   If this table already has an object with the specified <CODE>key</CODE>,
   *   then that object is replaced by </CODE>element</CODE>, and the return 
   *   value is a reference to the replaced object. Otherwise, the new 
   *   </CODE>element</CODE> is added with the specified <CODE>key</CODE>
   *   and the return value is null.
   * @exception IllegalStateException
   *   Indicates that there is no room for a new object in this table.
   * @exception NullPointerException
   *   Indicates that <CODE>key</CODE> or <CODE>element</CODE> is null.   
   **/
   public E put(K key, E element)
   {  
      collisions = 0;
      Node<E> tableCursor = null;
      Node<K> keyCursor = null;
      Node<E> dataNode = new Node<E>(element, null);
      Node<K> keyNode = new Node<K>(key, null);
      
      int index = findIndex(key);
      E answer = null;
      
      if (key == null || element == null) {
         System.out.println("Key or Element is null");
         return null;
      }
      if (manyItems < table.length) {
         // The key is not yet in this Table.
         index = hash(key);
         // the current indexes have nodes
         if(table[index] != null) {
            // create cursor nodes to find where to place new Node
            tableCursor = table[index];
            keyCursor = keys[index];
            while (tableCursor != null) {
               collisions++;
               tableCursor = tableCursor.getLink();
               keyCursor = keyCursor.getLink();
            }
         }
         else {
            tableCursor = null;
            keyCursor = null;
         }

         if (tableCursor == null) {
            // add a node at the front of the list of table
            index = hash(key);
            tableCursor = new Node<E>(null, null);
            keyCursor = new Node<K>(null, null);
            tableCursor.setData(element);
            keyCursor.setData(key);
            tableCursor.setLink(table[index]);
            keyCursor.setLink(keys[index]);
            table[index] = tableCursor;
            keys[index] = keyCursor;
         }
         else {
            // new element replaces old
            answer = tableCursor.getData();
            tableCursor.setData(element);
         }
         return answer;
      }
      else
      {  // The table is full.
         throw new IllegalStateException("Table is full.");
      }

   }
   
   /**
   * Removes an object for a specified key.
   * @param <CODE>key</CODE>
   *   the non-null key to look for
   * <dt><b>Precondition:</b><dd>
   *   <CODE>key</CODE> cannot be null.
   * <dt><b>Postcondition:</b><dd>
   *   If an object was found with the specified </CODE>key</CODE>, then that
   *   object has been removed from this table and a copy of the removed object
   *   is returned; otherwise, this table is unchanged and the null reference
   *   is returned.  Note that 
   *   <CODE>key.equals( )</CODE> is used to compare the <CODE>key</CODE>
   *   to the keys that are in the table.
   * @exception NullPointerException
   *   Indicates that </CODE>key</CODE> is null.
   **/
   public E remove(K key)
   {
      int index = hash(key);

      // Create a parent and current cursor for key and table nodes
      Node<E> tableParent = null;
      Node<K> keyParent = null;
      Node<E> tableCursor = null;
      Node<K> keyCursor = null;

      if(keys[index] == null) {
         return null;
      }
      else {
         // search for the given key;
         tableCursor = table[index];
         keyCursor = keys[index];

         while(keyCursor != null) {
            if (keyCursor.getData().equals(key)) {
               if (keyParent != null) {
                  // remove the desired key
                  keyParent.setLink(keyCursor.getLink());
                  tableParent.setLink(tableCursor.getLink());
               } 
               else {
                  table[index] = tableParent;
                  keys[index] = keyParent;
               }
               manyItems--;
               return tableCursor.getData();
            }
            else {
               // update the parent cursors
               tableParent = tableCursor;
               keyParent = keyCursor;
               tableCursor = tableCursor.getLink();
               keyCursor = keyCursor.getLink();
            }
         }
      }
      return null;
   }
   /**
   * returns the number of collisions from put method.
   * @param
   *     none
   * @return
   *     collisions - integer storing number of collisions 
   *                   from placing current element
   **/
   public int getCollisions() {
      return collisions;
   }
        
}
           
