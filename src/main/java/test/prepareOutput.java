package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import app.ApplicationController;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class prepareOutput {
    private static final String INPUT_FILE = "src/test/resources/input/EggsScrambled.tsv";
    private static final String OUTPUT_FILE = "src/test/resources/output/EggsScrambled_Output_TSVToXlsxTest.xlsx";
    private static final String INVALID_OUTPUT_FILE = "src/test/resources/output/InvalidPath/EggsScrambled_Output.xlsx";
    private ApplicationController appController;

    @Before
    public void setUp() {
        appController = new ApplicationController();
    }

    @Test
    public void T3_V0_01() {
        appController.load(INPUT_FILE, FileTypes.TSV);
        List<TaskAbstract> tasks = appController.getAllTasks();
        assertNotNull("Data not loaded(NULL)", tasks);

        ProjectInfo prjInfo = appController.prepareTargetWorkbook(FileTypes.XLSX, OUTPUT_FILE);
        assertNotNull("Data not loaded(NULL)", prjInfo);
        assertEquals("EggsScrambled_Output_TSVToXlsxTest", prjInfo.getProjectName());
        assertEquals("EggsScrambled.tsv", prjInfo.getSourceFileName());
        assertEquals("EggsScrambled_Output_TSVToXlsxTest.xlsx", prjInfo.getTargetFileName());
        assertEquals(14, prjInfo.getTotalNumTasks());
        assertEquals(3, prjInfo.getTotalTopTasks());
    }



    @Test
    public void T3_V0_02() {
        appController.load(INPUT_FILE, FileTypes.TSV);
        assertThrows(IllegalArgumentException.class, () -> {
            appController.prepareTargetWorkbook(FileTypes.XLSX, INVALID_OUTPUT_FILE);
        });

    }


}

