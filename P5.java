import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         // System.out.println("Program parsed successfully");
         GJDepthFirst<String,Integer> head = new GJDepthFirst<String,Integer>();
         root.accept(head, 0); // Your assignment part is invoked here.
         // root.accept(head, 1);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 


