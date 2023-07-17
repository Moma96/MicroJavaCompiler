package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			String sourceCodePath = args[0];
			File sourceCode = new File(sourceCodePath);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
		
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse();
			
			Program prog = (Program)s.value;
			// Initialize symbol table
			Tab.init();
			log.info("Syntax tree:");
			log.info(prog.toString(""));
			
			log.info("Semantic validation:");	
			SemanticPass v = new SemanticPass();
			prog.traverseBottomUp(v);
			
			Tab.dump();
			
			if (!p.errorDetected && v.passed()) {
				String objFilePath = args[1];
				File objFile = new File(objFilePath);
				if (objFile.exists()) objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsing successfully finished!");
			} else {
				log.error("Parsing failed!");
			}
		} finally {
			if (br != null) try {
				br.close();
			} catch (IOException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
	}

}
