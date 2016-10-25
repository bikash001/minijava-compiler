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
   
   	private HashMap<String,HashMap<String,Pair>> fnList;
   	private int spillMax;
   	public boolean firstTime;
   	private int stmtNo;
   	private HashMap<String,Pair> currMap;
   	private LinkedList<String> tRegisters;
   	private PriorityQueue<Integer> spillList;
   	private boolean expOn;

	public GJNoArguDepthFirst() {
		fnList = new HashMap<String,HashMap<String,Pair>>();
		tRegisters = new LinkedList<String>();
		for (int i=9; i>=0; --i) {
			tRegisters.addFirst("t"+i);
		}
		expOn = false;
		stmtNo = 0;
		spillMax = 0;
		firstTime = true;
		spillList = new PriorityQueue<Integer>();
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
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      if (firstTime) {
      	currMap = new HashMap<String,Pair>();
      	fnList.put("MAIN",currMap);
      }
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      // n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
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
    */
   public R visit(Stmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      ++stmtNo;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      oneTemp(tm);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      ++stmtNo;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      n.f2.accept(this);
      String temp = (String)n.f3.accept(this);
      twoTemp(tm, temp);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      ++stmtNo;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      String temp = (String)n.f2.accept(this);
      twoTemp(tm, temp);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      ++stmtNo;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      oneTemp(tm);
      expOn = true;
      n.f2.accept(this);
      expOn = false;
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      ++stmtNo;
      simpleTemp(tm);
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public R visit(StmtExp n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n) {
      R _ret=null;
      expOn = false;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      simpleTemp(tm);
      expOn = true;
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      expOn = false;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      simpleTemp(tm);
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      expOn = false;
      n.f0.accept(this);
      String tm = (String)n.f1.accept(this);
      String temp = (String)n.f2.accept(this);
      twoTemp(tm, temp);
      return _ret;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n) {
      R _ret=null;
      n.f0.accept(this);
      String lt = (String)n.f1.accept(this);
      _ret = (R)("TEMP "+lt);
      if (expOn) {
      	oneTemp((String)_ret);
      }
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      _ret = (R)n.f0.toString();
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      _ret = (R)n.f0.toString();
      return _ret;
   }

   public void twoTemp(String tm, String temp) {
		// ++stmtNo;
   		if (firstTime) {
   			if (currMap.containsKey(tm)) {
   				currMap.get(tm).second = stmtNo;
   			} else {
   				Pair pr = new Pair(stmtNo);
   				currMap.put(tm, pr);
   			}
   			if (currMap.containsKey(temp)) {
   				currMap.get(temp).second = stmtNo;
   			} else {
   				Pair pair = new Pair(stmtNo);
   				currMap.put(temp, pair);
   			}
   		} else {
   			Pair fm = currMap.get(tm);
   			Pair meta = currMap.get(temp);
   			if (fm.register.isEmpty()) {
	   			if (tRegisters.isEmpty()) {
	   				if (fm.spillNo != -1){
	   					System.out.print("spill "+fm.spillNo+" ");
	   				} else {
	   					int no = getSpilledNo();
	   					System.out.print("spill "+no+" ");
	   				}
	   			} else {
	   				String rg = tRegisters.removeFirst();
	   				fm.register = rg;
	   				System.out.print(rg+" ");
	   			}
	   		} else {
	   			String rs = fm.register;
	   			System.out.print(rs+" ");
	   		}
   			if (meta.register.isEmpty()) {
	   			if (tRegisters.isEmpty()) {
	   				if (meta.spillNo != -1){
	   					System.out.println("spill "+meta.spillNo);
	   				} else {
	   					int no = getSpilledNo();
	   					System.out.println("spill "+no);
	   				}
	   			} else {
	   				String rg = tRegisters.removeFirst();
	   				meta.register = rg;
	   				System.out.println(rg);
	   			}
	   		} else {
	   			String rs = meta.register;
	   			System.out.println(rs);
	   		}
	   		if (stmtNo >= fm.second) {
	   			if (!fm.register.isEmpty()) {
	   				tRegisters.addLast(fm.register);
	   			} else if (fm.spillNo != -1) {
	   				addSpilledNo(fm.spillNo);
	   			}
	   		}
	   		if (stmtNo >= meta.second) {
	   			if (!meta.register.isEmpty()) {
	   				tRegisters.addLast(meta.register);
	   			} else if (fm.spillNo != -1) {
	   				addSpilledNo(fm.spillNo);
	   			}
	   		}
   		}
   }

   public void simpleTemp(String tm) {
   		if (firstTime) {
	      	if (currMap.containsKey(tm)) {
	      		currMap.get(tm).second = stmtNo;
	      	}
	    } else {
	      	if (currMap.containsKey(tm)) {
	      		if (currMap.get(tm).register.isEmpty()) {
	      			System.out.println("spilled "+currMap.get(tm).spillNo);
	      		} else {
	      			System.out.println(currMap.get(tm).register);
	      		}
		   		if (stmtNo >= currMap.get(tm).second) {
		   			if (!currMap.get(tm).register.isEmpty()) {
		   				tRegisters.addLast(currMap.get(tm).register);
		   			}
		   		}
	      	} else {
	      		System.out.println("PRINT "+tm);
	      	}
	    }
   }

   public int getSpilledNo() {
   		int no;
   		if (spillList.isEmpty()) {
   			no = spillMax++;
   		} else {
   			no = spillList.poll();
   		}
   		return no;
   }
   public void addSpilledNo(int a) {
   		spillList.add(a);
   }

   public void oneTemp(String tm) {
   		// ++stmtNo;
   		if (firstTime) {
   			if (currMap.containsKey(tm)) {
   				currMap.get(tm).second = stmtNo;
   			} else {
   				Pair pr = new Pair(stmtNo);
   				currMap.put(tm, pr);
   			}
   		} else {
   			Pair fm = currMap.get(tm);
   			if (fm.register.isEmpty()) {
	   			if (tRegisters.isEmpty()) {
	   				if (fm.spillNo != -1){
	   					System.out.println("spill "+fm.spillNo);
	   				} else {
	   					int no = getSpilledNo();
	   					System.out.println("spill "+no);
	   				}
	   			} else {
	   				String rg = tRegisters.removeFirst();
	   				fm.register = rg;
	   				System.out.println(rg);
	   			}
	   		} else {
	   			String rs = fm.register;
	   			System.out.println(rs);
	   		}
	   		if (stmtNo >= currMap.get(tm).second) {
	   			if (!fm.register.isEmpty()) {
	   				tRegisters.addLast(fm.register);
	   			} else if (fm.spillNo != -1) {
	   				addSpilledNo(fm.spillNo);
	   			}
	   		}
   		}
   }

}

class FuncMeta{
	public HashMap<String,Pair> hm;
	public FuncMeta() {
		hm = new HashMap<String,Pair>();
	}
}

class Pair{
	public int first;
	public int second;
	public int spillNo;
	public String register;
	public Pair(int ln){
		first = ln;
		second = ln;
		register = "";
		spillNo = -1;
	}
}