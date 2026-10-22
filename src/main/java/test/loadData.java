package test;
import org.junit.*;

import java.util.*;
import static org.junit.Assert.*;

import app.ApplicationController;
import util.FileTypes;

public class loadData {

    private static final String INPUT_FILE = "src/test/resources/input/EggsScrambled.tsv";
    private static final String INPUT_XLSX = "src/test/resources/input/EggsScrambled.xlsx";
    private static final String INPUT_XLS = "src/test/resources/input/EggsScrambled.xls";
    private static final String INPUT_MISSING="src/test/resources/input/MissingFile.tsv";
    private ApplicationController appController;

    @Before
    public void setUp() {
        appController = new ApplicationController();
    }



    @Test
    public void T1_V0_01() {
        
        List<String> loadedStr = appController.load(INPUT_FILE, FileTypes.TSV);
        assertNotNull("Data not loaded(NULL)", loadedStr);
        

    
        List<String> expectedData = Arrays.asList(
            "TaskId: \t100\tText: \tPrepare Fry\tMama: \t0\tStart: \t1\tEnd: \t12\tCost: \t60.0\tEffort: \t91.0",
            "TaskId: \t101\tText: \tTurn on burner (low)\tMama: \t100\tStart: \t1\tEnd: \t1\tCost: \t10.0\tEffort: \t15.0",
            "TaskId: \t102\tText: \tBreak eggs and pour into fry\tMama: \t100\tStart: \t2\tEnd: \t4\tCost: \t10.0\tEffort: \t14.0",
            "TaskId: \t103\tText: \tSteer mixture to avoid sticking\tMama: \t100\tStart: \t5\tEnd: \t10\tCost: \t10.0\tEffort: \t14.0",
            "TaskId: \t105\tText: \t\"Salt, pepper\"\tMama: \t100\tStart: \t5\tEnd: \t5\tCost: \t10.0\tEffort: \t16.0",
            "TaskId: \t104\tText: \tThrow yellow cheese into fry\tMama: \t100\tStart: \t6\tEnd: \t12\tCost: \t10.0\tEffort: \t17.0",
            "TaskId: \t106\tText: \tTurn burner off\tMama: \t100\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t15.0",
            "TaskId: \t200\tText: \tPrepare the bread\tMama: \t0\tStart: \t10\tEnd: \t12\tCost: \t20.0\tEffort: \t56.0",
            "TaskId: \t201\tText: \tHeat bread in toaster\tMama: \t200\tStart: \t10\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
            "TaskId: \t202\tText: \t\"Little bit of salt, galric spice to bread\"\tMama: \t200\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
            "TaskId: \t300\tText: \tServe eggs\tMama: \t0\tStart: \t13\tEnd: \t20\tCost: \t30.0\tEffort: \t149.0",
            "TaskId: \t301\tText: \tPut bread in plate\tMama: \t300\tStart: \t13\tEnd: \t13\tCost: \t10.0\tEffort: \t50.0",
            "TaskId: \t302\tText: \tPut eggs on bread\tMama: \t300\tStart: \t14\tEnd: \t14\tCost: \t10.0\tEffort: \t50.0",
            "TaskId: \t303\tText: \tWash fry\tMama: \t300\tStart: \t15\tEnd: \t20\tCost: \t10.0\tEffort: \t49.0"
        );

        
        assertEquals("data DONT match ", expectedData, loadedStr);
    }

