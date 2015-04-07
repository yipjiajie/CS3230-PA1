/**
 *  This is a Java template for CS3230 - Programming Assignment 1 - Part 1
 *  (January-2015)
 *
 *  You are not required to follow the template. Feel free to modify any part.
 *
 *  Comment your code!
 *  
 *  Author: Yip Jiajie (A0101924R)
 *  
 *  References:
 *  http://rosettacode.org/wiki/Long_multiplication#Java
 *  http://stackoverflow.com/questions/5318068/very-large-numbers-in-java-without-using-java-math-biginteger
 *  
 *  Collaborators:
 *  Ashmawi
 *  Jayden
 *  Leonardo
 */


import java.io.*;
import java.util.*;


class Template { // in Mooshak online judge, make sure that Java file name = class name that contains Main method
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // use this (a much faster output routine) instead of Java System.out.println (slow)
        
        int T, B;
		String V, M; 
        T = sc.nextInt();
        int radixCounter1 = 0; 
        int radixCounter2 =0;
        boolean leadingZeroes = false;
		
        for (int i = 1; i <= T; ++i) {
            B = sc.nextInt();	// Radix base
			sc.nextLine();
			V = sc.nextLine(); M = sc.nextLine();	// Velocity and Mass
			
			// Insert solution here.
	    	// Checks if Velocity and Mass contains radix, stores the position of the radix into radixCounter for future computation
	    	if(V.contains(".")){
	    		radixCounter1 += V.length() - V.indexOf(".") -1;
	    		V = V.replace(".", ""); 	    	
	    		if(V.charAt(0) == '0'){
	        		leadingZeroes = true;
	        	}
	    	}
	    	
	    	if(M.contains(".")){
	    		radixCounter2 += M.length() - M.indexOf(".") -1;
	    		M = M.replace(".", "");
	    		if(M.charAt(0) == '0'){
	        		leadingZeroes = true;
	        	}
	    	}
	    		    	
			String P = calculateMomentum (V,M,B,radixCounter1+radixCounter2,leadingZeroes);
			
			// Reset
			leadingZeroes = false;
			radixCounter1 = 0;
			radixCounter2 = 0;
			
			pw.write(trimZeros(P));
			pw.write("\n");
        }
        pw.close(); // do not forget to use this
    }
	
    private static String calculateMomentum(String Velocity, String Mass, int base, int radixCounter, boolean leadingZeroes){
    	
    	char v[] = Velocity.toCharArray();
    	char m[] = Mass.toCharArray();
    	char[] V = new char[Velocity.length()];
    	char[] M = new char[Mass.length()];
    	int p[] = new int[10010];
    	char[] tempResult = new char[10010]; 	

    	
		reverseArray(v, V);
       	reverseArray(m, M); 	
    	computeMomentumInitialResult(p, V, M);
    	computeMomentumFinalResult(base, p, V, M);
    	reverseArray(p, tempResult);
    	
    	String P = new String(tempResult);
    	P = addRadixToFinalResult(radixCounter, P, leadingZeroes);
    	
    	
    	return P;
    }
    
    /**
     * Adds radix if there is radix in V/M
     * @param radixCounter is the counter for the decimal points in Velocity and Mass
     * @param P is the final result string
     * @return
     */
	private static String addRadixToFinalResult(int radixCounter, String P, boolean leadingZeroes) {		
		if(radixCounter != 0){
			if(leadingZeroes){
				P = P.substring(0, P.length()-radixCounter) + "." + P.substring(P.length()-radixCounter, P.length());
			} else {
				P = trimZeros(P);
				P = P.substring(0, P.length()-radixCounter) + "." + P.substring(P.length()-radixCounter, P.length());
			}
		}
		return P;
	}

    /**
     * Deals with the carry and quotient of each element in array p
     * @param base is the base specific by the user
     * @param p is the int array which contains initial momentum results
     * @param V is the char array which contains velocity
     * @param M is the char array which contains momentum
     */
	private static void computeMomentumFinalResult(int base, int[] p, char[] V,
			char[] M) {
		for(int i=0; i<V.length + M.length; i++){
    		int initialResult = p[i];
    		p[i] = initialResult % base;
    		p[i+1] += initialResult / base;
    		
    	}
	}
    
    /**
     * Multiplication for each element in array V and M and placing
     * result in the appropriate spot in array p
     * @param p is the int array to store results
     * @param V is the char array which contains velocity
     * @param M is the char array which contains momentum
     */
	private static void computeMomentumInitialResult(int[] p, char[] V, char[] M) {
		for(int i=0; i<V.length; i++){
    		for(int j=0; j<M.length; j++){
    			p[i+j] += parseDigit(V[i]) * parseDigit(M[j]);
    		}
    	}
	}
    
    /**
     * Reverses the array elements 
     * @param p is the initial int array
     * @param tempResult is the final char array after reversing
     */
	private static void reverseArray(int[] p, char[] tempResult) {
		for(int i=0; i<p.length; i++){
    		tempResult[i] = toDigit(p[p.length-i-1]);
    	}
	}
    
    /**
     * Reverses the array elements
     * @param v is the initial char array
     * @param V is the final char array after reversing
     */
	private static void reverseArray(char[] a, char[] A) {
		for(int i=0; i<a.length; i++){
    		A[i] = a[a.length-i-1];
    	}
	}

 
	
	/**
	 * Use to trim leading and trailing zeros on a result string.
	 */
	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length()-1;
		int fp = input.indexOf('.');
		if (fp == -1) {
			fp = input.length();
		}
		
		while(left < fp-1) {
			if (input.charAt(left) != '0')
				break;
			left++;
		}
		
		while (right >= fp) {
			if (input.charAt(right) != '0') {
				if (input.charAt(right) == '.')
					right--;
				break;
			}
			right--;
		}
		
		if (left >= fp)
			return "0" + input.substring(left,right+1);
		return input.substring(left,right+1);
	}
    
	/**
	 * Convert digit to int (for reading)
	 */
	private static int parseDigit(char c) {
		if (c <= '9') {
			return c - '0';
		} 
		return c - 'A' + 10;
	}
	
	/**
	 * Convert int to digit. (for printing)
	 */
	private static char toDigit(int digit) {
		if (digit <= 9) {
			return (char)(digit + '0');
		} 
		return (char)(digit - 10 + 'A');
	}
}
