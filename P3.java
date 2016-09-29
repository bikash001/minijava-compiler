import syntaxtree.*;
import visitor.*;

public class P3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         System.out.println("Program parsed successfully");
         GJNoArguDepthFirst<String> head = new GJNoArguDepthFirst<String>();
         root.accept(head); // Your assignment part is invoked here.
         head.topSort();
         head.firstTime = false;
         root.accept(head);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 


