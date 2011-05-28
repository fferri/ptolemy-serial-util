package ptolemy.exprparser.tests;

import ptolemy.exprparser.ExprParser;
import ptolemy.exprparser.ParseException;

public class Tests {
	private static int i = 1;

	public static void main(String args[]) throws Exception {
		boolean pass = true;

		pass = pass && T("1+2*3-4*(2+3*-2)", 23.0);
		pass = pass && T("(2^3)^2", 64.0);
		pass = pass && T("2^(3^2)", 512.0);
		pass = pass && T("2^3^2", 512.0);
		pass = pass && T("-1", -1);
		pass = pass && T("-(-1)", 1);
		pass = pass && T("2^0-1", 0);

		String expr = "sin(x)/x";
		ExprParser p = ExprParser.parseString(expr);
		p.setVar("x", 0);
		for(int j = 1; j < 5; j++) {
			p.setVar("x", j);
			System.out.println(expr + " (x = " + j + ") = " + p.eval());
		}
	}

	public static boolean T(String expr, double expect) {
		System.out.print("Test #" + (i++) + ": '" + expr + "' =? " + expect + " ...");
		try {
			ExprParser p = ExprParser.parseString(expr);
			double v = p.eval();
			boolean r = (v == expect);
			if(r) {
				System.out.println("PASSED");
			} else {
				System.out.println("FAILED (result: " + v + ")");
			}
			p.dumpTree();
			return r;
		} catch(ParseException ex) {
			System.out.println("FAILED (ParseException: " + ex + ")");
			return false;
		}
	}
}
