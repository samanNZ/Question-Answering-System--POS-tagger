package hiwi.ilmenau.de;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.syntax.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.StringUtils;

import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class JasonReader {

	public static void main(String[] args) throws ParseException, ClassNotFoundException, IOException {
		String queryFile =  "G:\\Eclipse\\hiwi.ilmenau.de\\data_set.json"; 
		List<String> questions = getCorrectedQuestions(queryFile);
		MaxentTagger tagger = new MaxentTagger("tagger/bidirectional-distsim-wsj-0-18.tagger");
		
	}

	public static List<String> getCorrectedQuestions(String queryFile) throws ParseException, ClassNotFoundException, IOException {
		JSONParser parser = new JSONParser();
		List<String> correctedQuestions = new ArrayList<String>();
	    
		try {
			Object obj = parser.parse(new FileReader(queryFile));
			JSONArray array = (JSONArray) obj;
			System.out.println(array.size());
			String queryStr ;
			MaxentTagger tagger = new MaxentTagger("tagger/bidirectional-distsim-wsj-0-18.tagger");
		    
			for(int i=0;i<array.size();i++){
				JSONObject jObj = (JSONObject) array.get(i);
				//System.out.println(i);
				queryStr = jObj.get("sparql_query").toString();
				List<Triple> Tps= getTriplesPatterns(queryStr);
				if(Tps.size()==1){
				     String prop = Tps.get(0).getPredicate().toString();
				     String correctedQuestion = jObj.get("corrected_question").toString();
				     correctedQuestions.add(correctedQuestion);
				     String tagged = tagger.tagString(correctedQuestion);
				     String [] tokens = tagged.split(" ");
				     String verbs = "" ;
				     for (String token:tokens){	 
				      if(token.contains("/V"))
				      {
				       String [] subTokens = token.split("/V");
				       verbs = verbs + " " +  subTokens[0];
				      } 
				     }
				     System.out.println(prop +"\t"+verbs );
				    }
				}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return correctedQuestions;
	}

	private static List<Triple> getTriplesPatterns(String queryStr) {
		// TODO first parse the given SPARQL string using JENA SPARQL Parser. then get the list of predicates in the query. if its equal to 1 then return true else return false
		//MyOpVisitorBase.myOpVisitorWalker(OpBGP);

		if(queryStr.contains("COUNT(?uri)"))
			queryStr = queryStr.replace("COUNT(?uri)","(COUNT(?uri) as ?cnt)");
		//	System.out.println(queryStr);
		Query sparql = QueryFactory.create(queryStr);
		//System.out.println(sparql.getProjectVars());
		Op op = Algebra.compile(sparql);
		List<Triple> triples = MyOpVisitorBase.myOpVisitorWalker(op);
		//	System.out.println(queryStr +"\t" + triples.size());
		return triples;
	}
	
}

