/**
 *  This is a Java template for CS3230 - Programming Assignment 1 - Part 1
 *  (January-2015)
 *
 *  You are not required to follow the template. Feel free to modify any part.
 *
 *  Comment your code!
 *  
 *  Name: Yip Jiajie
 *  Matric no.: A0101924R
 *  Question Q1P2
 *  Collaborators: Yuan Bin
 */


import java.io.*;
import java.util.*;

class TemplatePart2 { // in Mooshak online judge, make sure that Java file name = class name that contains Main method
	
	public static int T, B;
	public static int CUT_OFF = 500;
	public static int MAX_NUMBER_OF_INTEGERS = 20005;
	public static int MAX_NUMBER_OF_INTEGERS_RESULTS = MAX_NUMBER_OF_INTEGERS * 2 + 1;
	

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
		// use this (a much faster output routine) instead of Java
		// System.out.println (slow)
		String V, M;

		T = sc.nextInt();

		for (int i = 1; i <= T; ++i) {
			
			B = sc.nextInt();
			sc.nextLine();
			V = sc.nextLine();
			M = sc.nextLine();
			
			int[] velocity = new int[MAX_NUMBER_OF_INTEGERS+1];
			int[] mass = new int[MAX_NUMBER_OF_INTEGERS+1];
			stringToIntArray(V, M, velocity, mass);			
			int[] result = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
			karatusbaMultiplication(velocity, mass, result);
			
			String out = intArrayToString(result); 

			pw.write(trimZeros(out));
			pw.write("\n");
		}
		pw.close(); // do not forget to use this
		sc.close();
	}
	
	/**
	 * 
	 * @param X
	 * @param Y
	 * @param ans
	 * 
	 * Karatsuba Multiplication
	 */
	private static void karatusbaMultiplication(int[] X, int[] Y, int ans[]){
		
		if(X[0] < CUT_OFF && Y[0] < CUT_OFF){
			multiply(X, Y, ans);
			return;
		}
		
		int R = Math.max(X[0], Y[0])/2;
		
		int[] xHigh = new int[MAX_NUMBER_OF_INTEGERS+1];
		int[] xLow = new int[MAX_NUMBER_OF_INTEGERS+1];
		int[] yHigh = new int[MAX_NUMBER_OF_INTEGERS+1];
		int[] yLow = new int[MAX_NUMBER_OF_INTEGERS+1];	
		splitsArrayIntoHalf(R, X, xHigh, xLow);
		splitsArrayIntoHalf(R, Y, yHigh, yLow);
		
		int[] z0 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		int[] z1 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		int[] z2 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		karatusbaMultiplication(xLow, yLow, z0);
		karatusbaMultiplication(xHigh, yHigh, z2);		
		
		int[] sumX = new int[MAX_NUMBER_OF_INTEGERS+1];
		int[] sumY = new int[MAX_NUMBER_OF_INTEGERS+1];	
		add(xHigh, xLow, sumX);
		add(yHigh, yLow, sumY);	
		karatusbaMultiplication(sumX, sumY, z1);	
		
		int[] zArray1 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		int[] zArray2 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];	
		subtract(z1, z2, zArray1);
		subtract(zArray1, z0, zArray2);	
		
		int[] zShift1 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		int[] zShift2 = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];		
		shift(zArray2, zShift1, R);
		shift(z2, zShift2, R*2);	
		
		int[] sum = new int[MAX_NUMBER_OF_INTEGERS_RESULTS];
		add(zShift1, zShift2, sum);
		add(sum, z0, ans);
	}
	
	/**
	 * 
	 * @param R
	 * @param current
	 * @param high
	 * @param low
	 * 
	 * Splits array into half
	 */
	private static void splitsArrayIntoHalf(int R, int[] current, int[] high, int[] low){
		int sizeL = 0;
		int sizeH = 0;
		
		if(current[0] <= R) {
			high[0] = 1;
			
			for(int i=1; i<R+1; i++){
				low[i] = current[i];
				sizeL += 1;
			}
			
			low[0] = sizeL;
		} else {
			for(int j=1; j<R+1; j++) {
				low[j] = current[j];
				sizeL += 1;
			}
			
			low[0] = sizeL;
			
			for(int k=R+1; k<current[0]+1; k++){
				high[k-R] = current[k];
				sizeH += 1;
			}
			
			high[0] = sizeH;
		}
	}
	
	/**
	 * 
	 * @param temp1
	 * @param temp2
	 * @param arrayResults
	 * 
	 * Add numbers from each array element by element
	 */
	private static void add(int[] temp1, int[] temp2, int[] arrayResults){
		int carry= 0;
		int sumLength = Math.max(temp1[0], temp2[0])+2;
	
    	for(int i=1; i<sumLength; i++) {
    		arrayResults[i] = temp1[i] + temp2[i] + carry;
    		carry = arrayResults[i]/B;
    		
    		if(arrayResults[i]>=10) {
    			arrayResults[i]=arrayResults[i]-10;	
    		}
    		
    		arrayResults[0] += 1;
 
    	}   
    }
	
	/**
	 * 
	 * @param temp1
	 * @param temp2
	 * @param arrayDifference
	 * 
	 * Subtract numbers from each array element by element
	 */
	private static void subtract(int[] temp1, int[] temp2, int[] arrayDifference){
		int ans = 0;
		
		if(temp2[0] == 1 && temp2[1] == 0){
    		for(int i = 1; i < temp1[0] + 1; i++){
    			arrayDifference[i] = temp1[i];
    		}
    		arrayDifference[0] = temp1[0];
		} else{
    		for(int j = 1; j < temp1[0] + 1; j++){	
    			ans = temp1[j] - temp2[j] + arrayDifference[j];
    			
    			if(ans < 0){	
    				arrayDifference[j] = 10 + ans;
    				arrayDifference[j+1] = -1;
    				arrayDifference[0] = j-1;
    			} else{
    				arrayDifference[j] = ans;
    				
    				if(ans == 0){
    					arrayDifference[0] = j - 1;
    				} else{
    					arrayDifference[0] = j;
    				}
    			}
    		}
    	}
	}

	/**
	 * 
	 * @param temp1
	 * @param temp2
	 * @param result
	 * 
	 * Multiply numbers from each array element by element
	 */
    public static void multiply(int[] temp1, int[] temp2, int[] result){
    	int product, remainder, carry;

    	for (int i = 1; i<temp1[0]+1; i++) {
    		for (int j = 1; j<temp2[0]+1; j++) {
    			product = temp1[i] * temp2[j];
    			remainder = (product + result[j+i-1]) % B;
    			carry =(product+result[j+i-1])/B;    		
    			result[j+i-1]  = remainder;
    			
				if(carry > 0 ){
					result[j+i] = carry + result[j+i];
					result[0] = j+i;
				} else {
					result[0] = j+i-1;
				}
			}	
    	}
    }
    
	/**
	 * 
	 * @param result
	 * @param shift
	 * @param shiftPosition
	 * 
	 * Shifts integers
	 */
	private static void shift(int[] result, int[] shift, int shiftPosition){
    	int size = result[0];
    	
    	if(size == 1 && result[1] == 0){
    		shift[0] = 0;
    	} else{
    		
    		for(int i = 0; i < size; i++){
    			shift[shiftPosition+1+i] = result[i+1];
    		}
    		
    		shift[0] = size+shiftPosition; 
    	}
	}
	
	/**
	 * 
	 * @param V
	 * @param M
	 * @param velocity
	 * @param mass
	 * 
	 * Converts String to array of integers
	 */
	private static void stringToIntArray(String V, String M, int[] velocity, int[] mass){
		
		for(int i=0; i<V.length(); i++){
			velocity[i+1] = parseDigit(V.charAt(V.length()-i-1));		
		}
		
		velocity[0] = V.length();
		
		for(int j=0; j<M.length(); j++) {
			mass[j+1] = parseDigit(M.charAt(M.length()-j-1));
		}
		
		mass[0] = M.length();
	}
	
	/**
	 * 
	 * @param result
	 * @return String of results
	 * 
	 * Converts array of integers to String
	 */
	private static String intArrayToString(int[] result) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<result[0]; i++) {
			sb.append(toDigit(result[result[0]-i]));
		}
		return sb.toString();
	}

	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length() - 1;
		int fp = input.indexOf('.');
		if (fp == -1) {
			fp = input.length();
		}

		while (left < fp - 1) {
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
			return "0" + input.substring(left, right + 1);
		return input.substring(left, right + 1);
	}

	private static int parseDigit(char c) {
		if (c <= '9') {
			return c - '0';
		}
		return c - 'A' + 10;
	}

	private static char toDigit(int digit) {
		if (digit <= 9) {
			return (char) (digit + '0');
		}
		return (char) (digit - 10 + 'A');
	}
}