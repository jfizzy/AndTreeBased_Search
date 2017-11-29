/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Input;
import java.io.*;

import Schedule.*;
import java.util.ArrayList;

/**
 *
 * @author emlou
 */

public class ScheduleVisualizer {
    
    public static final int COLUMN_WIDTH = 600;
    public static final int ROW_HEIGHT = 400;
    public static final int LINE_WIDTH = 1;
    public static final int MARGIN_WIDTH = 100;
    public static final int NUM_ROWS = 16;
    public static final int NUM_COLUMNS = 6;
    
    private Schedule finalSchedule;
    
   public ScheduleVisualizer(Schedule fs)
   {
       this.finalSchedule = fs;
   }
    
    Schedule schedule;
    
    public void run()
    {
        try{
            StringBuilder htmlSB = new StringBuilder();
            //Append HTML header and title:
            htmlSB.append("<html>\n<head><title>CPSC 433 - Assignment Schedule Optimizer</title></head>\n");


            //Append Body and begin <svg>:
            htmlSB.append("<body>\n");
            htmlSB.append("<svg width = \"");
            htmlSB.append(Integer.toString((NUM_COLUMNS)*COLUMN_WIDTH+500));
            htmlSB.append("\" height = \"");
            htmlSB.append(Integer.toString((NUM_ROWS)*ROW_HEIGHT+500));
            htmlSB.append("\">\n");
            
           //Schedule column lines:
           for (int i=0; i<NUM_COLUMNS; i++)
           {
                htmlSB.append("<rect x=\"");
                htmlSB.append(Integer.toString((MARGIN_WIDTH + i*COLUMN_WIDTH)));
                htmlSB.append("\" y=\"");
                htmlSB.append(Integer.toString(MARGIN_WIDTH));
                htmlSB.append("\" width=\"");
                htmlSB.append(Integer.toString(LINE_WIDTH*3));
                htmlSB.append("\" height=\"");
                htmlSB.append(Integer.toString(ROW_HEIGHT*NUM_ROWS));
                htmlSB.append("\" style = \"stroke:black\" />\n");
           }
           
           for (int i=0; i<(NUM_COLUMNS-1); i++)
           {
                htmlSB.append("<rect x=\"");
                htmlSB.append(Integer.toString((MARGIN_WIDTH + (COLUMN_WIDTH/2) + i*COLUMN_WIDTH)));
                htmlSB.append("\" y=\"");
                htmlSB.append(Integer.toString(MARGIN_WIDTH));
                htmlSB.append("\" width=\"");
                htmlSB.append(Integer.toString(LINE_WIDTH));
                htmlSB.append("\" height=\"");
                htmlSB.append(Integer.toString(ROW_HEIGHT*NUM_ROWS));
                htmlSB.append("\" style = \"stroke:black;opacity:0.5\" />\n");
           }

            //Schedule Row Lines:
           for (int i=0; i<=(NUM_ROWS); i++)
           {
                htmlSB.append("<rect x=\"");
                htmlSB.append(Integer.toString(MARGIN_WIDTH));
                htmlSB.append("\" y = \"");
                htmlSB.append(Integer.toString(MARGIN_WIDTH + i*ROW_HEIGHT));
                htmlSB.append("\" width=\"");
                htmlSB.append(Integer.toString(COLUMN_WIDTH*(NUM_COLUMNS-1)));
                htmlSB.append("\" height=\"");
                htmlSB.append(Integer.toString(LINE_WIDTH*2));
                htmlSB.append("\" style = \"stroke:black;opacity:0.5\" />\n");
           }  
           
           //Print Times
           for(int i = 0; i < (NUM_ROWS+1); i++)
           {
               String time = Integer.toString(i+8);
               time = time.concat(":00");
               htmlTextBox(htmlSB, 0 , MARGIN_WIDTH + i*ROW_HEIGHT, time, 30);
               
           }

           
            // Weekday Headings:
            String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            
            for(int i = 0; i < weekdays.length; i++)
            {
              htmlTextBox(htmlSB, (MARGIN_WIDTH/2 + (COLUMN_WIDTH/2)+ i*COLUMN_WIDTH), (MARGIN_WIDTH/2), weekdays[i], 30);   
                
            }
            
            // Slot Labeling
            String[] slotTypes = {"Lecture", "Lab/Tutorial"};
            
            int x;
            int y = MARGIN_WIDTH - 10;
            for(int i = 0; i < (NUM_COLUMNS-1); i++)
            {
                x = COLUMN_WIDTH*i + MARGIN_WIDTH + COLUMN_WIDTH/6;
                
                for(int j = 0; j < slotTypes.length; j++)
                {
                    htmlTextBox(htmlSB, x + j*(COLUMN_WIDTH/2), y, slotTypes[j], 20);
                }
                
            }
            
            // Print Meetings
            
            printAssignments(htmlSB, this.finalSchedule);
            System.out.println("test_outside");
            
            //printMeeting(htmlSB, MARGIN_WIDTH, MARGIN_WIDTH, "CPSC 313 LEC 01 TUT 04");
            
            
            
                      
            htmlSB.append("</svg>\n");
            htmlSB.append("</body>\n");


            //Close HTML
            htmlSB.append("</html>");

            //Write HTML string content to a file

            WriteToFile(htmlSB.toString(), "Schedule.html");
            
            System.out.println("Schedule visualization outputted to Schedule.html");
        }
        catch (IOException e) {
        }
       
        
    }
    
