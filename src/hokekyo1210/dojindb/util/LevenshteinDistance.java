package hokekyo1210.dojindb.util;

import java.io.UnsupportedEncodingException;

public class LevenshteinDistance {
	
	private static int dp[][] = new int[60][60];
	
	public static double edit(String px_, String py_) throws UnsupportedEncodingException
	  {
		String px = px_.toLowerCase().replaceAll("\\ ", "").replaceAll("Å@", "");
		String py = py_.toLowerCase().replaceAll("\\ ", "").replaceAll("Å@", "");
	    int len1=px.length(),len2=py.length();
	    int i,j;
	    int maxi = Math.max(len1,len2);
	    for(i = 0;i<maxi;i++)for(j = 0;j<maxi;j++){
	    	dp[i][j] = 0;
	    }
	    for(i=0;i<len1+1;i++) dp[i][0] = i;
	    for(i=0;i<len2+1;i++) dp[0][i] = i;
	    for(i=1;i<=len1;++i)
	    {
	      for(j=1;j<=len2;++j)
	      {
	    	  dp[i][j] = Math.min(Math.min(
	           (dp[i-1][j-1])
	           + ((px.substring(i-1,i).equals(py.substring(j-1,j)))?0:1) , // replace
	                      (dp[i][j-1]) + 1),     // delete
	                      (dp[i-1][j]) + 1);  // insert
	      }
	    }
	    return ((double)(maxi-dp[len1][len2])/(double)maxi);
	  }
	
	private static final String toSmallAsciiByOne( boolean mode,char code ) {
        switch( code ){
            case 'ÅI' : return "!" ;
            case 'Åh' : return ( mode ) ? "&quot;" : "\"" ;
            case 'Åî' : return "#" ;
            case 'Åê' : return "$" ;
            case 'Åè' : return "\\" ;
            case 'Åì' : return "%" ;
            case 'Åï' : return "&" ;
            case 'Åf' : return "\'" ;
            case 'Åi' : return "(" ;
            case 'Åj' : return ")" ;
            case 'Åñ' : return "*" ;
            case 'Å{' : return "+" ;
            case 'ÅC' : return "," ;
            case 'Å|' : return "-" ;
            case 'ÅD' : return "." ;
            case 'Å^' : return "/" ;
            case 'ÇO' : return "0" ;
            case 'ÇP' : return "1" ;
            case 'ÇQ' : return "2" ;
            case 'ÇR' : return "3" ;
            case 'ÇS' : return "4" ;
            case 'ÇT' : return "5" ;
            case 'ÇU' : return "6" ;
            case 'ÇV' : return "7" ;
            case 'ÇW' : return "8" ;
            case 'ÇX' : return "9" ;
            case 'ÅF' : return ":" ;
            case 'ÅG' : return ";" ;
            case 'ÅÉ' : return ( mode ) ? "&lt;" : "<" ;
            case 'ÅÅ' : return "=" ;
            case 'ÅÑ' : return ( mode ) ? "&gt;" : ">" ;
            case 'ÅH' : return "?" ;
            case 'Åó' : return "@" ;
            case 'Ç`' : return "A" ;
            case 'Ça' : return "B" ;
            case 'Çb' : return "C" ;
            case 'Çc' : return "D" ;
            case 'Çd' : return "E" ;
            case 'Çe' : return "F" ;
            case 'Çf' : return "G" ;
            case 'Çg' : return "H" ;
            case 'Çh' : return "I" ;
            case 'Çi' : return "J" ;
            case 'Çj' : return "K" ;
            case 'Çk' : return "L" ;
            case 'Çl' : return "M" ;
            case 'Çm' : return "N" ;
            case 'Çn' : return "O" ;
            case 'Ço' : return "P" ;
            case 'Çp' : return "Q" ;
            case 'Çq' : return "R" ;
            case 'Çr' : return "S" ;
            case 'Çs' : return "T" ;
            case 'Çt' : return "U" ;
            case 'Çu' : return "V" ;
            case 'Çv' : return "W" ;
            case 'Çw' : return "X" ;
            case 'Çx' : return "Y" ;
            case 'Çy' : return "Z" ;
            case 'ÅO' : return "^" ;
            case 'ÅQ' : return "_" ;
            case 'Åe' : return "`" ;
            case 'ÇÅ' : return "a" ;
            case 'ÇÇ' : return "b" ;
            case 'ÇÉ' : return "c" ;
            case 'ÇÑ' : return "d" ;
            case 'ÇÖ' : return "e" ;
            case 'ÇÜ' : return "f" ;
            case 'Çá' : return "g" ;
            case 'Çà' : return "h" ;
            case 'Çâ' : return "i" ;
            case 'Çä' : return "j" ;
            case 'Çã' : return "k" ;
            case 'Çå' : return "l" ;
            case 'Çç' : return "m" ;
            case 'Çé' : return "n" ;
            case 'Çè' : return "o" ;
            case 'Çê' : return "p" ;
            case 'Çë' : return "q" ;
            case 'Çí' : return "r" ;
            case 'Çì' : return "s" ;
            case 'Çî' : return "t" ;
            case 'Çï' : return "u" ;
            case 'Çñ' : return "v" ;
            case 'Çó' : return "w" ;
            case 'Çò' : return "x" ;
            case 'Çô' : return "y" ;
            case 'Çö' : return "z" ;
            case 'Åo' : return "{" ;
            case 'Åb' : return "|" ;
            case 'Åp' : return "}" ;
            case 'ÅB' : return "°" ;
            case 'Åu' : return "¢" ;
            case 'Åv' : return "£" ;
            case 'ÅA' : return "§" ;
            case 'ÅE' : return "•" ;
            case 'Å@' : return ( mode ) ? "&nbsp;" : " " ;
        }
        return new String( new char[]{ code } ) ;
    }

}
