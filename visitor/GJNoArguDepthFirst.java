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

	public boolean init;
	private HashMap<String,ClassMeta> classList;
	private HashMap<String,String> stackFrame;
	private String currentClass;
	private String currentFunction;
	private int param;
	private String pClass, pFunc;
	private boolean expn;
	public GJNoArguDepthFirst() {
		init = true;
		param = 1;
		expn = false;
		pClass = "";
		pFunc = "";
		currentClass = "";
		currentFunction = "";
		stackFrame = new HashMap<String,String>();
		classList = new HashMap<String,ClassMeta>();
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
      n.f0.accept(this);
      currentClass = (String)n.f1.accept(this);
      if (init) {
      	classList.put(currentClass, new ClassMeta());
      }
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      currentFunction = "main";
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      String id = (String)n.f11.accept(this);
      if (!init) {
      	stackFrame.put(id,"String[]");
      }
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      currentFunction = "";
      stackFrame.clear();
      n.f16.accept(this);
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
      currentClass = (String)n.f1.accept(this);
      if (init) {
      	if (classList.containsKey(currentClass)) {
      		System.out.println("cannot define two class of same name");
      		System.exit(0);
      	}
      	classList.put(currentClass,new ClassMeta());
      }
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
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
      currentClass = (String)n.f1.accept(this);
      n.f2.accept(this);
      String cl = (String)n.f3.accept(this);
      if (init) {
      	if (classList.containsKey(currentClass)) {
      		System.out.println("cannot define two class of same name");
      		System.exit(0);
      	}
      	classList.put(currentClass,new ClassMeta(cl));
      } else {
      	if (!classList.containsKey(cl)) {
      		System.out.println("parent class doesnot exist");
      		System.exit(0);
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
      String tp = (String)n.f0.accept(this);
      String id = (String)n.f1.accept(this);
      if (init) {
	      if (currentFunction.isEmpty()) {
	      	ClassMeta cm = classList.get(currentClass);
	      	if (cm.containsId(id)) {
	      		System.out.println("cannot declare two identifier of same name");
	      		System.exit(0);
	      	}
	      	cm.putIdType(id,tp);
	      }
	  } else if (!currentFunction.isEmpty()) {	//local variables
      	if (stackFrame.containsKey(id)) {
      		System.out.println("cannot declare two identifier of same name inside same function");
      		System.exit(0);
      	}
      	stackFrame.put(id, tp);
      }
      n.f2.accept(this);
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
      String fn = (String)n.f1.accept(this);
      currentFunction = (String)n.f2.accept(this);
      if (init) {
	      ClassMeta cm = classList.get(currentClass);
	      if (cm.containsFunc(currentFunction)) {
	      	System.out.println("Cannot define two function with same name");
	      	System.exit(0);
	      } else {
	      	cm.putFunc(currentFunction,new FuncMeta(fn));
	      }
	  }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      // retn = true;
      String rt = (String)n.f10.accept(this);
      if (!init) {
      		String typ = classList.get(currentClass).getFunc(currentFunction).getRtn();
	      if (!getType(rt).equals(typ)) {
	      	System.out.println("return type error "+rt +" "+typ);
	      	System.exit(0);
	      }
	  }
	  // retn = false;
      n.f11.accept(this);
      n.f12.accept(this);
      if (!init) {
      	stackFrame.clear();
      }
      currentFunction = "";
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n) {
      R _ret=null;
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
      String tp = (String)n.f0.accept(this);
      String id = (String)n.f1.accept(this);
      FuncMeta fm = classList.get(currentClass).getFunc(currentFunction);
      if (init) {
	      if (fm.containsParam(id)) {
	      	System.out.println("formal parameter cannot have two id of same name");
	      	System.exit(0);
	      } else {
	      	fm.addParam(tp);
	      }
	  } else {
	  	stackFrame.put(id, tp);
	  }
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
      R _ret= (R)"int[]";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n) {
      R _ret= (R)"boolean";
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
      n.f0.accept(this);
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
      String id = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      n.f3.accept(this);
      if (!init) {
      	System.out.println("l432 "+rt);
      	String lt = getType(id);
      	if (rt == null) {
      		System.out.println("null ljsjjdj");
      	}
      	if (!lt.equals(getType(rt))) {
      		System.out.println("type error 438 "+lt+" "+rt);
      		System.exit(0);
      	}
      }
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
      String id = (String)n.f0.accept(this);
      n.f1.accept(this);
      String indx = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String rt = (String)n.f5.accept(this);
      n.f6.accept(this);
      if (!init) {
      	System.out.println("l462");
      	if ((!getType(id).equals("int[]")) || (!getType(indx).equals("int")) || (!getType(rt).equals("int"))) {
      		System.out.println("type error 465");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n) {
      R _ret=null;
      n.f0.accept(this);
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
      String tp = (String)n.f2.accept(this);
      if (!init) {
      	if (!getType(tp).equals("boolean")) {
	      	System.out.println("type error 495");
	      	System.exit(0);
	     }
      }
      n.f3.accept(this);
      n.f4.accept(this);
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
      String tp = (String)n.f2.accept(this);
      if (!init) {
      	if (!getType(tp).equals("boolean")) {
	      	System.out.println("type error 518");
	      	System.exit(0);
	     }
      }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
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
      String tp = (String)n.f2.accept(this);
      if (!init) {
      	if (!getType(tp).equals("boolean")) {
	      	System.out.println("type error 541");
	      	System.exit(0);
      	}
      }
      n.f3.accept(this);
      n.f4.accept(this);
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
      String tp = (String)n.f2.accept(this);
      if (!init) {
      	if (!getType(tp).equals("int")) {
	      	System.out.println("type error 496");
	      	System.exit(0);
	      }
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
      if (!init) {
      	System.out.println("l586 "+(String)_ret);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n) {
      R _ret= (R)"boolean";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 605");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n) {
      R _ret= (R)"boolean";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 624");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n) {
      R _ret= (R)"boolean";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 643");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n) {
      R _ret= (R)"boolean";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 662");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n) {
      R _ret= (R)"int";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 681");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n) {
      R _ret= (R)"int";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 700 "+lt +" "+rt);
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n) {
      R _ret= (R)"int";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 719");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n) {
      R _ret= (R)"int";
      String lt = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rt = (String)n.f2.accept(this);
      if (!init) {
      	lt = getType(lt);
      	rt = getType(rt);
      	if (!lt.equals(rt) && !lt.equals((String)_ret)) {
      		System.out.println("type error 738");
      		System.exit(0);
      	}
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
      R _ret= (R)"int";
      String ap = (String)n.f0.accept(this);	//check if f0 is id and type is int[]
      n.f1.accept(this);
      String ip = (String)n.f2.accept(this);
      n.f3.accept(this);
      if (!init) {
      	System.out.println("l755");
      	ap = getType(ap);
      	ip = getType(ip);
      	if ((!ap.equals("int[]")) || (!ip.equals("int"))) {
      		System.out.println("type error 763");
      		System.exit(0);
      	}
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n) {
      R _ret= (R)"int";
      String ap = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if (!init) {
      	System.out.println("l776");
      	String tp = getType(ap);
      	if (!tp.equals("int[]")) {
      		System.out.println("type error 783");
      		System.exit(0);
      	}
      }
      return _ret;
   }



/****************************************************************
*						start from here 						*
*****************************************************************/

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
      String obj = (String)n.f0.accept(this);
      n.f1.accept(this);
      String fn = (String)n.f2.accept(this);
      if (!init) {
      	pClass = obj;
      	pFunc = fn;
      	System.out.println("ye "+pClass+" "+pFunc);
      }
      param = 0;
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      if (!init) {
      	FuncMeta fm = getFunc(pClass,pFunc);
      	if (!fm.totalPmEquals(param)) {
      		System.out.println("type error 832");
      		System.exit(0);
      	}
      	_ret = (R)fm.getRtn();
      	System.out.println("l824 "+(String)_ret);
      }
      param = 0;
      pFunc = "";
      if (!init) {
      	System.out.println("end l819");
      }
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      n.f1.accept(this);
      if (!init) {
      	System.out.println("l825");
      	String tp = getType((String)_ret);
      	FuncMeta fm = getFunc(pClass,pFunc);
      	if (!tp.equals(fm.getParam(++param))) {
      		System.out.println("type error 832 "+ tp +" " + fm.getParam(param));
      		System.exit(0);
      	}
      }
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
      if (!init) {
      	System.out.println("l845");
      	String tp = getType((String)_ret);
      	FuncMeta fm = getFunc(pClass,pFunc);
      	if (!tp.equals(fm.getParam(++param))) {
      		System.out.println("type error 851");
      		System.exit(0);
      	}
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
      if (!init) {
      	System.out.println("l874 "+(String)_ret);
      }
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret= (R)"int";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n) {
      R _ret=(R)"boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n) {
      R _ret=(R)"boolean";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n) {
      R _ret=null;
      n.f0.accept(this);
      _ret = (R)n.f0.toString();
      System.out.println("l921 "+(String)_ret);
      // if (!init && (expn || (!pFunc.isEmpty()))) {
      // 	_ret = (R)getType((String)_ret);
      // }
      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n) {
      R _ret=(R)"this";
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
      R _ret=(R)"int[]";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String tp = (String)n.f3.accept(this);
      if (!init) {
      	tp = getType(tp);
      	if (!tp.equals("int")) {
      		System.out.println("type error 912");
      		System.exit(0);
      	}
      }
      n.f4.accept(this);
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
      _ret = n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n) {
      R _ret= (R)"boolean";
      n.f0.accept(this);
      String tp = (String)n.f1.accept(this);
      if (!init) {
      	if (!tp.equals("boolean")) {
      		System.out.println("type error 912");
      		System.exit(0);
      	}
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
      _ret = n.f1.accept(this);
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


   private String getType(String id) {
   		switch(id) {
   			case "int":
   			case "int[]":
   			case "boolean":
   				return id;
   		}
   		if (stackFrame.containsKey(id)) {
   			return stackFrame.get(id);
   		}
   		ClassMeta cm = classList.get(currentClass);
   		String pt = cm.getParent();
   		while ((!cm.containsId(id)) && (!pt.isEmpty())) {
   			cm = classList.get(pt);
   			pt = cm.getParent();
   		}
   		if (cm.containsId(id)) {
   			return cm.getIdType(id);
   		} else {
   			System.out.println("id not found "+id);
   			System.exit(0);
   			return null;
   		}
   }

   private FuncMeta getFunc(String cl, String fn) {
   		if (cl.equals("this")) {
   			cl = currentClass;
   		}
   		System.out.println("cl name "+cl);
   		if (cl == null || fn == null) {
   			System.out.println("null data;");
   		}
   		ClassMeta cm = classList.get(cl);
   		if (cm == null) {
   			System.out.println(classList.keySet().toString());
   			System.out.println("null cm;");
   		}
   		String pt = cm.getParent();
   		if (pt == null) {
   			System.out.println("null pt;");
   		}
   		while ((!cm.containsFunc(fn)) && (!pt.isEmpty())) {
   			cm = classList.get(pt);
   			pt = cm.getParent();
   		}
   		if (cm.containsFunc(fn)) {
   			return cm.getFunc(fn);
   		} else {
   			System.out.println("function not found");
   			System.exit(0);
   			return null;
   		}
   }
}

class ClassMeta {
	private String parent;
	private HashMap<String,FuncMeta> funcList;
	private HashMap<String,String> idList;
	public ClassMeta() {
		parent = "";
		idList = new HashMap<String,String>();
		funcList = new HashMap<String,FuncMeta>();
	}
	public ClassMeta(String par) {
		parent = par;
		idList = new HashMap<String,String>();
		funcList = new HashMap<String,FuncMeta>();
	}
	public String getParent() {
		return parent;
	}
	public boolean containsId(String id) {
		return idList.containsKey(id);
	}
	public void putIdType(String id, String type) {
		idList.put(id,type);
	}
	public String getIdType(String id) {
		return idList.get(id);
	}
	public boolean containsFunc(String fnc) {
		return funcList.containsKey(fnc);
	}
	public void putFunc(String nm, FuncMeta fm) {
		funcList.put(nm,fm);
	}
	public FuncMeta getFunc(String nm) {
		return funcList.get(nm);
	}
}

class FuncMeta {
	private String rtype;
	private ArrayList<String> paramList;
	public FuncMeta() {
		rtype = "void";
		paramList = new ArrayList<String>();
	}
	public FuncMeta(String rt) {
		rtype = rt;
		paramList = new ArrayList<String>();
	}
	public boolean totalPmEquals(int ar) {
		return ar == paramList.size();
	}
	public String getRtn() {
		return rtype;
	}
	public boolean containsParam(String pm) {
		return paramList.contains(pm);
	}
	public String getParam(int x) {
		if (x > paramList.size()) {
			System.out.println("more parameter given");
			System.exit(0);
		}
		return paramList.get(x-1);
	}
	public void addParam(String pm) {
		paramList.add(pm);
	}
}