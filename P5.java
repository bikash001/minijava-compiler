import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         // System.out.println("Program parsed successfully");
         GJNoArguDepthFirst head = new GJNoArguDepthFirst();
         root.accept(head); // Your assignment part is invoked here.
         head.firstTime = false;
         root.accept(head);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 


