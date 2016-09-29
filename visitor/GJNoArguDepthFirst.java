//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class GJNoArguDepthFirst<R> implements GJNoArguVisitor<R> {

	private String className, functionName;
	private HashMap<String,ClassMeta> classList;
	private HashMap<String,ArrayList<String>> classDep;
	private HashMap<String,ObData> stackFrame;
	private int TEMP_VALUE;
	private int LABEL_VALUE;
	public boolean firstTime;
	private int fPmCount;
	private String args;
	private int paramCount;
	private boolean idReq, printExp;
	private String fCall, nwClass;

	private void print(String str) {
		System.out.print(str);
	}

	private void println(String str) {
		System.out.println(str);
	}

	public GJNoArguDepthFirst() {
		LABEL_VALUE = 0;
		TEMP_VALUE = 20;
		fCall = "";
		nwClass = "";
    printExp = false;
		paramCount = 0;
		idReq = false;
		firstTime = true;
		args = "";
		className = "";
		functionName = "";
		stackFrame = new HashMap<String,ObData>();
		classList = new HashMap<String,ClassMeta>();
		classDep = new HashMap<String,ArrayList<String>>();
	}
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n) {
      R _ret=null;
      if (!firstTime) {
      	print("MAIN");
      }
      n.f0.accept(this);
      idReq = true;
      n.f1.accept(this);
      idReq = false;
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      idReq = true;
      n.f11.accept(this);
      idReq = false;
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      if (!firstTime) {
      	println("\nEND");
      }
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      idReq = true;
      className = (String)n.f1.accept(this);
      idReq = false;
      if (firstTime) {
      	classList.put(className,new ClassMeta(className));
      }
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      className = "";
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      idReq = true;
      className = (String)n.f1.accept(this);
      n.f2.accept(this);
      String parent = (String)n.f3.accept(this);
      idReq = false;
      if (firstTime) {
	      classList.put(className,new ClassMeta(className, parent));
	      if (classDep.containsKey(parent)) {
	      	classDep.get(parent).add(className);
	      	classList.get(className).isChild = true;
	      } else {
	      	ArrayList<String> al = new ArrayList<String>();
	      	classList.get(className).isChild = true;
	      	al.add(className);
	      	classDep.put(parent,al);
	      }
	  }
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n) {
      R _ret=null;
      idReq = true;
      String type = (String)n.f0.accept(this);
      _ret = n.f1.accept(this);
      if (firstTime && functionName.isEmpty()) {
      	ClassMeta cm = classList.get(className);
      	cm.idField.add(className+"_"+_ret);
      	cm.idType.put(className+"_"+_ret,type);
      } else if (!firstTime && !functionName.isEmpty()) {
      	stackFrame.put((String)_ret, new ObData(type,getTemp()));
      }
      n.f2.accept(this);
      idReq = false;
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      idReq = true;
      functionName = (String)n.f2.accept(this);
      idReq = false;
      if (firstTime) {
      	classList.get(className).fnField.add(functionName);
      }
      fPmCount = 0;
      n.f3.accept(this);
      String pm = (String)n.f4.accept(this);
      if (!firstTime) {
      	print(className+"_"+functionName+" [ "+paramCount+" ]" );
      	println(" BEGIN");
      }
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      if (!firstTime) {
      	println("\nRETURN");
      }
      String xp = (String)n.f10.accept(this);
      if (!firstTime) {
        if (xp != null) {
          println(xp);
        }
      	println(" END");
      }
      n.f11.accept(this);
      n.f12.accept(this);
      functionName = "";
      stackFrame.clear();
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n) {
      R _ret=null;
      paramCount = 1;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n) {
      R _ret=null;
      idReq = true;
      String tp = (String)n.f0.accept(this);
      String id = (String)n.f1.accept(this);
      if (!firstTime) {
        stackFrame.put(id, new ObData(tp,"TEMP "+paramCount));
      }
      ++paramCount;
      idReq = false;
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n) {
      R _ret=(R)"int[]";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n) {
      R _ret=(R)"boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n) {
      R _ret=(R)"int";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n) {
      R _ret=null;
      idReq = true;
      String id = (String)n.f0.accept(this);
      idReq = false;
      if (!firstTime) {
       //  String temp = getTemp();
       //  println(" MOVE "+temp+" "+id);
      	// print(" MOVE "+temp+" ");
        print(getAddr(id));
      }
      n.f1.accept(this);
      String exp = (String)n.f2.accept(this);
      if (!firstTime && exp != null) {
        println(exp);
      }
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n) {
      R _ret=null;
      // idReq = true;
      String id = (String)n.f0.accept(this);
      // idReq = false;
      if (!firstTime) {
      	print("HSTORE PLUS PLUS 4 TIMES 4 ");
      }
      n.f1.accept(this);
      String tmp = (String)n.f2.accept(this);
      if (!firstTime) {
        if (tmp != null) {
          print(tmp);
        }
          print(" ");
          print(id+" 0 ");
      }
      n.f3.accept(this);
      n.f4.accept(this);
      String tt = (String)n.f5.accept(this);
      if (!firstTime && tt != null) {
        println(tt);
      }
      n.f6.accept(this);
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(IfthenStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
      	print(" CJUMP ");
      }
      String lb = "";
      String ex = (String)n.f2.accept(this);
      if (!firstTime) {
        if (ex != null) {
          print(ex);
        }
      	lb = getTemp();
      	println(" "+lb);
      }
      n.f3.accept(this);
      String stm = (String)n.f4.accept(this);
      if (!firstTime) {
      	println(" "+lb+" NOOP");
      }
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfthenElseStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String l1 = "", l2 = "";
      if (!firstTime) {
      	print(" CJUMP ");
      }
      String ex = (String)n.f2.accept(this);
      n.f3.accept(this);
      if (!firstTime) {
        if (ex != null) {
          print(ex);
        }
      	l1 = getLabel();
      	println(" "+l1);
      }
      String stm = (String)n.f4.accept(this);
      n.f5.accept(this);
      if (!firstTime) {
      	l2 = getLabel();
      	println(" JUMP "+l2);
      	print(l1);
      }
      String el = (String)n.f6.accept(this);
      if (!firstTime) {
      	println("\n"+l2+" NOOP");
      }
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String lb = "", l2 = "";
      if (!firstTime) {
        l2 = getLabel();
      	print("\n"+l2+" CJUMP ");
      }
      String ex = (String)n.f2.accept(this);
      n.f3.accept(this);
      if (!firstTime) {
        if (ex != null) {
          print(ex);
        }
      	lb = getLabel();
      	println(" "+lb);
      }
      String stm = (String)n.f4.accept(this);
      if (!firstTime) {
      	println("\nJUMP "+l2+"\n"+lb+" NOOP");
      }
      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
      	print(" PRINT ");
      }
      String exp = (String)n.f2.accept(this);
      if (!firstTime && exp != null) {
        println(exp);
      }
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n) {
      R _ret=null;
      String t1 = "",l2 = "",l3 = "";
      if (!firstTime) {
        // printExp = true; 
      	t1 = getTemp();
      	l2 = getLabel();
      	l3 = getLabel();
      	print(" BEGIN \nCJUMP ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	println(" "+l3);
      	print(" CJUMP ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" "+l3+"\n"+ "MOVE "+t1+
	      	" 1\n JUMP "+l2+"\n"+l3+" MOVE "+
	      	t1+" 0 \n"+l2+" NOOP\n RETURN "+t1+" END");
      // printExp = false;
	  }
	  return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n) {
      R _ret=null;
      String t1 = "", l1 = "",l2 = "",l3 = "";
      if (!firstTime) {
        // printExp = true; 
      	t1 = getTemp();
      	l1 = getLabel();
      	l2 = getLabel();
      	l3 = getLabel();
      	print(" BEGIN \nCJUMP ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	println(" "+l1);
      	print(" MOVE "+t1+" 1\n JUMP "+l2+"\n"+l1+" CJUMP ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" "+l3+"\n"+ "MOVE "+t1+" 1\n JUMP "+l2+"\n"+l3+" MOVE "+
	      	t1+" 0 \n"+l2+" NOOP\n RETURN "+t1+" END");
        // printExp = false;
	  }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n) {
      R _ret=null;
      String t1 = "", t2 = "", t3 = "";
      if (!firstTime) {
        // printExp = true; 
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" LE "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n) {
      R _ret=null;
      String t1 = "", t2 = "", t3 = "";
      if (!firstTime) { 
         // printExp = true;
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" NE "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n) {
      R _ret=null;
      String t1 = "", t2 ="", t3 = "";
      if (!firstTime) { 
        // printExp = true;
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" PLUS "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n) {
      R _ret=null;
      String t1 = "", t2 ="", t3 = "";
      if (!firstTime) { 
       // printExp = true;
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" MINUS "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n) {
      R _ret=null;
      String t1 = "", t2 ="", t3 = "";
      if (!firstTime) { 
       // printExp = true;
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" TIMES "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n) {
      R _ret=null;
      String t1 = "", t2 ="", t3 = "";
      if (!firstTime) { 
       // printExp = true;
      	t1 = getTemp();
      	t2 = getTemp();
      	t3 = getTemp();
      	print(" BEGIN \nMOVE "+t1+" ");
      }
      String p1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!firstTime) {
        if (p1 != null) {
          println(p1);
        }
      	print("\nMOVE "+t2+" ");
      }
      String p2 = (String)n.f2.accept(this);
      if (!firstTime) {
        if (p2 != null) {
          println(p2);
        }
      	println(" MOVE "+t3+" DIV "+t1+" "+t2);
      	println(" RETURN "+t3+" END");
        // printExp = false;
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n) {
      R _ret=null;
      // idReq = true;
      String t1 = "";
      String p1 = (String)n.f0.accept(this);
      // if (!firstTime) {
      // 	t1 = getTemp();
      // 	print(" BEGIN HLOAD "+t1+" "+getIdAddr(p1)+" PLUS 4 TIMES 4 ");
      // }
      // idReq = false;
      n.f1.accept(this);
      String p2 = (String)n.f2.accept(this);
      n.f3.accept(this);
      if (!firstTime) {
        t1 = getTemp();
        println("\nBEGIN\n HLOAD "+t1+" PLUS PLUS 4 TIMES 4 "+p2+" "+p1+" 0");
        // if (p2 != null) {
        //   print(" ");
        //   println(p2);
        // }
        println("RETURN "+t1+" END");
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n) {
      R _ret=null;
      String id = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if (!firstTime) {
        println(" BEGIN");
        String tem = getTemp();
        println("HLOAD "+tem+" "+id+" 0");
        println("RETURN "+tem+" END");
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n) {
      R _ret=null;
      String t1 = "", t2 ="", t3 = "";
      idReq = true;
      if (!firstTime) {
        t1 = getTemp();
        t2 = getTemp();
        t3 = getTemp();
        println(" CALL");
        print(" BEGIN\n MOVE "+t1+" ");
      }
      String cl = (String)n.f0.accept(this);
      if (!firstTime) {
        if (cl != null) {
        	if (cl.equals("TEMP 0")) {
        		println(" TEMP 0");
        	} else {
        		println(getIdAddr(cl));
        	}
        }
      	println("HLOAD "+ t2 + " "+t1+" 0");
        print("HLOAD "+ t3 + " "+t2+" ");
      }
      n.f1.accept(this);
      idReq = true;
      String fn = (String)n.f2.accept(this);
      idReq = false;
      if (!firstTime) {
        if (cl != null) {
        	if (cl.equals("TEMP 0")) {
        		println(" "+classList.get(className).getFnIndex(fn));
        	} else {
        		// println(getType(cl));
        		ClassMeta cm = classList.get(getType(cl));
        		int pm = cm.getFnIndex(fn);
        		println(" "+classList.get(getType(cl)).getFnIndex(fn));
        	}
        } else {
          println(" "+classList.get(nwClass).getFnIndex(fn));
        }
      	println(" RETURN "+t3+" END");
      	print("( "+t1+" ");

      }
      n.f3.accept(this);
      n.f4.accept(this);
      if (!firstTime) {
      	println(" )");
      }
      n.f5.accept(this);
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n) {
      R _ret=null;
      String s1 = (String)n.f0.accept(this);
      if (!firstTime && s1 != null) {
        print(" "+s1+" ");
      }
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n) {
      R _ret=null;
      n.f0.accept(this);
      _ret = n.f1.accept(this);
      if (!firstTime && _ret != null) {
        print(" "+_ret+" ");
      }
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public R visit(PrimaryExpression n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      if (!firstTime) {
      	_ret = (R)n.f0.toString();
      }
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n) {
      R _ret=(R)null;
      n.f0.accept(this);
      if (!firstTime) {
      	print(" 1 ");
      }
      return _ret;
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n) {
      R _ret=(R)null;
      if (!firstTime) {
      	print(" 0 ");
      }
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n) {
      R _ret=null;
      n.f0.accept(this);
      if (firstTime || idReq) {
      	_ret = (R)n.f0.toString();
      } else {
        _ret = (R)getIdAddr(n.f0.toString());
        // if (printExp) {
        //   print((String)_ret);
        // }
      }

      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n) {
      R _ret=(R)"TEMP 0";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String t2 = "";
      if (!firstTime) {
      	t2 = getTemp();
      	print(" BEGIN\n MOVE "+t2+" TIMES 4 ");
      }
      String exp = (String)n.f3.accept(this);
      n.f4.accept(this);
      String temp = "";
      if (!firstTime) {
        if (exp != null) {
          println(exp);
        }
	      String t1 = getTemp();
        String t3 = getTemp();
        println(" MOVE "+t3+" PLUS 4 "+t2);
      	println("\nMOVE "+t1 + " HALLOCATE "+t3+" \n RETURN "+t1+" END");
      }
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      idReq = true;
      String cl = (String)n.f1.accept(this);
      idReq = false;
      n.f2.accept(this);
      n.f3.accept(this);
      String temp = "";
      if (!firstTime) {
      	nwClass = cl;
	      String t1 = getTemp();
	      String t2 = getTemp();
	      ClassMeta cm = classList.get(cl);
	      println(" BEGIN\n MOVE "+t1+" HALLOCATE "+cm.getFnSize());
	      println("MOVE "+t2+" HALLOCATE "+cm.getFieldSize());
	      for (int i=0; i<cm.fnField.size(); i++) {
	      	println("HSTORE "+t1+" "+(i*4)+" "+cm.fnInfo.get(i)+"_"+cm.fnField.get(i));
	      }
	      println("HSTORE "+t2+" 0 "+t1);
	      println(" RETURN "+t2+" END");
	  }
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n) {
      R _ret=null;
      n.f0.accept(this);
      if (!firstTime) {
      	print(" BEGIN \n CJUMP ");
      }
      String exp = (String)n.f1.accept(this);
      if (!firstTime) {
	      // String rt;
	      String l1 = getLabel();
	      String l2 = getLabel();
	      String temp = getTemp();
	      println(" "+l1);
      	println("MOVE "+temp+ " 0");
      	println("JUMP "+l2);
      	println(" "+l1+ " MOVE "+temp+ " 1");
      	println(l2+" NOOP");
        println("RETURN "+temp+"\nEND");
      }
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n) {
      R _ret=null;
      n.f0.accept(this);
      _ret = (R)n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public R visit(IdentifierList n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public R visit(IdentifierRest n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   // public String getIdAddrClass(String cl, String id) {
   // 		String t1 = getTemp();
   // 			println("\nhello"+id+ " "+cl);
   // 			return " BEGIN HLOAD "+t1+" TEMP 0 "+classList.get(cl).getIdIndex(id) +"\n"+
   // 			"RETURN "+t1+" END\n";
   // }

   public String getIdAddr(String id) {
   		if (stackFrame.containsKey(id)) {
   			return stackFrame.get(id).mem;
   		} else {
   			String t1 = getTemp();
   			return " BEGIN HLOAD "+t1+" TEMP 0 "+classList.get(className).getIdIndex(id) +"\n"+
   			"RETURN "+t1+" END\n";
   		}
   }

   private String getAddr(String id) {
   		if (stackFrame.containsKey(id)) {
   			return " MOVE "+stackFrame.get(id).mem+ " ";
   		} else {
	        return " HSTORE TEMP 0 "+classList.get(className).getIdIndex(id)+" ";
   		}
   }

   private String getType(String id) {
   		if (stackFrame.containsKey(id)) {
   			return stackFrame.get(id).type;
   		} else {
   			println(id);
   			return classList.get(className).getIdType(id);
   		}
   }

   private String getTemp() {
    // if (TEMP_VALUE == 31) {
    //     for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
    //         System.out.println(ste);
    //     }
    // }
   	return "TEMP "+(++TEMP_VALUE);
   }

   private String getLabel() {
   	return "L"+(++LABEL_VALUE);
   }

   public void topSort() {
   		ArrayList<String> que = new ArrayList<String>();
   		ArrayList<String> list = new ArrayList<String>();
   		Set<String> keys = classList.keySet();
   		Iterator<String> it = keys.iterator();
   		ClassMeta cm;
   		String name;
   		while (it.hasNext()) {
   			name = it.next();
   			cm = classList.get(name);
   			if (!cm.isChild) {
   				que.add(name);
   			}
   		}
   		for (int i=0; i<que.size(); i++) {
   			name = que.get(i);
   			list.add(name);
   			if (classDep.containsKey(name)) {
   				que.addAll(classDep.get(name));
   			}
   		}
   		for (int i=0; i<list.size(); i++) {
   			cm = classList.get(list.get(i));
   			if (cm.isChild) {
   				ClassMeta pm = classList.get(cm.parent);
   				ArrayList<String> temp = cm.idField;
   				cm.idField = new ArrayList<String>(pm.idField);
   				cm.idField.addAll(temp);
   				temp = cm.fnField;
   				cm.fnField = new ArrayList<String>(pm.fnField);
   				cm.fnInfo = new ArrayList<String>(pm.fnInfo);
   				ArrayList<String> fn = cm.fnInfo;
   				for (int j=0; j<temp.size(); j++) {
   					if (!pm.fnField.contains(temp.get(j))) {
   						cm.fnField.add(temp.get(j));
   						fn.add(list.get(i));
   					} else {
   						fn.set(fn.indexOf(temp.get(j)),list.get(i));
   					}
   				}
   			} else {
   				ArrayList<String> fn = cm.fnInfo;
   				int size = cm.fnField.size();
   				for (int j=0; j<size; ++j) {
   					fn.add(list.get(i));
   				}
   			}
   		}
   }

}

class ObData{
	public String type;
	public String mem;
	public ObData() {
		type = "";
		mem = "";
	}
	public ObData(String tp, String mm) {
		type = tp;
		mem = mm;
	}
}

class ClassMeta {
	public ArrayList<String> idField;
	public ArrayList<String> fnField;
	public ArrayList<String> fnInfo;
	public HashMap<String,String> idType;
	public boolean isChild;
	public String parent;
	public String name;
	public ClassMeta(String nm) {
		parent = "";
		isChild = false;
		name = nm;
		idType = new HashMap<String,String>();
		fnInfo = new ArrayList<String>();
		idField = new ArrayList<String>();
		fnField = new ArrayList<String>();
	}
	public String getIdType(String id) {
		return idType.get(name+"_"+id);
	}
	public int getIdIndex(String id) {
		// int temp = 0;
		// if ((temp = idField.indexOf(name+"_"+id)) != -1)
		// 	return temp;
		// return this.getFnIndex(id);
    return 4 + 4 * idField.indexOf(name+"_"+id);
	}
	public ClassMeta(String nm, String par) {
		this(nm);
		parent = par;
	}
	public int getFnIndex(String fn) {
		return 4 * fnField.indexOf(fn);
	}

	public int getFnSize() {
		return 4 * fnField.size();
	}
	public int getFieldSize() {
		return 4 * idField.size() + 4;
	}
}