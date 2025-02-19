/**
 * CS 221 Exceptions: FormatChecker (HW)
 *
 * Demonstrates my knowledge about exceptions
 *
 * @author Vlad Maliutin
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FormatChecker
{
    public static void main(String[] args) throws FileNotFoundException, WrongDimensionException 
    {
        // String check[] = ("invalid1.dat invalid2.dat invalid3.dat invalid4.dat invalid5.dat invalid6.dat invalid7.dat valid1.dat valid2.dat valid3.dat wrong").trim().split("\\s+");
        
        //checking each file from args
        for (String f : args)
        {
            try
            {
                System.out.println(f);
                checkFormat(f);
            }
            catch (Exception e)     //if I didn't include some exceptions
            {
                System.out.println(e.toString());
                System.out.println("INVALID");
                System.out.println();
                return;
            }
        }
    }



    public static void checkFormat(String fileName) throws FileNotFoundException, WrongDimensionException 
    {
        Scanner fileScanner;

        try 
        {
            File file= new File(fileName);
            fileScanner = new Scanner(file);

            int rows = 0;
            int columns = 0;

            //reading variables for dimensions from the first row
            String dimensions[] = fileScanner.nextLine().trim().split("\\s+");

            try
            {
                if (dimensions.length > 2)      //if there are too many variables for dimensions (not 2)
                {
                    throw new WrongDimensionException("Too many variables for dimensions");
                }

                rows = Integer.parseInt(dimensions[0]);
                columns = Integer.parseInt(dimensions[1]);

                int fileRows = 0;
                String line = "";

                //going over each line of the file (except for the first) + lines should exist and should not be blank
                while ((fileScanner.hasNextLine()) && (!(line = fileScanner.nextLine()).isBlank()))
                {
                    //reading each object from the current line
                    String[] current = line.split(" ");
                    
                    if (current.length > columns)       //if current line has more columns than mentioned in first line
                    {
                        throw new WrongDimensionException("Too many objects in at least one of the rows");
                    }
                    else if (current.length < columns)  //if current line has less columns than mentioned in first line
                    {
                        throw new WrongDimensionException("Not enough objects in at least one of the rows");
                    }

                    //checking elements in a row (are they Double?)
                    for (int i = 0; i < current.length; i++)
                    {
                        try
                        {
                            Double.parseDouble(current[i]);
                        }
                        catch (NullPointerException e)  //if slod is emty
                        {
                            System.out.println(e.toString());
                            System.out.println("INVALID");
                            System.out.println();
                            return;
                        }
                        catch (NumberFormatException e) //if this object cannot be turned into double (if it is a letter or symbol)
                        {
                            System.out.println(e.toString());
                            System.out.println("INVALID");
                            System.out.println();
                            return;
                        }
                    }

                    fileRows++;     //counding rows
                }

                if (fileRows > rows)         //if there are more rows than mentioned in the first line
                {
                    throw new WrongDimensionException("Too many objects in at least one of the columns");
                }
                else if (fileRows < rows)    //if there are less rows than mentioned in the first line
                {
                    throw new WrongDimensionException("Not enough objects in at least one of the columns");
                }

                //if code didn't throw exceptions yet, then file is valid
                System.out.println("VALID");
                System.out.println();
            }
            catch (NumberFormatException e)     //if this object cannot be turned into double (if it is a letter or symbol) (for first line)
            {
                fileScanner.close();
                System.out.println(e.toString());
                System.out.println("INVALID");
                System.out.println();
                return;
            }
            catch (WrongDimensionException e)   //just in case
            {
                fileScanner.close();
                System.out.println(e.toString());
                System.out.println("INVALID");
                System.out.println();
                return;
            }

            fileScanner.close();
        }
        catch (FileNotFoundException e)     //if file is not found or filename is entered incorrectly
        {
            System.out.println(e.toString());
            System.out.println("INVALID");
            System.out.println();
            return;
        }
    }

}

class WrongDimensionException extends Exception     //seperate exception in case there are mistakes with dimensions in files
{
    public WrongDimensionException(String message) 
    {
        super(message);
    }
}