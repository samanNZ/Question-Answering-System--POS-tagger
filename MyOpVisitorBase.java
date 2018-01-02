package hiwi.ilmenau.de;

import java.util.List;

import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.OpWalker;
import org.apache.jena.sparql.algebra.op.OpBGP;

public class MyOpVisitorBase extends OpVisitorBase
{
	 public static List<Triple> triples ;
    public static  List<Triple> myOpVisitorWalker(Op op)
    {
        MyOpVisitorBase obj  =new  MyOpVisitorBase();
    	OpWalker.walk(op, obj);
    	return triples ;
    }

    @Override
    public void visit(final OpBGP opBGP) {
         triples = opBGP.getPattern().getList();
      //  int i = 0;
       // for (final Triple triple : triples) {
        //    System.out.println("Triple: "+triple.toString());
        //}
    }
}