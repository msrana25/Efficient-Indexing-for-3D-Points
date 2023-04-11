package KDtree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class App {

	public static void main(String args[])
	{
	List<double[]> points = new ArrayList<>();
	
	String inputFilePath = "E:\\Eclipse\\DBproject2\\src\\coordinates_large.csv";

	// Read points from the CSV file and insert them into the index
    	System.out.println("############################################################# File Reading ####################################################################");
    	System.out.println();
		System.out.println("File reading started");	
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] values = line.split(",");
					try {
						double[] point = new double[3];
						point[0] = Double.parseDouble(values[0]);
						point[1] = Double.parseDouble(values[1]);
						point[2] = Double.parseDouble(values[2]);
						points.add(point);

					} catch (NumberFormatException e) {
						continue;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        System.out.println("File reading done!!!");
	        System.out.println();
	        System.out.println("########################################################## File Reading Completed ##################################################################");
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println("################################################################ Build Tree ##################################################################");
	        System.out.println();
	        System.out.println("Starting with building the tree");
	        long startTime = System.currentTimeMillis(); 
			KDTree tree = new KDTree(points);
			long endTime = System.currentTimeMillis();
			System.out.println("Tree built successfully");
			System.out.println("Time taken to build the tree: "+ (endTime-startTime)+" milliseconds");
		    System.out.println();
	        System.out.println("############################################################### Tree Build Completed ##################################################################");
	        System.out.println("Number of nodes " + tree.display());
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println("############################################################### Range Query ###############################################################");
	        Scanner sc = new Scanner(System.in);
	        int file_index=1;
	        while(true)
	        {
	        System.out.println("Enter the lower bound of x, y and z coordinate values for finding range");
	        System.out.println("Enter lower bound of x");
	        double x_lower = sc.nextDouble();
	        System.out.println("Enter lower bound of y");
	        double y_lower = sc.nextDouble();
	        System.out.println("Enter lower bound of z");
	        double z_lower = sc.nextDouble();
	        System.out.println("Enter the upper bound of x, y and z coordinate values for finding range");
	        System.out.println("Enter upper bound of x");
	        double x_upper = sc.nextDouble();
	        System.out.println("Enter upper bound of y");
	        double y_upper = sc.nextDouble();
	        System.out.println("Enter upper bound of z");
	        double z_upper = sc.nextDouble();
	        sc.nextLine();
	        double[] min = {x_lower,y_lower,z_lower};
	        double[] max = {x_upper, y_upper,z_upper};
	        System.out.println("Starting with executing range query result between ( "+x_lower+", "+y_lower+ ", "+z_lower+" ) and ("+x_upper+", "+y_upper+ ", "+z_upper+ " )");
	        long rangeStart = System.currentTimeMillis();
	        List<double[]> rangeResult = tree.rangeQuery(min, max);
	        long rangeEnd = System.currentTimeMillis();
	        System.out.println("Range Result fetched in " +(rangeEnd -rangeStart) + " milliseconds");
	        System.out.println("Storing the result to output file");
	        String outputFilePath1 = "E:\\Eclipse\\DBproject2\\src\\";
	        String outputFileName =	"Q1-Output_KDT"+file_index+".csv";
	        file_index++;
	        int count =0;
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath1+outputFileName))) {
		        for (double[] point : rangeResult) {
		        	writer.write(String.format("%.2f,%.2f,%.2f%n", point[0], point[1], point[2]));
		        	count++;
		        	
		        }
		        writer.close();
		        System.out.println("Range output written and saved successfully with name "+outputFileName + " having records equal to " + count);
		        } catch (IOException e) {
		        	// TODO Auto-generated catch block
					e.printStackTrace();
	         }
	        
	        System.out.println("Do you want to check again for a different range (Y/N)");
	        String user_input = sc.nextLine();
	        if (user_input.equalsIgnoreCase ("N"))break;
	        
	        } 
	        System.out.println("###########################################################Range Query Completed ####################################################################################");
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println("########################################################### Nearest Neighbour Query ####################################################################################");
	        // Test kNN query
	        boolean flag = true;
	        double[] queryPoint = new double[3];
	        int file_index_2=1;
	        while(flag)
	        {
	        System.out.println("Enter the value of x, y and z coordinates of which you want to find the nearest neighbour for ");
	        System.out.println("Enter the value of x");
	        queryPoint[0]= sc.nextDouble();
	        System.out.println("Enter the value of y");
	        queryPoint[1]=sc.nextDouble();
	        System.out.println("Enter the value of z");
	        queryPoint[2]=sc.nextDouble();
	        sc.nextLine();
	        System.out.println("Starting with executing NN query result for coordinates ( "+queryPoint[0]+", "+queryPoint[1]+", "+queryPoint[2]+" )");
	        long startTime_NN = System.currentTimeMillis();
	        List<double[]> kNNResults = tree.findNearestNeighbors(queryPoint);
			long endTime_NN = System.currentTimeMillis();
			System.out.println("NN result fetched in " +(endTime_NN -startTime_NN) + " milliseconds");
	        String outputFilePath1 = "E:\\Eclipse\\DBproject2\\src\\";
	        String outputFileName =	"Q1-Output_NN"+file_index_2+".csv";
	        file_index_2++;
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath1+outputFileName))) {
	        	 	System.out.println("NN of the entered coordinate are ");
	        		for (double[] kNNResult: kNNResults )
	        		{
		        	writer.write(String.format("%.2f,%.2f,%.2f%n", kNNResult[0], kNNResult[1], kNNResult[2]));
			        System.out.println(Arrays.toString(kNNResult));

	        		}
	        		
	        		writer.close();
		        } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        System.out.println("Do you want to check NN again for a different coordinate (Y/N)");
	        String inp = sc.nextLine();
	        if (inp.equalsIgnoreCase("N")) break;
	        }
	        System.out.println("###########################################################Nearest Neighbour Query Completed ##########################################################################");
	        System.out.println();
	        System.out.println();
	        System.out.println();
	        System.out.println("########################################################### Index Size Enquiry  ##########################################################################");
	        System.out.println("Size of index created is: "+ tree.sizeOfIndex()/(1024*1024) + " MB");
	        System.out.println("########################################################### Program Finished  ##########################################################################");
	        sc.close();
	        
	    }
	}