    // Prints text at coordinates (x,y). MUST BE WITHIN AN <svg> text </svg> FORMAT!
    private void htmlTextBox(StringBuilder htmlSB, int x, int y, String text, int size)
    {
        
          htmlSB.append("<text font-size = \"");
          htmlSB.append(Integer.toString(size));
          htmlSB.append("\" x = \"");
          htmlSB.append(Integer.toString(x));
          htmlSB.append("\" y = \"");
          htmlSB.append(Integer.toString(y));
          htmlSB.append("\" >");
          htmlSB.append(text);
          htmlSB.append("</text>\n");
 
    }
   
    // Prints boxes for to outline slots. MUST BE WITHIN AN <svg> text </svg> FORMAT!
    private void htmlRectangle(StringBuilder htmlSB, int x, int y, int width, int height, String fillColour, double fillOpacity, String lineColour, double lineOpacity)
    {
        htmlSB.append("<rect x = \"");
        htmlSB.append(Integer.toString(x));
        htmlSB.append("\" y = \"");
        htmlSB.append(Integer.toString(y));
        htmlSB.append("\" width = \"");
        htmlSB.append(Integer.toString(width)); 
        htmlSB.append("\" height = \"");
        htmlSB.append(Integer.toString(height));
        htmlSB.append("\" style = \"fill:");
        htmlSB.append(fillColour);
        htmlSB.append(";fill-opacity:");
        htmlSB.append(Double.toString(fillOpacity));
        htmlSB.append(";stroke-width:3;stroke:");
        htmlSB.append(lineColour);
        htmlSB.append(";stroke-opacity:");
        htmlSB.append(Double.toString(lineOpacity));
        htmlSB.append("\"/> \n");
    }
    
   

    private void printAssignments(StringBuilder htmlSB, Schedule schedule)
    {
        String colour;
        int x;
        int y;
        int width;
        int height;
        ArrayList<Assignment> assignments = schedule.getAssignments();
        ArrayList<Slot> slots = new ArrayList<>();
        Slot s;
        
        
        //Make an arraylist of all the slots used in this schedule.
        for(Assignment a : assignments)
        {
            System.out.println(a.toString());
            if(a.getS() instanceof LectureSlot)
            {
                s = (LectureSlot)a.getS();
            }
            else
            {
                s = (NonLectureSlot)a.getS();
            }
            if(!existsInArray(slots, s) | slots.isEmpty())
            {
                slots.add(s);
            }
     
        }
        
        System.out.println("test1");
        
        for(Slot a : slots)
        {
            System.out.println("test");
            if(a != null)
            System.out.println(a.toString());
            
            
            System.out.println("Length of slots array is " + slots.size());
        }
        
        
    }

    
    private boolean existsInArray(ArrayList<Slot> slots, Slot slot)
    {
        boolean exists = false;
        for (Slot s : slots)
        {
            if(s != null)
            {
                if(s.equals(slot))
                    exists = true;
            }
        }
        
        return exists;
    }
    
    private void printMeeting(StringBuilder htmlSB, int x, int y, String text)
    {
        // x should be the leftmost coordinate of the Slot
        // y should be the ..
        
        int xMargin = COLUMN_WIDTH/20;
        int yMargin = ROW_HEIGHT/20;
        x += xMargin;
        y += yMargin;
        int width = (COLUMN_WIDTH)/2 - 2*xMargin;
        int height = (ROW_HEIGHT)/10;
        
        htmlRectangle(htmlSB, x, y, width, height, "white", 0, "black", 0.3);
        htmlTextBox(htmlSB, x+10, y+25, text, 15);

    }
    
    
    public static String updateFileName(String fileName)
    {
        String newFileName;
        int periodIndex = 1;
        
        // Determine where the .html starts:
        for(int i = 0; i < fileName.length(); i++)
        {
            if(fileName.charAt(i) == '.')
            {
                periodIndex = i;
                break;
            }   
        }
            
        
        for(int i = 1; i < 100; i++)
        {
            File f = new File(fileName);
            if (f.exists())
            {
                String nameNoExt = fileName.substring(0, periodIndex);
                newFileName = nameNoExt.concat(Integer.toString(i));
                newFileName = newFileName.concat(".html");
                fileName = newFileName;
            }
            else
                break;
            
        }
        return fileName;
    }
    
    public static void WriteToFile(String fileContent, String fileName) throws IOException 
    {
        
        String projectPath = System.getProperty("user.dir");
        String tempFile = projectPath + File.separator+fileName;
        File file = new File(tempFile);
        // if file does exists, then delete and create a new file
        if (file.exists()) 
        {
            try 
            {
                int nameSize = fileName.length();

                String fileName2 = fileName.substring(0,(fileName.length()-5));
                File newFileName = new File(projectPath + File.separator+updateFileName(fileName));
                file.renameTo(newFileName);
                file.createNewFile();
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        //write to file with OutputStreamWriter
        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
        Writer writer=new OutputStreamWriter(outputStream);
        writer.write(fileContent);
        writer.close();

    }
    

}
