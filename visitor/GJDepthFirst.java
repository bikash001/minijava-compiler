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
public class GJDepthFirst<R,A> implements GJVisitor<R,A> {
  
	private Integer totalSpilled = 0;
	private String opType, moveVal;
	private int expType;
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( Procedure() )*
    * f13 -> VariablePackingInfo()
    * f14 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String mem = (String)n.f5.accept(this, argu);
      int alloc = 4 * Integer.valueOf(mem) + 4;
      totalSpilled = alloc;
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      System.out.println("\t.text");
      System.out.println("\t.globl\tmain");
      System.out.println("main:");
      System.out.println("\tmove $fp, $sp");
      System.out.println("\tsubu $sp, $sp, "+alloc);
      System.out.println("\tsw $ra, -4($fp)");
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      System.out.println("\tlw $ra, -4($fp)");
      System.out.println("\taddu $sp, $sp, "+alloc);
      System.out.println("\tli $v0, 10\n\tsyscall\n");
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      n.f14.accept(this, argu);
      printLabels();
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      String label = (String)n.f0.accept(this, (A)totalSpilled);
      n.f1.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String mem = (String)n.f5.accept(this, argu);
      int space = 4 * Integer.valueOf(mem) + 8;
      totalSpilled = space;
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      System.out.println("\t.text\n\t.globl "+label);
      System.out.println(label+":");
      System.out.println("\tsw $fp, -8($sp)\n\tmove $fp, $sp\n\tsubu $sp, $sp, "+space);
      System.out.println("\tsw $ra, -4($fp)");
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      System.out.println("\tlw $ra, -4($fp)\n\taddu $sp, $sp, "+space);
      System.out.println("\tlw $fp, -8($sp)\n\tjr $ra\n");
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("\tnop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      System.out.println("\tla $a0, str_er\n\tli $v0, 4\n\tsyscall\n\tli $v0, 10\n\tsyscall");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String reg = (String)n.f1.accept(this, argu);
      String label = (String)n.f2.accept(this, (A)totalSpilled);
      System.out.println("\tbeqz $"+reg+", "+label);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String label = (String)n.f1.accept(this, (A)totalSpilled);
      System.out.println("\tj "+label);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String rdest = (String)n.f1.accept(this, argu);
      String lt = (String)n.f2.accept(this, argu);
      String rsrc = (String)n.f3.accept(this, argu);
      System.out.println("\tsw $"+rsrc+", "+lt+"($"+rdest+")");
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String rdest = (String)n.f1.accept(this, argu);
      String rsrc = (String)n.f2.accept(this, argu);
      String lt = (String)n.f3.accept(this, argu);
      System.out.println("\tlw $"+rdest+", "+lt+"($"+rsrc+")");
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String reg = (String)n.f1.accept(this, argu);
      String exp = (String)n.f2.accept(this, argu);
      switch(expType) {
      	case 0:
      		System.out.println("\tli $"+reg+", "+exp);
      		break;
      	case 1:
      		System.out.println("\tmove $"+reg+", $"+exp);
      		break;
      	case 2:
      		System.out.println("\tla $"+reg+", "+exp);
      		break;
      	case 3:
   			System.out.println("\t"+opType+" $"+reg+", "+moveVal);
   			break;
      	case 4:
      		System.out.println("\tmove $"+reg+", $v0");
      		break;
      }
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String ex = (String)n.f1.accept(this, argu);
      if (expType == 0) {	//immediate
      	System.out.println("\tli $a0, "+ex+"\n\tjal _print");
      } else {
      	System.out.println("\tmove $a0, $"+ex+"\n\tjal _print");
      }
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String reg = (String)n.f1.accept(this, argu);
      String ar = (String)n.f2.accept(this, argu);
      int var = (totalSpilled - Integer.valueOf(ar) * 4 - 12);
      System.out.println("\tlw $"+reg+", "+var+"($sp)");
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String ar = (String)n.f1.accept(this, argu);
      String reg = (String)n.f2.accept(this, argu);
      int var = (totalSpilled - Integer.valueOf(ar) * 4 - 12);
      System.out.println("\tsw $"+reg+", "+var+"($sp)");
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String lt = (String)n.f1.accept(this, argu);
      int var = (Integer.valueOf(lt) - 1) * 4 + 8;
      String reg = (String)n.f2.accept(this, argu);
      System.out.println("\tsw $"+reg+", "+var+"($sp)");
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String exp = (String)n.f1.accept(this, argu);
      if (expType == 1) {
      	System.out.println("\tjalr $"+exp);
      } else {
      	System.out.println("\tjal "+exp);
      }
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String exp = (String)n.f1.accept(this, argu);
      if (expType == 0) {
      	System.out.println("\tli $a0, "+exp+"\n\tjal _halloc");
      } else {//1 is register
      	System.out.println("\tmove $a0, $"+exp+"\n\tjal _halloc");
      }
      expType = 4;
      	_ret = (R)"$v0";
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      String ops = (String)n.f0.accept(this, argu);
      String reg = (String)n.f1.accept(this, argu);
      String exp = (String)n.f2.accept(this, argu);
      if (expType != 0) {
      	exp = "$"+exp;
      }
      moveVal = "$"+reg+", "+exp;
      if (ops.equals("LT")) {
      	opType = "slt";
      } else if (ops.equals("PLUS")) {
      	opType = "add";
      } else if (ops.equals("MINUS")) {
      	opType = "sub";
      } else if (ops.equals("TIMES")) {
      	opType = "mul";
      } else if (ops.equals("DIV")) {
      	opType = "div";
      }
      expType = 3;
      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    *       | "BITOR"
    *       | "BITAND"
    *       | "LSHIFT"
    *       | "RSHIFT"
    *       | "BITXOR"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = (R)n.f0.choice.toString();
      return _ret;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, (A)totalSpilled);
      return _ret;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = (R)n.f0.choice.toString();
      expType = 1;
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = (R)n.f0.toString();
      expType = 0;
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = (R)n.f0.toString();
      expType = 2;
      if (argu == null) {
      	System.out.println(n.f0.toString()+":");
      }
      return _ret;
   }

   /**
    * f0 -> "// Number of  vars after packing ="
    * f1 -> IntegerLiteral()
    * f2 -> "; Number of Spilled vars ="
    * f3 -> IntegerLiteral()
    */
   public R visit(VariablePackingInfo n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return _ret;
   }


   private void printLabels() {
		System.out.println("\t.text\n\t.globl _halloc\n_halloc:\n\tli $v0, 9\n\tsyscall");
		System.out.println("\tjr $ra\n\n\t.text\n\t.globl _print\n_print:\n\tli $v0, 1\n\tsyscall");
		System.out.println("\tla $a0, newl\n\tli $v0, 4\n\tsyscall\n\tjr $ra\n\n\t.data\n\t.align 0");
		System.out.println("newl:\t.asciiz \"\\n\"\n\n\t.data\n\t.align 0\nstr_er:\t.asciiz \"ERROR: abnormal termination\\n\"");
   }
}
