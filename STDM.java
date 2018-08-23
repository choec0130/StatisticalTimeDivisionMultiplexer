import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//SourceA: 1 2 A1, 4 5 A2
// 9 characters, then two ints, then one string till comma, REPEAT
//etc.

//frame time duration, data block, and address
public class STDM {
	
	public static final double BIT_RATE = 50.0; //say this is in bits
	public static final int MAX_BLOCKS_OUTPUT_BUFFER = 3;
	
	static int numberOfInputSources = 0;
	static int numberOfFramesOneStream = 0;
	int startTime, endTime;
	static int lastEndTime;
	static double totalFrameRate = 0.0;
	static double averageTransmitionRate = 0.0;
	String addressBits;
	
	public static void main(String[] args) {
		
	    System.out.print("Enter the file name with extension: ");

	    Scanner myScanner = new Scanner(System.in);

        File file = new File(myScanner.nextLine());
        
        try {
			myScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found.");
		}
		
		ArrayList<Integer> startTimes = new ArrayList<Integer>();
		ArrayList<Integer> endTimes = new ArrayList<Integer>();
		ArrayList<String> blockNames = new ArrayList<String>();
		ArrayList<Integer> tokensInStream = new ArrayList<Integer>();
		LinkedList<String> inputBuffer = new LinkedList<String>();
		LinkedList<String> outputBuffer = new LinkedList<String>();
		
		double transmitionRate = 0;
		
		while (myScanner.hasNextLine()) {
			numberOfInputSources++;
			
			int numberOfTokensInStream = 0;
	
			String currentString = myScanner.nextLine();
			String myString = currentString.substring(9); //to account for SourceX: 
			Scanner parsedScanner = new Scanner(myString);
			while(parsedScanner.hasNext() == true) {
				
				numberOfTokensInStream++;
				
				int startTime = parsedScanner.nextInt();
				startTimes.add(startTime);
				
				int endTime = parsedScanner.nextInt();
				endTimes.add(endTime);
				String blockName = parsedScanner.next();
				if(parsedScanner.hasNext()) {
					int endIndex = blockName.indexOf(',');
					blockName = blockName.substring(0, endIndex);
					blockNames.add(blockName);
				} else {
					blockNames.add(blockName);
				}
					
			}

			tokensInStream.add(numberOfTokensInStream);
		}
		
		int endOfStream = endTimes.get(endTimes.size()-1) + 1;
		
		for (int i = 0; i < tokensInStream.size(); i++) {
			transmitionRate = transmitionRate + ((double) tokensInStream.get(i)/(double) endOfStream) * BIT_RATE;	
		}
	
		int headerBitsNeeded = (int) (Math.log(powerOfTwo(numberOfInputSources))/Math.log(2));
		
		System.out.println("I set the original bit rate for each stream to be equal to: " + BIT_RATE);
		System.out.println("The transmition rate calculated for this file with repspect to the original bit rate of " + BIT_RATE +  " is: " + transmitionRate);
		System.out.println("The number of bits needed for addressing each stream is: " + headerBitsNeeded*MAX_BLOCKS_OUTPUT_BUFFER);
		System.out.println("The frame time duration is " + endOfStream + " time units.");
		
		for (int i = 0; i < endOfStream; i++) {
			System.out.println("We are currently in slot " + i + ".");
			int count = 0;
			
			for (int j = 0; j < startTimes.size() - 1; j++) {
				
				while(!inputBuffer.isEmpty() && count < MAX_BLOCKS_OUTPUT_BUFFER-1) {
					outputBuffer.add(inputBuffer.get(0));
					System.out.println("Delivered: " + inputBuffer.get(0));
					int alphabetCount = 0;
					
					for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
						alphabetCount++;
					    if (inputBuffer.get(0).toString().toCharArray()[0] == alphabet) {
					    	break;
					    }
					}
					
					System.out.println("Address of delivered block is: " + Integer.toBinaryString(alphabetCount-1));
					inputBuffer.remove(0);
					count++;
				}
				if (startTimes.get(j) == i && count < MAX_BLOCKS_OUTPUT_BUFFER-1) {
					outputBuffer.add(blockNames.get(j));
					System.out.println("Delivered: " + blockNames.get(j));
					int alphabetCount = 0;
					
					for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
						alphabetCount++;
					    if (blockNames.get(j).toString().toCharArray()[0] == alphabet) {
					    	break;
					    }
					}
					
					System.out.println("Address of delivered block is: " + Integer.toBinaryString(alphabetCount-1));
					
					
					count++;
				} else if (startTimes.get(j) == i) {
					inputBuffer.add(blockNames.get(j));
					System.out.println("Queued: " + blockNames.get(j));
				}
			}
		}
		
		//use headerBitsNeeded and i in binary to get address
		
		//include flag, address, and time duration of frame in output
		//frame time duration, data block, address
		
		
}
		static int powerOfTwo(int x) {
			x = x - 1;
			x |= x >> 1;
			x |= x >> 2;
			x |= x >> 4;
			x |= x >> 8;
			x |= x >> 16;
			return x + 1;
		}
		
		double calculateFrameTimeDuration() {
			return 0.0;
		}
		
}
