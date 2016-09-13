import syntaxtree.*;
import visitor.*;

public class Main {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         // System.out.println("Program parsed successfully");
         GJNoArguDepthFirst<String> temp = new GJNoArguDepthFirst<String>(); 
         root.accept(temp); // Your assignment part is invoked here.
         temp.init = false;
         root.accept(temp);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 


