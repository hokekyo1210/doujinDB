package hokekyo1210.dojindb.util;

import java.io.UnsupportedEncodingException;

public class LevenshteinDistance {
	
	private static int dp[][] = new int[60][60];
	
	public static double edit(String px_, String py_) throws UnsupportedEncodingException
	  {
		String px = px_.toLowerCase().replaceAll("\\ ", "").replaceAll("�@", "");
		String py = py_.toLowerCase().replaceAll("\\ ", "").replaceAll("�@", "");
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
            case '�I' : return "!" ;
            case '�h' : return ( mode ) ? "&quot;" : "\"" ;
            case '��' : return "#" ;
            case '��' : return "$" ;
            case '��' : return "\\" ;
            case '��' : return "%" ;
            case '��' : return "&" ;
            case '�f' : return "\'" ;
            case '�i' : return "(" ;
            case '�j' : return ")" ;
            case '��' : return "*" ;
            case '�{' : return "+" ;
            case '�C' : return "," ;
            case '�|' : return "-" ;
            case '�D' : return "." ;
            case '�^' : return "/" ;
            case '�O' : return "0" ;
            case '�P' : return "1" ;
            case '�Q' : return "2" ;
            case '�R' : return "3" ;
            case '�S' : return "4" ;
            case '�T' : return "5" ;
            case '�U' : return "6" ;
            case '�V' : return "7" ;
            case '�W' : return "8" ;
            case '�X' : return "9" ;
            case '�F' : return ":" ;
            case '�G' : return ";" ;
            case '��' : return ( mode ) ? "&lt;" : "<" ;
            case '��' : return "=" ;
            case '��' : return ( mode ) ? "&gt;" : ">" ;
            case '�H' : return "?" ;
            case '��' : return "@" ;
            case '�`' : return "A" ;
            case '�a' : return "B" ;
            case '�b' : return "C" ;
            case '�c' : return "D" ;
            case '�d' : return "E" ;
            case '�e' : return "F" ;
            case '�f' : return "G" ;
            case '�g' : return "H" ;
            case '�h' : return "I" ;
            case '�i' : return "J" ;
            case '�j' : return "K" ;
            case '�k' : return "L" ;
            case '�l' : return "M" ;
            case '�m' : return "N" ;
            case '�n' : return "O" ;
            case '�o' : return "P" ;
            case '�p' : return "Q" ;
            case '�q' : return "R" ;
            case '�r' : return "S" ;
            case '�s' : return "T" ;
            case '�t' : return "U" ;
            case '�u' : return "V" ;
            case '�v' : return "W" ;
            case '�w' : return "X" ;
            case '�x' : return "Y" ;
            case '�y' : return "Z" ;
            case '�O' : return "^" ;
            case '�Q' : return "_" ;
            case '�e' : return "`" ;
            case '��' : return "a" ;
            case '��' : return "b" ;
            case '��' : return "c" ;
            case '��' : return "d" ;
            case '��' : return "e" ;
            case '��' : return "f" ;
            case '��' : return "g" ;
            case '��' : return "h" ;
            case '��' : return "i" ;
            case '��' : return "j" ;
            case '��' : return "k" ;
            case '��' : return "l" ;
            case '��' : return "m" ;
            case '��' : return "n" ;
            case '��' : return "o" ;
            case '��' : return "p" ;
            case '��' : return "q" ;
            case '��' : return "r" ;
            case '��' : return "s" ;
            case '��' : return "t" ;
            case '��' : return "u" ;
            case '��' : return "v" ;
            case '��' : return "w" ;
            case '��' : return "x" ;
            case '��' : return "y" ;
            case '��' : return "z" ;
            case '�o' : return "{" ;
            case '�b' : return "|" ;
            case '�p' : return "}" ;
            case '�B' : return "�" ;
            case '�u' : return "�" ;
            case '�v' : return "�" ;
            case '�A' : return "�" ;
            case '�E' : return "�" ;
            case '�@' : return ( mode ) ? "&nbsp;" : " " ;
        }
        return new String( new char[]{ code } ) ;
    }

}
