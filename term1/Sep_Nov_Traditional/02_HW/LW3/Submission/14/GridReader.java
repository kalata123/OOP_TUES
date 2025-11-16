import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class GridReader {

    public static boolean isLineWall(String line)
    {
        char repeatingChar = line.charAt(0);
        for(int i = 1; i < line.length(); i++)
        {
            if(line.charAt(i) != repeatingChar)
                return false;
        }
        return true;
    }


    public static Vector<String[]> readGridsFromFile(String filename)
    {
        Vector<String[]> grids = new Vector<String[]>();
        Vector<String> lines = new Vector<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        boolean isReadingGrid = false;
        Vector<String> currentGrid = new Vector<>();
        for(String line : lines)
        {
            if (line.isEmpty())
                continue;
            if(line.charAt(0) == '#')
            {
                if(!isReadingGrid)
                {
                    isReadingGrid = true;
                    currentGrid.clear();
                    currentGrid.add(line);
                }
                else{
                    if(isLineWall(line))
                    {
                        isReadingGrid = false;
                        currentGrid.add(line);
                        grids.add(currentGrid.toArray(new String[0]));
                    }
                    else
                    {
                        currentGrid.add(line);
                    }
                }

            }

        }


        return grids;
    }
}
