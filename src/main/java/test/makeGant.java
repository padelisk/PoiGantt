package test;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Before;
import org.junit.Test;

import app.ApplicationController;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class makeGant {

    private static final String INPUT_FILE = "src/test/resources/input/EggsScrambled.tsv";
    private static final String OUTPUT_FILE = "src/test/resources/output/EggsScrambled_Output_TSVToXlsxTest.xlsx";
    private ApplicationController appController;

    @Before
    public void setUp() {
        appController = new ApplicationController();
    }
    @Test
    public void T4_V0() {
        
        appController.load(INPUT_FILE, FileTypes.TSV);
        List<TaskAbstract> tasks=appController.getAllTasks();
        assertNotNull("Data not loaded(NULL)", tasks);
      
       
        ProjectInfo prjInfo = appController.prepareTargetWorkbook(FileTypes.XLSX, OUTPUT_FILE);
        assertNotNull("Data not loaded(NULL)", prjInfo);
        assertEquals("EggsScrambled_Output_TSVToXlsxTest", prjInfo.getProjectName());
        assertEquals("EggsScrambled.tsv", prjInfo.getSourceFileName());
        assertEquals("EggsScrambled_Output_TSVToXlsxTest.xlsx", prjInfo.getTargetFileName());
        assertEquals(14, prjInfo.getTotalNumTasks());
        assertEquals(3, prjInfo.getTotalTopTasks());




        
        String font1 = appController.addFontedStyle("FontStyle1", IndexedColors.RED.getIndex(), (short)10, "Algerian", true, false, false, IndexedColors.WHITE.getIndex(), "Solid", "Left", true);


        assertEquals("FontStyle1", font1);


    
        boolean all = appController.createNewSheet("ALL_Styled", appController.getAllTasks(), 
				"DefaultHeaderStyle", "TopTask_bar_style", font1, "NonTopTask_bar_style", "NonTopTask_data_style", "Normal");
		boolean top = appController.createNewSheet("Top_Level", appController.getTopLevelTasksOnly(), 
							"DefaultHeaderStyle", "TopTask_bar_style", font1, font1, "NonTopTask_data_style", "Normal");
		boolean range = appController.createNewSheet("range", appController.getTasksInRange(200,201), 
				"DefaultHeaderStyle", "TopTask_bar_style", font1, "NonTopTask_bar_style", "NonTopTask_data_style", "Normal");
  

        assertTrue("Error creating all tasks sheet", all);
        assertTrue("Error creating top tasks sheet", top);
        assertTrue("Error creating range tasks sheet", range);

    }




}
