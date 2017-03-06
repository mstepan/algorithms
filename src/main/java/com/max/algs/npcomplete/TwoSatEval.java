package com.max.algs.npcomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class TwoSatEval {
	
	private static final Logger LOG = Logger.getLogger(TwoSatEval.class);
	
	static class OrExpr {
		
		int varIndex1;
		int varIndex2;		
		boolean neg1;
		boolean neg2;
		
		OrExpr(String varStr1, String varStr2){
			
			if( varStr1.charAt(0) == '!' ){
				neg1 = true;
			}
						
			
			varIndex1 = Integer.valueOf( varStr1.substring(varStr1.indexOf("x")+1) ) - 1;
			
			if( varStr2.charAt(0) == '!'){
				neg2 = true;
			}
			
			varIndex2 = Integer.valueOf( varStr2.substring(varStr2.indexOf("x")+1) ) - 1;
		}
		
		boolean eval( boolean[] vars ){			
			boolean var1 = neg1 ? !vars[varIndex1] : vars[varIndex1];
			boolean var2 = neg2 ? !vars[varIndex2] : vars[varIndex2];			
			return  var1 || var2;
		}
	}
	
	static class Expr {		
		
		final Pattern VAR_PATTERN = Pattern.compile("\\s*[(]\\s*(!?x\\d)\\s*[|]{2}\\s*(!?x\\d)\\s*[)]\\s*");
		
		final List<OrExpr> terms = new ArrayList<OrExpr>();
		
		Expr(String exprStr){
			
			String[] termsStr = exprStr.split("&&");
			
			for( String singleTermStr : termsStr ){
				
				Matcher matcher = VAR_PATTERN.matcher(singleTermStr);
				
				if( ! matcher.matches() ){
					throw new IllegalArgumentException("Illegal term found: '" + singleTermStr + "'");
				}
				
				String first = matcher.group(1);
				String second = matcher.group(2);	
				
				terms.add( new OrExpr(first, second) );
			}			
		}
		
		int getVarsCount(){
			return 4;
		}
		
		boolean eval( boolean[] vars ){
			
			for( OrExpr term : terms ){
				if( ! term.eval(vars) ){
					return false;
				}
			}
			
			return true;
		}
		
	}
	
	private TwoSatEval() throws Exception {				
	
		Expr expr = new Expr("( x1 || !x2 ) && ( !x1 || !x3 ) && ( x1 || x2 ) && ( !x3 || x4 ) && ( !x1 || x4 )");
		
		int varCount = 4;		
		int maxValue = (int)Math.pow(2, varCount);
		
		boolean[] vars = new boolean[expr.getVarsCount()];
		
		for( int i = 0; i < maxValue; i++ ){
			
			for( int index =0; index < vars.length; index++ ){
				vars[index] = ((i & 1<<index) != 0);
			}
			
			if( expr.eval(vars) ){
				LOG.info( Arrays.toString(vars) );
			}
		}
		
		LOG.info( "Main done" );		
	}
	
	

	

	public static void main(String[] args) { 
		try {
			new TwoSatEval();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}
	}		

}
