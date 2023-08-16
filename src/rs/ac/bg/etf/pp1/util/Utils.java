package rs.ac.bg.etf.pp1.util;

import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.Const;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Utils {
	
	public static final Struct boolType = new Struct(Struct.Bool);
	
	public static Struct arrayType(Struct elemType) {
		Struct arrayType = new Struct(Struct.Array);
		arrayType.setElementType(elemType);
		return arrayType;
	}
	
	private static int _printBoolAdr;
	public static int getPrintBoolAdr() {
		return _printBoolAdr;
	}
	
	private static int _findAnyAdr;
	public static int getFindAnyAdr() {
		return _findAnyAdr;
	}
	
	private static int _arrayLengthGuardAdr;
	public static int getArrayLengthGuardAdr() {
		return _arrayLengthGuardAdr;
	}
	
	private static int _arrayIndexGuardAdr;
	public static int getArrayIndexGuardAdr() {
		return _arrayIndexGuardAdr;
	}
	
	private static int _arrayMemberAccessGuardAdr;
	public static int getArrayMemberAccessGuardAdr() {
		return _arrayMemberAccessGuardAdr;
	}
	
	public static void callMethod(int adr) {
		int adrOffset = adr - Code.pc;
		Code.put(Code.call);
		Code.put2(adrOffset);
	}
	
	public static void tabInit() {
		Tab.init();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}
	
	public static void codeGeneratorInit() {
		printBoolInit();
		findAnyInit();
		guardsInit();
	}
	
	public static void printBoolInit() {
		_printBoolAdr = Code.pc;
		enterMethod(2, 0);

		 // argument 1 - value
		Code.put(Code.load_n);
		
		Code.loadConst(1);
		Code.putFalseJump(0, 0);
		int falseCaseAddress = Code.pc - 2;
		
		// value == 1
		print("true");
		
		Code.putJump(0);
		int exitAddress = Code.pc - 2;
		
		Code.fixup(falseCaseAddress);
		
		// value == 0
		print("false");
		
		Code.fixup(exitAddress);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public static void print(String value) {
		for(int i = 0; i < value.length(); i++) {
			Code.load(createGlobalConst(value.charAt(i), Tab.charType));
			if (i == 0) {
				// initBool method argiment 2 - width
				Code.put(Code.load_1);
			} else {
				Code.loadConst(1);
			}
			Code.put(Code.bprint);
		}
	}
	
	public static void findAnyInit() {
		_findAnyAdr = Code.pc;
		enterMethod(2, 1);
		
		 // argument 1 - array adr
		Code.put(Code.load_n);
		
		Code.put(Code.arraylength);
		int whileAdr = Code.pc;
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.put(Code.store_2);
		
		Code.put(Code.load_2);
		Code.loadConst(0);
		// while index >= 0
		Code.putFalseJump(5, 0);
		int exitWhileAddress = Code.pc - 2;
		
		// argument 1 - array adr
		Code.put(Code.load_n);
		Code.put(Code.load_2);
		// load element on index load_2
		Code.put(Code.aload);
		// argument 2 - value to search
		Code.put(Code.load_1);
		// exit if equal
		Code.putFalseJump(1, 0);
		int exitValueFoundAddress = Code.pc - 2;
		
		Code.put(Code.load_2);		
		Code.putJump(whileAdr);
		
		Code.fixup(exitWhileAddress);
		// return 0
		Code.loadConst(0);
		
		Code.putJump(0);
		int exitAddress = Code.pc - 2;
		
		Code.fixup(exitValueFoundAddress);
		// return 1
		Code.loadConst(1);
		
		Code.fixup(exitAddress);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public static void guardsInit() {
		arrayLengthGuardInit();
		arrayIndexGuardInit();
		arrayMemberGuardInit();
	}
	
	public static void arrayLengthGuardInit() {
		_arrayLengthGuardAdr = Code.pc;
		enterMethod(1, 0);
		
		 // argument 1 - array length
		Code.put(Code.load_n);
		
		Code.loadConst(0);
		Code.putFalseJump(5, 0);
		int errorAdr = Code.pc - 2;
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(errorAdr);
		print("\nArray length must not be a negative number!");
		Code.put(Code.trap);
		Code.put(0);
	}
	
	public static void arrayIndexGuardInit() {
		_arrayIndexGuardAdr = Code.pc;
		enterMethod(2, 0);
		
		 // argument 1 - array adr
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		
		// argument 2 - index
		Code.put(Code.load_1);
		Code.putFalseJump(4, 0);
		int indexOverflowErrorAdr = Code.pc - 2;
		
		// argument 2 - index
		Code.put(Code.load_1);
		Code.loadConst(0);
		Code.putFalseJump(5, 0);
		int indexUnderflowErrorAdr = Code.pc - 2;
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(indexOverflowErrorAdr);
		Code.fixup(indexUnderflowErrorAdr);
		print("\nArray index is out of bounds!");
		Code.put(Code.trap);
		Code.put(1);
	}

	public static void arrayMemberGuardInit() {
		_arrayMemberAccessGuardAdr = Code.pc;
		enterMethod(1, 0);
		
		 // argument 1 - array adr
		Code.put(Code.load_n);
		Code.loadConst(0);
		Code.putFalseJump(1, 0);
		int errorAdr = Code.pc - 2;
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Code.fixup(errorAdr);
		print("\nArray member access before array initialization!");
		Code.put(Code.trap);
		Code.put(2);
	}
	
	private static void enterMethod(int numberOfArguments, int numberOfLocalVars) {
		Code.put(Code.enter);
		Code.put(numberOfArguments);
		Code.put(numberOfArguments + numberOfLocalVars);
	}
	
	public static boolean isSympleType(Struct struct) {
		return struct.equals(Tab.intType) || struct.equals(Tab.charType) || struct.equals(Utils.boolType);
	}

	public static int getConstValue(Const const_) {
		if (const_ instanceof NumConst) {
			return ((NumConst)const_).getN1();
		} else if (const_ instanceof CharConst) {
			return ((CharConst)const_).getC1();
		} else if (const_ instanceof BoolConst) {
			return ((BoolConst)const_).getB1();
		} else {
			return 0;
		}
	}
	
	public static Obj createGlobalConst(int value, Struct struct) {
		return createGlobalNamedConst(value, "$", struct);
	}
	
	public static Obj createGlobalNamedConst(int value, String name, Struct struct) {
		Obj con = Tab.insert(Obj.Con, name, struct);
		
		con.setLevel(0);
		con.setAdr(value);
		
		return con;
	}
}

