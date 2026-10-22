package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import app.ApplicationController;
import dom.gantt.TaskAbstract;
import util.FileTypes;

public class filterData {
	 private static final String INPUT_FILE = "src/test/resources/input/EggsScrambled.tsv";
	 private ApplicationController appController;
	 @Before
	 public void setUp() {
	        appController = new ApplicationController();
	  }
	  @Test
	  public void T2_V0_01() {
	       
	        appController.load(INPUT_FILE, FileTypes.TSV);
	        
	        
	        List<TaskAbstract> allTasks = appController.getAllTasks();
	        assertNotNull("Data not loaded(NULL)", allTasks);
	       
             //just take some tasks as sample to see if they are correct
	        
	        TaskAbstract task1 = allTasks.get(0);
	        assertEquals("Prepare Fry", task1.getTaskText());
	        assertEquals(100, task1.getTaskId());
	        assertEquals(1, task1.getTaskStart());
	        assertEquals(12, task1.getTaskEnd());

	        TaskAbstract task2 = allTasks.get(1);
	        assertEquals("Turn on burner (low)", task2.getTaskText());
	        assertEquals(101, task2.getTaskId());
	        assertEquals(1, task2.getTaskStart());
	        assertEquals(1, task2.getTaskEnd());

	        TaskAbstract task3 = allTasks.get(7);
	        assertEquals("Prepare the bread", task3.getTaskText());
	        assertEquals(200, task3.getTaskId());
	        assertEquals(10, task3.getTaskStart());
	        assertEquals(12, task3.getTaskEnd());

	        TaskAbstract task4 = allTasks.get(10);
	        assertEquals("Serve eggs", task4.getTaskText());
	        assertEquals(300, task4.getTaskId());
	        assertEquals(13, task4.getTaskStart());
	        assertEquals(20, task4.getTaskEnd());
	        
	   }

    @Test
    public void T2_V0_02() {
        
        appController.load(INPUT_FILE, FileTypes.TSV);
        
        
        List<TaskAbstract> topLevelTasks = appController.getTopLevelTasksOnly();
        assertNotNull("Data not loaded(NULL)", topLevelTasks);
       

        
        TaskAbstract firstTask = topLevelTasks.get(0);
        assertEquals("Prepare Fry", firstTask.getTaskText());
        assertEquals(100, firstTask.getTaskId());
        assertEquals(1, firstTask.getTaskStart());
        assertEquals(12, firstTask.getTaskEnd());
        
        
        TaskAbstract secondTask = topLevelTasks.get(1);
        assertEquals("Prepare the bread", secondTask.getTaskText());
        assertEquals(200, secondTask.getTaskId());
        assertEquals(10, secondTask.getTaskStart());
        assertEquals(12, secondTask.getTaskEnd());

        TaskAbstract thirdTask = topLevelTasks.get(2);
        assertEquals("Serve eggs", thirdTask.getTaskText());
        assertEquals(300, thirdTask.getTaskId());
        assertEquals(13, thirdTask.getTaskStart());
        assertEquals(20, thirdTask.getTaskEnd());
    }
    @Test
    public void T2_V0_03() {
       
        appController.load(INPUT_FILE, FileTypes.TSV);

       
        List<TaskAbstract> tasksInRange = appController.getTasksInRange(200, 201);
        assertNotNull("Data not loaded(NULL)", tasksInRange);
        

        TaskAbstract firstTask = tasksInRange.get(0);
        assertEquals("Prepare the bread", firstTask.getTaskText());
        assertEquals(200, firstTask.getTaskId());
        assertEquals(10, firstTask.getTaskStart());
        assertEquals(12, firstTask.getTaskEnd());

        TaskAbstract secondTask = tasksInRange.get(1);
        assertEquals("Heat bread in toaster", secondTask.getTaskText());
        assertEquals(201, secondTask.getTaskId());
        assertEquals(10, secondTask.getTaskStart());
        assertEquals(12, secondTask.getTaskEnd());


    }
}
