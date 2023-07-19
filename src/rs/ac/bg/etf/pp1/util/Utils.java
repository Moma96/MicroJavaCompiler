package rs.ac.bg.etf.pp1.util;

import java.util.Stack;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Utils {
	
	public static final Struct
			intStruct = new Struct(Struct.Int),
			charStruct = new Struct(Struct.Char),
			boolStruct = new Struct(Struct.Bool);
	
	public static Stack<Boolean> boolExpresionStack = new Stack<Boolean>();
	
	static public Obj createGlobalConst(int value, Struct struct) {
		Obj con = Tab.insert(Obj.Con, "$", struct);

		con.setLevel(0);
		con.setAdr(value);
		
		return con;
	}
	
	static public void print(String value, int width) {
		for(int i = 0; i < value.length(); i++) {
			print(value.charAt(i), i == 0 ? 5 : 1);
		}
	}

	static public void print(char value, int width) {
		Code.load(createGlobalConst(value, null));
		Code.loadConst(width);
		Code.put(Code.bprint);
	}
}
