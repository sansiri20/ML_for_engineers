package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class FileHelper {

	public Vector readFile(String fileName) throws IOException {

		Scanner file=new Scanner(new FileReader(fileName));
		
		Vector network = new Vector();

		// read each line of text file
		while(file.hasNextLine()) {
			Link link = new Link();
			int row = 0;
			int col = 0;	
			for(int i=0; i<3; i++)
			{
				if (col==0) {
					link.setNode1((int)file.nextInt());
				}
				if (col==1) {
					link.setNode2((int)file.nextInt());
				}
				if (col==2) {
					link.setCost((double)file.nextDouble());
				}
				col++;
			}
			link.setActive(0);
			col = 0;
			row++;
			network.add(link);
		}
		return network;
	}

}