    @Test
    public void T1_V0_02() {
        
        List<String> loadedStr = appController.load(INPUT_XLSX, FileTypes.XLSX);
        assertNotNull("Data not loaded (NULL)", loadedStr);

        
        List<String> expectedData = Arrays.asList(
        	    "TaskId: \t100\tText: \tPrepare Fry\tMama: \t0\tStart: \t1\tEnd: \t12\tCost: \t60.0\tEffort: \t91.0",
        	    "TaskId: \t101\tText: \tTurn on burner (low)\tMama: \t100\tStart: \t1\tEnd: \t1\tCost: \t10.0\tEffort: \t15.0",
        	    "TaskId: \t102\tText: \tBreak eggs and pour into fry\tMama: \t100\tStart: \t2\tEnd: \t4\tCost: \t10.0\tEffort: \t14.0",
        	    "TaskId: \t103\tText: \tSteer mixture to avoid sticking\tMama: \t100\tStart: \t5\tEnd: \t10\tCost: \t10.0\tEffort: \t14.0",
        	    "TaskId: \t105\tText: \tSalt, pepper\tMama: \t100\tStart: \t5\tEnd: \t5\tCost: \t10.0\tEffort: \t16.0",
        	    "TaskId: \t104\tText: \tThrow yellow cheese into fry\tMama: \t100\tStart: \t6\tEnd: \t12\tCost: \t10.0\tEffort: \t17.0",
        	    "TaskId: \t106\tText: \tTurn burner off\tMama: \t100\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t15.0",
        	    "TaskId: \t200\tText: \tPrepare the bread\tMama: \t0\tStart: \t10\tEnd: \t12\tCost: \t20.0\tEffort: \t56.0",
        	    "TaskId: \t201\tText: \tHeat bread in toaster\tMama: \t200\tStart: \t10\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
        	    "TaskId: \t202\tText: \tLittle bit of salt, galric spice to bread\tMama: \t200\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
        	    "TaskId: \t300\tText: \tServe eggs\tMama: \t0\tStart: \t13\tEnd: \t20\tCost: \t30.0\tEffort: \t149.0",
        	    "TaskId: \t301\tText: \tPut bread in plate\tMama: \t300\tStart: \t13\tEnd: \t13\tCost: \t10.0\tEffort: \t50.0",
        	    "TaskId: \t302\tText: \tPut eggs on bread\tMama: \t300\tStart: \t14\tEnd: \t14\tCost: \t10.0\tEffort: \t50.0",
        	    "TaskId: \t303\tText: \tWash fry\tMama: \t300\tStart: \t15\tEnd: \t20\tCost: \t10.0\tEffort: \t49.0"
        	);





        
        assertEquals("Loaded and expected data size mismatch!", expectedData, loadedStr);
    }
    @Test
    public void T1_V0_03() {
        
        List<String> loadedStr = appController.load(INPUT_XLS, FileTypes.XLS);
        assertNotNull("Data not loaded (NULL)", loadedStr);

        
        List<String> expectedData = Arrays.asList(
        	    "TaskId: \t100\tText: \tPrepare Fry\tMama: \t0\tStart: \t1\tEnd: \t12\tCost: \t60.0\tEffort: \t91.0",
        	    "TaskId: \t101\tText: \tTurn on burner (low)\tMama: \t100\tStart: \t1\tEnd: \t1\tCost: \t10.0\tEffort: \t15.0",
        	    "TaskId: \t102\tText: \tBreak eggs and pour into fry\tMama: \t100\tStart: \t2\tEnd: \t4\tCost: \t10.0\tEffort: \t14.0",
        	    "TaskId: \t103\tText: \tSteer mixture to avoid sticking\tMama: \t100\tStart: \t5\tEnd: \t10\tCost: \t10.0\tEffort: \t14.0",
        	    "TaskId: \t105\tText: \tSalt, pepper\tMama: \t100\tStart: \t5\tEnd: \t5\tCost: \t10.0\tEffort: \t16.0",
        	    "TaskId: \t104\tText: \tThrow yellow cheese into fry\tMama: \t100\tStart: \t6\tEnd: \t12\tCost: \t10.0\tEffort: \t17.0",
        	    "TaskId: \t106\tText: \tTurn burner off\tMama: \t100\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t15.0",
        	    "TaskId: \t200\tText: \tPrepare the bread\tMama: \t0\tStart: \t10\tEnd: \t12\tCost: \t20.0\tEffort: \t56.0",
        	    "TaskId: \t201\tText: \tHeat bread in toaster\tMama: \t200\tStart: \t10\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
        	    "TaskId: \t202\tText: \tLittle bit of salt, galric spice to bread\tMama: \t200\tStart: \t12\tEnd: \t12\tCost: \t10.0\tEffort: \t28.0",
        	    "TaskId: \t300\tText: \tServe eggs\tMama: \t0\tStart: \t13\tEnd: \t20\tCost: \t30.0\tEffort: \t149.0",
        	    "TaskId: \t301\tText: \tPut bread in plate\tMama: \t300\tStart: \t13\tEnd: \t13\tCost: \t10.0\tEffort: \t50.0",
        	    "TaskId: \t302\tText: \tPut eggs on bread\tMama: \t300\tStart: \t14\tEnd: \t14\tCost: \t10.0\tEffort: \t50.0",
        	    "TaskId: \t303\tText: \tWash fry\tMama: \t300\tStart: \t15\tEnd: \t20\tCost: \t10.0\tEffort: \t49.0"
        	);





        
        assertEquals("Loaded and expected data size mismatch!", expectedData, loadedStr);
    }
    @Test
    public void T1_V0_04() {
  
        
        assertThrows(IllegalArgumentException.class, () -> {
            appController.load(INPUT_MISSING, FileTypes.TSV);
        });

    }

    
    

  }
