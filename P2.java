import syntaxtree.*;
import visitor.*;

public class P2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         // System.out.println("Program parsed successfully");
         GJNoArguDepthFirst<String> temp = new GJNoArguDepthFirst<String>(); 
         root.accept(temp); // Your assignment part is invoked here.
         temp.init = false;
         root.accept(temp);
	System.out.println("Program type checked successfully");
      }
      catch (ParseException e) {
         //System.out.println(e.toString());
	System.out.println(e.toString());
      }
   }
} 


