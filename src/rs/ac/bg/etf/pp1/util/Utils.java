package rs.ac.bg.etf.pp1.util;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Utils {
	
	public static final Struct
			intStruct = new Struct(Struct.Int),
			charStruct = new Struct(Struct.Char),
			boolStruct = new Struct(Struct.Bool);
	
	private static int _printBoolAdr;
	public static int getPrintBoolAdr() {
		return _printBoolAdr;
	}
	
	public static void Initialize() {
		printBool();
	}
	
	public static void printBool() {
		_printBoolAdr = Code.pc;
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		
		Code.put(Code.load_n);
		Code.loadConst(1);
		Code.putFalseJump(0, 0);
		int falsePatchAddress = Code.pc - 2;
		
		// value == 1
		Utils.print("true", 5);
		
		Code.putJump(0);
		int exitPatchAddress = Code.pc - 2;
		
		Code.fixup(falsePatchAddress);
		
		// value == 0
		Utils.print("false", 5);
		
		Code.fixup(exitPatchAddress);
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public static void print(String value, int width) {
		for(int i = 0; i < value.length(); i++) {
			print(value.charAt(i), i == 0 ? 5 : 1);
		}
	}

	public static void print(char value, int width) {
		Code.load(createGlobalConst(value, null));
		Code.loadConst(width);
		Code.put(Code.bprint);
	}
	
	public static Obj createGlobalConst(int value, Struct struct) {
		Obj con = Tab.insert(Obj.Con, "$", struct);
		
		con.setLevel(0);
		con.setAdr(value);
		
		return con;
	}
}

