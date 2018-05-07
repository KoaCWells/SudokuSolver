
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Sodoku 
{
	static int[][] Board = new int[9][9];
	static int[][] AllDiffs = new int[27][9];
	static int[][] Domains = new int[81][9];
	static int[] NakedPair_A = new int[2];
	static int[] NakedPair_B = new int[2];
	static int[] NakedTriple_A = new int[3];
	static int[] NakedTriple_B = new int[3];
	static int[] NakedTriple_C = new int[3];
	static int[][] boxes =  {{0,1,2,9,10,11,18,19,20},
							{3,4,5,12,13,14,21,22,23},
							{6,7,8,15,16,17,24,25,26},
							{27,28,29,36,37,38,45,46,47},
							{30,31,32,39,40,41,48,49,50},
							{33,34,35,42,43,44,51,52,53},
							{54,55,56,63,64,65,72,73,74},
							{57,58,59,66,67,68,75,76,77},
							{60,61,62,69,70,71,78,79,80}};
	
	static void printBoard()
	{
		System.out.println("Board:");
		for(int i =0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(j == 3 || j == 6)
					System.out.print("| " + Board[i][j]+" ");
				else
					System.out.print(Board[i][j]+" ");
			}
			System.out.println();
			if(i == 2 || i == 5)
			{
				for(int k = 0; k < 11; k++)
				{
					System.out.print("- ");
				}
				System.out.println();
			}
		}
	}
	
	static void printAllDiffs()
	{
		System.out.println("\nAllDiffs: \nROWS");
		for(int i =0; i < 27; i++)
		{
			if(i == 9)
				System.out.println("COLUMNS");
			if(i==18)
				System.out.println("BOXES");
			for(int j = 0; j < 9; j++)
			{
				System.out.print(AllDiffs[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	static void printDomains()
	{
		int l = -1, m = 0;
		System.out.println("\nDomains:");
		for(int i =0; i < 81; i++)
		{
			if(i%9 == 0)
				l++;
			if(m == 9)
				m = 0;
			System.out.print(i+": Entry("+l+","+m+") ");
			for(int j = 0; j < 9; j++)
				System.out.print(Domains[i][j]+" ");
			System.out.println();
			m++;
		}
	}
	
	static void setDomains(int row, int col, int el)
	{
		for(int k = 0; k < 9; k++) //zero out domain
		{
			if(el != (k+1))
				Domains[9 * row + col][k] = 0;
		} 
	}
	
	static void setConstraints(int row, int col, int el)
	{
		//ROWS
		AllDiffs[row][col] = el; 
		//COLUMNS
		AllDiffs[col + 9][row] = el;
		//BOXES
		if(row >= 0 && row <= 2)
		{
			if(col >= 0 && col <= 2)
			{
				//box 0
				AllDiffs[18][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 1
				AllDiffs[19][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 2
				AllDiffs[20][(3 * (row % 3) + (col % 3))] = el;
			}
		}
		if(row >= 3 && row <= 5)
		{
			if(col >= 0 && col <= 2)
			{
				//box 3
				AllDiffs[21][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 4
				AllDiffs[22][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 5
				AllDiffs[23][(3 * (row % 3) + (col % 3))] = el;
			}
		}
		if(row >= 6 && row <= 8)
		{
			if(col >= 0 && col <= 2)
			{
				//box 6
				AllDiffs[24][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 7
				AllDiffs[25][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 8
				AllDiffs[26][(3 * (row % 3) + (col % 3))] = el;
			}
		}
	}
	
	static void updateDomains(int r, int c, int val)
	{	
		//ROWS
		for(int i=0; i< 9;i++)
		{
			if(9 * r + i != 9 * r + c)
			{
				Domains[9 * r + i][val - 1] = 0;
			}
		}
		//COLUMNS
		for(int i=0; i< 9;i++)
		{
			if(9 * i + c != 9 * r + c)
			{
				Domains[9 * i + c][val - 1] = 0;
			}
		}
		//BOXES
		if(r >= 0 && r <= 2)
		{
			if(c >= 0 && c <= 2)
			{
				//box 0
				for(int i=0;i<=2;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 1
				for(int i=0;i<=2;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 2
				for(int i=0;i<=2;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
		if(r >= 3 && r <= 5)
		{
			if(c >= 0 && c <= 2)
			{
				//box 3
				for(int i=3;i<=5;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 4
				for(int i=3;i<=5;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 5
				for(int i=3;i<=5;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
		if(r >= 6 && r <= 8)
		{
			if(c >= 0 && c <= 2)
			{
				//box 6
				for(int i=6;i<=8;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 7
				for(int i=6;i<=8;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 8
				for(int i=6;i<=8;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
	}
	
	static void updateRowDomains(int row, int domains_index1, int domains_index2, int idx1, int idx2)
	{
		for(int i = 9*row; i < 9*row + 9;i++)
		{
			if(i != 9*row + domains_index1 && i != 9*row + domains_index2) {
				Domains[i][idx1] = 0;
				Domains[i][idx2] = 0;
			}
		}
	}
	
	static void updateColDomains(int col, int domains_index1, int domains_index2, int idx1, int idx2)
	{
		for(int i = col; i < 81;i += 9)
		{ 
			if(i != domains_index1 && i != domains_index2) {
				Domains[i][idx1] = 0;
				Domains[i][idx2] = 0;
			}
		}
	}
	
	static void updateBoxDomains(int box, int domains_index1, int domains_index2, int idx1, int idx2) {
		for(int i = 0; i < 9; i++) {
				if(boxes[box][i] != domains_index1 && boxes[box][i] != domains_index2) {
					Domains[boxes[box][i]][idx1] = 0;
					Domains[boxes[box][i]][idx2] = 0;
			}
		}
	}
	
	static void updateRowDomainsTriple(int row, int domains_index1, int domains_index2, int domains_index3, int idx1, int idx2, int idx3)
	{
		for(int i = 9*row; i < 9*row + 9;i++)
		{
			if(i != 9*row + domains_index1 && i != 9*row + domains_index2 && i != 9*row + domains_index3) {
				Domains[i][idx1] = 0;
				Domains[i][idx2] = 0;
				Domains[i][idx3] = 0;
			}
		}
	}
	
	static void updateColDomainsTriple(int col, int domains_index1, int domains_index2, int domains_index3, int idx1, int idx2, int idx3)
	{
		for(int i = col; i < 81;i += 9)
		{ 
			if(i != domains_index1 && i != domains_index2 && i != domains_index3) {
				Domains[i][idx1] = 0;
				Domains[i][idx2] = 0;
				Domains[i][idx3] = 0;
			}
		}
	}
	
	static void updateBoxDomainsTriple(int[] box, int domains_index1, int domains_index2, int domains_index3, int idx1, int idx2, int idx3) {
		for(int i = 0; i < 9; i++) {
			if(box[i] != domains_index1 && box[i] != domains_index2 && box[i] != domains_index3) {
				Domains[box[i]][idx1] = 0;
				Domains[box[i]][idx2] = 0;
				Domains[box[i]][idx3] = 0;
			}
		}
	}
	
	static void findSingleDomain(int row, int col)
	{
		int val = 0,count = 0;
		for(int j = 0;j<9;j++)
		{
			if(Domains[9 * row + col][j] > 0)
			{
				count++;
				val = Domains[9 * row + col][j];
			}
		}	
		if(count == 1 && Board[row][col] == 0 && val != 0)
		{
			System.out.println("New entry - ("+row+","+col+"): " + val);
				Board[row][col] = val;
				setConstraints(row, col, val);
				setDomains(row, col, val);
		}
	}
	
	static void findNakedDoubleRows() {
		int counter = 0;
		int row = 0;
		int domains_index1 = 0;
		int domains_index2 = 0;
		int idx1 = 0;
		int idx2 = 0;
		int[] temp = new int[9];
		boolean double_found = false;
		
		for(int i = 0; i < 81; i++){//board locations
			if(i%9 == 0 && i > 0) {//new row
				row++;
				counter = 0;
				domains_index1 = 0;
				domains_index2 = 0;
				idx1 = 0;
				idx2 = 0;
				temp = null;
				double_found = false;
			}
			
			if(double_found == false) {
				for(int j = 0; j < 9; j++) {//possible domain values [1,9]
					if(Domains[i][j] > 0 && Domains[i][j] < 10) {
						counter++;
						
						if(counter == 1) {
							domains_index1 = i;
							idx1 = j;
						}
						
						else if(counter == 2) {
							domains_index2 = i;
							idx2 = j;
						}
						else if(counter > 2) { //
							counter = 0;
							domains_index1 = 0;
							domains_index2 = 0;
							idx1 = 0;
							idx2 = 0;
							temp = null;
							break;
						}
						if(j == 8) {
							temp = Domains[i];
							double_found = true;
						}
					}
				}
			}
			else {
				if(temp.equals(Domains[i]))
				{
					updateRowDomains(row, domains_index1, domains_index2, idx1, idx2);
					double_found = false;
					temp = null;
				}
			}
		}
	}
	
	static void findNakedDoubleCols() {
		int counter = 0;
		int col = 0;
		int domains_index1 = 0;
		int domains_index2 = 0;
		int idx1 = 0;
		int idx2 = 0;
		int[] temp = new int[9];
		boolean double_found = false;
		
		for(int i = col; i < 81; i = i + 9){//board locations
			if(i < 9) {//new row
				counter = 0;
				domains_index1 = 0;
				domains_index2 = 0;
				idx1 = 0;
				idx2 = 0;
				temp = null;
				double_found = false;
			}
			
			if(double_found == false) {
				for(int j = 0; j < 9; j++) {//possible domain values [1,9]
					if(Domains[i][j] > 0 && Domains[i][j] < 10) {
						counter++;
						
						if(counter == 1) {
							domains_index1 = i;
							idx1 = j;
						}
						
						else if(counter == 2) {
							domains_index2 = i;
							idx2 = j;
						}
						else if(counter > 2) { //
							counter = 0;
							domains_index1 = 0;
							domains_index2 = 0;
							idx1 = 0;
							idx2 = 0;
							temp = null;
							break;
						}
						if(j == 8) {
							temp = Domains[i];
							double_found = true;
						}
					}
				}
			}
			else {
				if(temp.equals(Domains[i]))
				{
					updateColDomains(col, domains_index1, domains_index2, idx1, idx2);
					double_found = false;
					temp = null;
				}
			}
			if(i > 71 && i != 80) {
				col++;
				i = -9 + col;
			}
		}
	}
	
	static void findNakedDoubleBoxes() {
		int counter = 0;
		int box = -1;
		int domains_index1 = -1;
		int domains_index2 = -1;
		int idx1 = -1;
		int idx2 = -1;
		int[] temp = new int[9];
		boolean double_found = false;
		
		for(int i = 0;i< 9;i++)
		{
			domains_index1 = -1;
			domains_index2 = -1;
			idx1 = -1;
			idx2 = -1;
			temp = null;
			box++;
			for(int j = 0; j < 9; j++) {
				if(double_found == false) {
					for(int k = 0; k < 9; k++)
					{
						if(Domains[boxes[i][j]][k] > 0 && Domains[boxes[i][j]][k] < 10) {
							counter++;
								
							if(counter == 1) {
								domains_index1 = boxes[i][j];
								idx1 = j;
							}
								
							else if(counter == 2) {
								domains_index2 = boxes[i][j];
								idx2 = j;
							}
									
							else if(counter > 2) {
								counter = 0;
								domains_index1 = 0;
								domains_index2 = 0;
								idx1 = 0;
								idx2 = 0;
								temp = null;
							}
									
							else if(j == 8) {
								temp = Domains[boxes[i][j]];
								double_found = true;
							}
						}
					}
				}
				else
				{
					if(temp.equals(Domains[boxes[i][j]]))
					{
						updateBoxDomains(box,domains_index1,domains_index2,idx1,idx2);
					}
				}
			}
		}
	}
	
	static void findNakedTriplesBoxes() {
		int counter = 0;
		int box = 0;
		int domains_index1 = -1;
		int domains_index2 = -1;
		int domains_index3 = -1;
		int idx1 = -1;
		int idx2 = -1;
		int idx3 = -1;
		int[] triple = new int[3];
		int triple_count = 0;
		int[] temp = new int[9];
		boolean triple_found = false;
		
		for(int i = 0;i< 9;i++)
		{//board locations
				//new box
				box++;
				counter = 0;
				domains_index1 = -1;
				domains_index2 = -1;
				domains_index3 = -1;
				idx1 = -1;
				idx2 = -1;
				idx3 = -1;
				triple_count = 0;
				
				if(triple_found == false) {
					for(int j = 0; j < 9; j++) {//possible domain values [1,9]
						if(Domains[boxes[box][i]][j] > 0 && Domains[boxes[box][i]][j] < 10) {
							counter++;
								
							if(counter == 1) {
								domains_index1 = i;
								idx1 = j;
								triple[0] = Domains[boxes[box][i]][j];
								triple_count++;
							}
								
							else if(counter == 2) {
								idx2 = j;
								triple[1] = Domains[boxes[box][i]][j];
								triple_count++;
							}
							
							else if(counter == 3)
							{
								idx3 = j;
								triple[2] = Domains[boxes[box][i]][j];
								triple_count++;
							}
							else if(counter > 3) { //
								counter = 0;
								domains_index1 = -1;
								idx1 = -1;
								idx2 = -1;
								idx3 = -1;
								triple_count = 0;
								break;
							}
							
							else if((counter == 2 || counter == 3) && j == 8)
							{
								triple_found = true;
							}
						}
					}
						
				}
				
				else
				{
					for(int j = 0; j < 9; j++)
					{
						if (triple_count == 2 && Domains[i][j] != 0)
						{
							if(Domains[boxes[box][i]][j] != triple[0] && Domains[boxes[box][i]][j] != triple[1])
							{
								triple_count++;
								triple[2] = Domains[boxes[box][i]][j];
							}
							
							else
							{
								if(domains_index2 == -1)
								{
									domains_index2 = i;
								}
								
								else if(domains_index3 == -1)
								{
									domains_index3 = i;
									updateRowDomainsTriple(box, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
								}
							}
						}
						
						else if(triple_count == 3)
						{
							if(Domains[boxes[box][i]][j] != triple[0] && Domains[boxes[box][i]][j] != triple[1] && Domains[boxes[box][i]][j] != triple[2])
							{
								break;
							}
							
							else
							{
								if(domains_index2 == -1)
								{
									domains_index2 = i;
								}
								
								else if(domains_index3 == -1)
								{
									domains_index3 = i;
									updateRowDomainsTriple(box, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
								}
							}
						}
					}	
				}
			}
	}
	
	static void findNakedTriplesRows() {
		int counter = 0;
		int row = 0;
		int domains_index1 = -1;
		int domains_index2 = -1;
		int domains_index3 = -1;
		int idx1 = -1;
		int idx2 = -1;
		int idx3 = -1;
		int[] triple = {0, 0, 0};
		int triple_count = 0;
		boolean triple_found = false;
		
		for(int i = 0; i < 81; i++){//board locations
			if(i%9 == 0 && i > 0) {//new row
				row++;
				counter = 0;
				domains_index1 = -1;
				domains_index2 = -1;
				domains_index3 = -1;
				idx1 = -1;
				idx2 = -1;
				idx3 = -1;
				triple_count = 0;
			}
			if(triple_found == false) {
				for(int j = 0; j < 9; j++) {//possible domain values [1,9]
					if(Domains[i][j] > 0 && Domains[i][j] < 10) {
						counter++;
							
						if(counter == 1) {
							domains_index1 = i;
							idx1 = j;
							triple[0] = Domains[i][j];
							triple_count++;
						}
							
						else if(counter == 2) {
							idx2 = j;
							triple[1] = Domains[i][j];
							triple_count++;
						}
						
						else if(counter == 3)
						{
							idx3 = j;
							triple[2] = Domains[i][j];
							triple_count++;
						}
						else if(counter > 3) { //
							counter = 0;
							domains_index1 = -1;
							idx1 = -1;
							idx2 = -1;
							idx3 = -1;
							triple_count = 0;
							break;
						}
						
						else if((counter == 2 || counter == 3) && j == 8)
						{
							triple_found = true;
						}
					}
				}
					
			}
			
			else
			{
				for(int j = 0; j < 9; j++)
				{
					if (triple_count == 2 && Domains[i][j] != 0)
					{
						if(Domains[i][j] != triple[0] && Domains[i][j] != triple[1])
						{
							triple_count++;
							triple[2] = Domains[i][j];
						}
						
						else
						{
							if(domains_index2 == -1)
							{
								domains_index2 = i;
							}
							
							else if(domains_index3 == -1)
							{
								domains_index3 = i;
								updateRowDomainsTriple(row, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
							}
						}
					}
					
					else if(triple_count == 3)
					{
						if(Domains[i][j] != triple[0] && Domains[i][j] != triple[1] && Domains[i][j] != triple[2])
						{
							break;
						}
						
						else
						{
							if(domains_index2 == -1)
							{
								domains_index2 = i;
							}
							
							else if(domains_index3 == -1)
							{
								domains_index3 = i;
								updateRowDomainsTriple(row, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
							}
						}
					}
				}	
			}
		}
	}
	
	static void findNakedTriplesCols() {
		int counter = 0;
		int col = 0;
		int domains_index1 = -1;
		int domains_index2 = -1;
		int domains_index3 = -1;
		int idx1 = -1;
		int idx2 = -1;
		int idx3 = -1;
		int[] triple = {0, 0, 0};
		int triple_count = 0;
		boolean triple_found = false;
		
		for(int i = col; i < 81; i = i + 9){//board locations			
			if(i < 9) {//new col
				counter = 0;
				domains_index1 = -1;
				domains_index2 = -1;
				domains_index3 = -1;
				idx1 = -1;
				idx2 = -1;
				idx3 = -1;
				triple_found = false;
			}
			
			if(triple_found == false) {
				for(int j = 0; j < 9; j++) {//possible domain values [1,9]
					if(Domains[i][j] > 0 && Domains[i][j] < 10) {
						counter++;
							
						if(counter == 1) {
							domains_index1 = i;
							idx1 = j;
							triple[0] = Domains[i][j];
							triple_count++;
						}
							
						else if(counter == 2) {
							idx2 = j;
							triple[1] = Domains[i][j];
							triple_count++;
						}
						
						else if(counter == 3)
						{
							idx3 = j;
							triple[2] = Domains[i][j];
							triple_count++;
						}
						else if(counter > 3) { //
							counter = 0;
							domains_index1 = -1;
							idx1 = -1;
							idx2 = -1;
							idx3 = -1;
							triple_count = 0;
							break;
						}
						
						else if((counter == 2 || counter == 3) && j == 8)
						{
							triple_found = true;
						}
					}
				}
					
			}
			
			else
			{
				for(int j = 0; j < 9; j++)
				{
					if (triple_count == 2 && Domains[i][j] != 0)
					{
						if(Domains[i][j] != triple[0] && Domains[i][j] != triple[1])
						{
							triple_count++;
							triple[2] = Domains[i][j];
						}
						
						else
						{
							if(domains_index2 == -1)
							{
								domains_index2 = i;
							}
							
							else if(domains_index3 == -1)
							{
								domains_index3 = i;
								updateColDomainsTriple(col, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
							}
						}
					}
					
					else if(triple_count == 3)
					{
						if(Domains[i][j] != triple[0] && Domains[i][j] != triple[1] && Domains[i][j] != triple[2])
						{
							break;
						}
						
						else
						{
							if(domains_index2 == -1)
							{
								domains_index2 = i;
							}
							
							else if(domains_index3 == -1)
							{
								domains_index3 = i;
								updateColDomainsTriple(col, domains_index1, domains_index2, domains_index3, idx1, idx2, idx3);
							}
						}
					}
				}	
			}
			if(i > 71 && i != 80) {
				col++;
				i = -9 + col;
			}
		}
	}
	
	static void makeEntries()
	{
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				findSingleDomain(i,j);
			}
		}
		findNakedDoubleRows();
		findNakedDoubleCols();
		findNakedDoubleBoxes();
		findNakedTriplesRows();
	}
	
	static boolean isValid(int r, int c, int val)
	{
		
		for(int i = 0; i < 9; i++) {
			if(Domains[9*r + c][i] == val)// if value is within the domain for (row,col)
				{break;}
			
			else
				{return false;}
		}
		
		for(int i = 0; i < 9; i++)
		{
			if(AllDiffs[r][i] == val || AllDiffs[9 + i][r] == val)
			{return false;}
			
			if(r >= 0 && r <= 2){
				if(c >= 0 && c <= 2){
					//box 0
					if(AllDiffs[18][i] == val)
						return false;
					}
				if(c >= 3 && c <= 5){
					//box 1
					if(AllDiffs[19][i] == val)
						return false;
					}
				if(c >= 6 && c <= 8){
					//box 2
					if(AllDiffs[20][i] == val)
						return false;
					}
			}
			else if(r >= 3 && r <= 5){
				if(c >= 0 && c <= 2){
					//box 3
					if(AllDiffs[21][i] == val)
						return false;
					}
				if(c >= 3 && c <= 5){
					//box 4
					if(AllDiffs[22][i] == val)
						return false;
					}
				if(c >= 6 && c <= 8){
					//box 5
					if(AllDiffs[23][i] == val)
						return false;
				}
			}
			else if(r >= 6 && r <= 8){
				if(c >= 0 && c <= 2){
					//box 6
					if(AllDiffs[24][i] == val)
						return false;
				}
				if(c >= 3 && c <= 5){
					//box 7
					if(AllDiffs[25][i] == val)
						return false;
				}
				if(c >= 6 && c <= 8){
					//box 8
					if(AllDiffs[26][i] == val)
						return false;
				}
			}
		}
		return true;
	}
	
	static boolean isWin()
	{
		int sum = 0;
		
		for(int i = 0; i < 27; i++){
			for(int j = 0; j < 9; j++){
				sum += AllDiffs[i][j];
			}
			
			if(sum < 45)
				{return false;}
			sum = 0;
		}
		System.out.println("\nCongrats Broseph, you win this round of Sudoku!");
		return true;
	}
	
	
	
	public static void main(String[] args)
	{
		int el = 0;
		Scanner inFile = null;
		String inFileName = "C:\\Users\\Koa\\Desktop\\ExtremeDifficultyTestSudokus\\hard-1.txt";
		File file = new File(inFileName);
		try
		{
			inFile = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File "+file+" not found!");
		}
		
		//initializes all row/col/boxes domains to 1,2,3,4,5,6,7,8,9 (all possible values)
	    for(int i =0; i < 81;i++)
	    {
	    	for(int j = 0; j < 9;j++)
	    	{
	    		Domains[i][j] = j + 1;
	    	}
	    }
	    
	    //initializes board from input text file
		for(int row = 0; row<9; row++)
		{
			for(int col = 0; col<9 && inFile.hasNext(); col++)
			{
				el = inFile.nextInt();
				System.out.println(el);
				Board[row][col] = el;
				if(el > 0)
				{
					setDomains(row,col,el);
				}
				setConstraints(row,col,el);
			}
		}
		
		//prints new board, each row/col/box, and the domain of every value for the board
		printBoard();
		printAllDiffs();
		printDomains();
		
		System.out.println("\nUPDATE TEST");
		
		while(!isWin()) {
			for(int i=0;i < 9;i++)
			{
				for(int j=0;j < 9;j++)
				{
					if(Board[i][j] > 0)
						{updateDomains(i,j,Board[i][j]);}
				}
			}
			printDomains();
			printBoard();
			makeEntries();
			System.out.print("\nNew ");
			printBoard();
		}
	}
}
