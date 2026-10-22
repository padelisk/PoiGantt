package service;

import org.apache.poi.ss.usermodel.*;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
public class MainController implements IMainController {
	
	
	List<TaskAbstract> tasks = new ArrayList<>();        
	List<TaskAbstract> sortedTasks = new ArrayList<>();   
	private Workbook workbook;                             
	Sheet newSheet ;
	String filePath;
	String source;
	FileOutputStream outputStream ;
	private Map<String, CellStyle> styleGallery=new HashMap<>(); 
	
	
	@Override
	public List<String> load(String sourcePath, FileTypes filetype) {      //find the type of loading file and calls the appropriate function  at the end it returns the task in strings
	    File file = new File(sourcePath);
		source = file.getName();                                                                   
	    if (!file.exists() ) {
	        throw new IllegalArgumentException("File Not Found: " + sourcePath);
	    }                                                                     
	    else if (filetype.name().equals("TSV")) {
	        tasks = loadTSV(sourcePath);
	    } else if (filetype.name().equals("XLS")) {
	        tasks = loadXLS(sourcePath);
	    } else if (filetype.name().equals("XLSX")) {
	        tasks = loadXLSX(sourcePath);
	    } else {
	        throw new IllegalArgumentException("Unsupported file type: " + filetype);
	    }

	    
	    return sortTasksAndConvertToString(tasks);
	}
	
	

	public List<TaskAbstract> loadTSV(String sourcePath) {
	    String delimiter = "\t"; 
	    String[] data;
	    int id ;
	    String taskText;
	    int containerTaskId;
	    int start;
	    int end;
        double cost;
        double effort;
        
	    try (BufferedReader buffer = new BufferedReader(new FileReader(sourcePath))) {
	        String line;

	       
	        while ((line = buffer.readLine()) != null) {
	        	
	            data = line.split(delimiter);
	            id = Integer.parseInt(data[0]);
	            taskText = data[1];
	            containerTaskId = Integer.parseInt(data[2]);
	            
	            
	            														// Composite task
	            if (containerTaskId == 0) { 
	                tasks.add(compositeTask(id, taskText));
	            } 
	            													    // Simple task
	            else { 
	                start = Integer.parseInt(data[3]);
	                end = Integer.parseInt(data[4]);
	                cost = Double.parseDouble(data[5]);
	                effort = Double.parseDouble(data[6]);
	                tasks.add(newSimpleTask(id, taskText, containerTaskId, start, end, cost, effort));
	            }
	        }

	        														// Link subtasks to top level composite tasks
	        for (TaskAbstract task : tasks) {
	            if (task.getContainerTaskId() != 0) { 
	                for (TaskAbstract topTasks : tasks) {
	                    if (topTasks.getTaskId() == task.getContainerTaskId()) {
	                        topTasks.getSubtasks().add(task);
	                        break;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return tasks;
	}

	private List<TaskAbstract> loadXLS(String sourcePath) {
	    
	    int id ;
	    String taskText;
	    int containerTaskId;
	    int start;
	    int end;
        double cost;
        double effort;
        
	    try (FileInputStream input = new FileInputStream(sourcePath);
	         HSSFWorkbook workbook = new HSSFWorkbook(input)) {

	        HSSFSheet sheet = workbook.getSheetAt(0);
	        int rows = sheet.getLastRowNum();

	    
	        for (int r = 0; r <= rows; r++) {
	        	
	            HSSFRow row = sheet.getRow(r);
	            if (row == null) continue;

	            id = (int) row.getCell(0).getNumericCellValue();
	            taskText = row.getCell(1).getStringCellValue();
	            containerTaskId = (int) row.getCell(2).getNumericCellValue();

	            if (containerTaskId == 0) {                               // Composite task
	                tasks.add(compositeTask(id, taskText));
	            } 
	            else {                                                    // Simple task
	                start = (int) row.getCell(3).getNumericCellValue();
	                end = (int) row.getCell(4).getNumericCellValue();
	                cost = row.getCell(5).getNumericCellValue();
	                effort = row.getCell(6).getNumericCellValue();
	                tasks.add(newSimpleTask(id, taskText, containerTaskId, start, end, cost, effort));
	            }
	        }

	                                                                      // Link subtasks to top level composite tasks
	        for (TaskAbstract task : tasks) {
	            if (task.getContainerTaskId() != 0) { 
	                for (TaskAbstract topTasks : tasks) {
	                    if (topTasks.getTaskId() == task.getContainerTaskId()) {
	                        topTasks.getSubtasks().add(task);
	                        break;
	                    }
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return tasks;
	}

	
	
	private List<TaskAbstract> loadXLSX(String sourcePath) {
	    int id ;
	    String taskText;
	    int containerTaskId;
	    int start;
	    int end;
        double cost;
        double effort;

	    try (FileInputStream input = new FileInputStream(sourcePath);
	         XSSFWorkbook workbook = new XSSFWorkbook(input)) {

	        XSSFSheet sheet = workbook.getSheetAt(0);
	        int rows = sheet.getLastRowNum();

	        
	        for (int r = 0; r <= rows; r++) {
	            XSSFRow row = sheet.getRow(r);
	            if (row == null) continue;

	            id = (int) row.getCell(0).getNumericCellValue();
	            taskText = row.getCell(1).getStringCellValue();
	            containerTaskId = (int) row.getCell(2).getNumericCellValue();

	            if (containerTaskId == 0) {										 // Composite task
	                tasks.add(compositeTask(id, taskText));
	            } else { 														// Simple task
	                start = (int) row.getCell(3).getNumericCellValue();
	                end = (int) row.getCell(4).getNumericCellValue();
	                cost = row.getCell(5).getNumericCellValue();
	                effort = row.getCell(6).getNumericCellValue();
	                tasks.add(newSimpleTask(id, taskText, containerTaskId, start, end, cost, effort));
	            }
	        }

	        																	// Link subtasks to top level composite tasks
	        for (TaskAbstract task : tasks) {
	            if (task.getContainerTaskId() != 0) { 
	                for (TaskAbstract topTasks : tasks) {
	                    if (topTasks.getTaskId() == task.getContainerTaskId()) {
	                        topTasks.getSubtasks().add(task);
	                        break;
	                    }
	                }
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return tasks;
	}


	private TaskAbstract compositeTask(int taskId, String taskText) {
	    return new TaskAbstract(taskId, taskText, 0) {
	        private final List<TaskAbstract> subtasks = new ArrayList<>();

	        @Override
	        public int getTaskStart() {
	            if (subtasks.isEmpty()) {
	            	return 0;
	            }
	            int minStart =1000;
	            for (TaskAbstract subtask : subtasks) {
	                if (subtask.getTaskStart() < minStart) {
	                    minStart = subtask.getTaskStart();
	                }
	            }
	            return minStart;
	        }

	        @Override
	        public int getTaskEnd() {
	            if (subtasks.isEmpty()) {
	            	return 0;
	            }
	            int maxEnd =0;
	            for (TaskAbstract subtask : subtasks) {
	                if (subtask.getTaskEnd() > maxEnd) {
	                    maxEnd = subtask.getTaskEnd();
	                }
	            }
	            return maxEnd;
	        }

	        @Override
	        public double getCost() {
	            double totalCost = 0.0;
	            for (TaskAbstract subtask : subtasks) {
	                totalCost += subtask.getCost();
	            }
	            return totalCost;
	        }

	        @Override
	        public double getEffort() {
	            double totalEffort = 0.0;
	            for (TaskAbstract subtask : subtasks) {
	                totalEffort += subtask.getEffort();
	            }
	            return totalEffort;
	        }

	        @Override
	        public boolean isSimple() {
	            return false; 
	        }

	        @Override
	        public List<TaskAbstract> getSubtasks() {
	            return subtasks; 
	        }
	    };
	}

	
	private TaskAbstract newSimpleTask(int taskId, String taskText, int containerTaskId, int start, int end, double cost, double effort) {
	    return new TaskAbstract(taskId, taskText, containerTaskId) {
	        @Override
	        public int getTaskStart() {
	            return start;
	        }

	        @Override
	        public int getTaskEnd() {
	            return end;
	        }

	        @Override
	        public double getCost() {
	            return cost;
	        }

	        @Override
	        public double getEffort() {
	            return effort;
	        }

	        @Override
	        public boolean isSimple() {
	            return true;
	        }

	        @Override
	        public List<TaskAbstract> getSubtasks() {
	            return null; 
	        }
	    };
	}



	
	private List<String> sortTasksAndConvertToString(List<TaskAbstract> tasks) {
	    
	    List<TaskAbstract> topLevelTasks = new ArrayList<>();
	    List<TaskAbstract> subtasks = new ArrayList<>();

	    for (TaskAbstract task : tasks) {
	        if (task.getContainerTaskId() == 0) {
	            topLevelTasks.add(task);
	        } else {
	            subtasks.add(task);
	        }
	    }

	    																			// Sort top-level tasks 
	    for (int i = 0; i < topLevelTasks.size() - 1; i++) {
	        for (int j = 0; j < topLevelTasks.size() - i - 1; j++) {
	            TaskAbstract task1 = topLevelTasks.get(j);
	            TaskAbstract task2 = topLevelTasks.get(j + 1);

	            if (task1.getTaskStart() > task2.getTaskStart()) {
	                
	            	topLevelTasks.set(j, task2);
	            	topLevelTasks.set(j + 1, task1);
	            }
	        }
	    }

	    																			// Sort subtasks 
	    for (int i = 0; i < subtasks.size() - 1; i++) {
	        for (int j = 0; j < subtasks.size() - i - 1; j++) {
	            TaskAbstract task1 = subtasks.get(j);
	            TaskAbstract task2 = subtasks.get(j + 1);
  
                if (task1.getTaskStart() > task2.getTaskStart()) {
	                
	            	subtasks.set(j, task2);
	            	subtasks.set(j + 1, task1);
	            }
                if(task1.getTaskStart() == task2.getTaskStart() && task1.getTaskId()>task2.getTaskId()){
	            	subtasks.set(j, task2);
	            	subtasks.set(j + 1, task1);
                }
	        }
	    }

	    																	// Merge subtasks under their  top-level tasks
	    
	    for (TaskAbstract topLevelTask : topLevelTasks) {
	        sortedTasks.add(topLevelTask);
	        for (TaskAbstract subtask : subtasks) {
	            if (subtask.getContainerTaskId() == topLevelTask.getTaskId()) {
	                sortedTasks.add(subtask);
	            }
	        }
	    }
        
	    															// Convert sorted tasks to a list of strings 
	    List<String> sortedTaskStrings = new ArrayList<>();
	    for (TaskAbstract task : sortedTasks) {
	        sortedTaskStrings.add(task.toString()); 
	    }

	    return sortedTaskStrings;
	}


	@Override
	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		filePath=targetPath;
		File file = new File(filePath);
		String fileName = file.getName();
		String projectName;
		int dot = fileName.lastIndexOf('.');
        projectName=fileName.substring(0, dot);
        
		                                                                  
	    if (!file.exists() ) {
	        throw new IllegalArgumentException("File Not Found: " + targetPath);
	    }                   
	    try {
			outputStream = new FileOutputStream(targetPath);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	    
            
        if (fileType == FileTypes.XLS) {
                    workbook = new HSSFWorkbook(); // For .xls files
                    newSheet =  workbook.createSheet("Sheet1");
                    
        } else if (fileType == FileTypes.XLSX) {
                    workbook = new XSSFWorkbook(); // For .xlsx files
                    newSheet =  workbook.createSheet("Sheet1");
        }

	    int totalNumTasks = 0;
	    int totalTopTasks = 0;
	    
	    for (TaskAbstract task : tasks) {
	            if (task.getContainerTaskId() == 0) {
	                totalTopTasks++;
	           }
	    
	  
           
           totalNumTasks++;
            
        }
	    
	    initializeDefaultStyles();
	   
	    return new ProjectInfo(
	    		projectName, 
	            source, 
	            fileName,
	            totalNumTasks,
	            totalTopTasks
	    );
	}

	@Override
	public List<TaskAbstract> getAllTasks() {

	        return sortedTasks; 
	}

	@Override
	public List<TaskAbstract> getTopLevelTasksOnly() {

        List<TaskAbstract> topLevelTasks = new ArrayList<>();
        for (TaskAbstract task : tasks) {
            if (task.getContainerTaskId() == 0) {
                topLevelTasks.add(task);
            }
        }

        return topLevelTasks;
	}

	@Override
	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded) {
	        List<TaskAbstract> tasksInRange = new ArrayList<>();
	        for (TaskAbstract task : sortedTasks) {
	            if (task.getTaskId() >= firstIncluded && task.getTaskId() <= lastIncluded) {
	                tasksInRange.add(task);
	            }
	        }
	        return tasksInRange;
	}

	@Override
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		List<String> intermediateRepresentation;
	
		Row header = newSheet.createRow(0);
        header.createCell(0).setCellValue(""); 
        header.createCell(1).setCellValue("Level");
        header.createCell(2).setCellValue("Id");
        header.createCell(3).setCellValue("Description");
        header.createCell(4).setCellValue("Cost");
        header.createCell(5).setCellValue("Effort");

      
        int maxEnd = 0;
        for (TaskAbstract task : sortedTasks) {
        	if (task.getTaskEnd()>maxEnd) {
        		maxEnd=task.getTaskEnd();
        	}
        }
        for (int i = 6; i <= 5 + maxEnd; i++) {
            header.createCell(i).setCellValue(i - 5);
        }


        
        int rows = 1;
        for (TaskAbstract task : sortedTasks) {
        	
            Row row =newSheet.createRow(rows++);
            intermediateRepresentation = generateIntermediateRepresentation(task, maxEnd);
     
            
            for (int col = 0; col < intermediateRepresentation.size(); col++) {
                row.createCell(col).setCellValue(intermediateRepresentation.get(col));
            }
        }

        try {
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return true; 

	}
	private List<String> generateIntermediateRepresentation(TaskAbstract task, int maxEnd) {
	    List<String> row = new ArrayList<>();
        //space
	    row.add("");
	    if (task.getContainerTaskId()==0) {
	    	row.add("TOP");
	    }else {
	    	row.add("");
	    }
	    row.add(String.valueOf(task.getTaskId()));
        row.add(task.getTaskText());
	    row.add(String.valueOf(task.getCost()));
	    row.add(String.valueOf(task.getEffort()));
	    for (int i = 1; i <= maxEnd; i++) {
	        if (i >= task.getTaskStart() && i <= task.getTaskEnd()) {
	            row.add("x");
	        } else {
	            row.add(""); 
	        }
	    }
	    return row;
	}

	@Override
	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints,
			String styleFontName, boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout,
			short styleFillForegroundColor, String styleFillPatternString, String HorizontalAlignmentString,
			boolean styleWrapText) {
				Font font =workbook.createFont();
				CellStyle style = workbook.createCellStyle();
				font.setColor(styleFontColor);
				font.setFontHeightInPoints(styleFontHeightInPoints);
				font.setFontName(styleFontName);
				font.setBold(styleFontBold);
				font.setItalic(styleFontItalic);
				font.setStrikeout(styleFontStrikeout);
				
				
				style.setFillForegroundColor(styleFillForegroundColor);
				if(styleFillPatternString.equals("Solid")) {
					style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				}
				if(styleFillPatternString.equals("NoFill")) {
					style.setFillPattern(FillPatternType.NO_FILL);
				}
				
				if (HorizontalAlignmentString.equals("Left")) {
					style.setAlignment(HorizontalAlignment.LEFT);
				}
				if (HorizontalAlignmentString.equals("Center")) {
					style.setAlignment(HorizontalAlignment.CENTER);
				}
				if (HorizontalAlignmentString.equals("Right")) {
					style.setAlignment(HorizontalAlignment.RIGHT);
				}
				style.setWrapText(styleWrapText);
				style.setFont(font);
				styleGallery.put(styleName, style);
				return styleName;
				

	}
	private void initializeDefaultStyles() {
	    // Normal Style
	    addFontedStyle("Normal", IndexedColors.BLACK.getIndex(), (short) 11, "Arial",
	            false, false, false, IndexedColors.WHITE.getIndex(), "Solid", "Left", false);

	    // Default Header Style
	    addFontedStyle("DefaultHeaderStyle", IndexedColors.WHITE.getIndex(), (short) 12, "Arial",
	            true, false, false, IndexedColors.BLUE_GREY.getIndex(), "Solid", "Center", true);

	    // Top Task Bar Style
	    addFontedStyle("TopTask_bar_style", IndexedColors.WHITE.getIndex(), (short) 11, "Arial",
	            true, false, false, IndexedColors.BLUE.getIndex(), "Solid", "Center", true);

	    // Top Task Data Style
	    addFontedStyle("TopTask_data_style", IndexedColors.BLUE.getIndex(), (short) 11, "Arial",
	            true, false, false, IndexedColors.WHITE.getIndex(), "Solid", "Left", false);

	    // Non-Top Task Bar Style
	    addFontedStyle("NonTopTask_bar_style", IndexedColors.WHITE.getIndex(), (short) 11, "Arial",
	            true, false, false, IndexedColors.PALE_BLUE.getIndex(), "Solid", "Center", true);

	    // Non-Top Task Data Style
	    addFontedStyle("NonTopTask_data_style", IndexedColors.BLACK.getIndex(), (short) 11, "Arial",
	            false, false, false, IndexedColors.WHITE.getIndex(), "Solid", "Left", false);


	}
	@Override
	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName,
	        String topBarStyleName, String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName,
	        String normalStyleName) {
	    
		try {
			outputStream = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newSheet = workbook.createSheet(sheetName);
		newSheet.setColumnWidth(3, 10000); 
		Row header = newSheet.createRow(0);
        header.createCell(0).setCellValue(""); 
        header.getCell(0).setCellStyle(styleGallery.get( headerStyleName));
        header.createCell(1).setCellValue("Level");
        header.getCell(1).setCellStyle(styleGallery.get( headerStyleName));
        header.createCell(2).setCellValue("Id");
        header.getCell(2).setCellStyle(styleGallery.get( headerStyleName));
        header.createCell(3).setCellValue("Description");
        header.getCell(3).setCellStyle(styleGallery.get( headerStyleName));
        header.createCell(4).setCellValue("Cost");
        header.getCell(4).setCellStyle(styleGallery.get( headerStyleName));
        header.createCell(5).setCellValue("Effort");
        header.getCell(5).setCellStyle(styleGallery.get( headerStyleName));

        int maxEnd = 0;
        for (TaskAbstract task : sortedTasks) {
        	if (task.getTaskEnd()>maxEnd) {
        		maxEnd=task.getTaskEnd();
        	}
        }
        for (int i = 6; i <= 5 + maxEnd; i++) {
            header.createCell(i).setCellValue(i - 5);
            header.getCell(i).setCellStyle(styleGallery.get(headerStyleName));
        }


        // Write tasks
        int rows = 1;
        for (TaskAbstract task : tasks) {
            Row row = newSheet.createRow(rows++);
            Cell level= row.createCell(1);
            if (task.getContainerTaskId()==0) {
            	level.setCellValue("TOP" );
            	level.setCellStyle(styleGallery.get(normalStyleName));
                row.createCell(2).setCellValue(task.getTaskId());
                row.getCell(2).setCellStyle( styleGallery.get(topDataStyleName));
                row.createCell(3).setCellValue(task.getTaskText());
                row.getCell(3).setCellStyle( styleGallery.get(topDataStyleName));
                row.createCell(4).setCellValue(task.getCost());
                row.getCell(4).setCellStyle( styleGallery.get(topDataStyleName));
                row.createCell(5).setCellValue(task.getEffort());
                row.getCell(5).setCellStyle( styleGallery.get(topDataStyleName));
                for (int i = 1; i <= maxEnd; i++) {
                    Cell timelineCell = row.createCell(5 + i);
                    if (i >= task.getTaskStart() && i <= task.getTaskEnd()) {
                       
                        timelineCell.setCellStyle(styleGallery.get(topBarStyleName) );
                    } else {
                        timelineCell.setCellStyle(styleGallery.get(normalStyleName));
                    }
                }
            }else {
            	level.setCellValue("" );
            	level.setCellStyle(styleGallery.get(normalStyleName));
            	level.setCellStyle(styleGallery.get(normalStyleName));
                row.createCell(2).setCellValue(task.getTaskId());
                row.getCell(2).setCellStyle( styleGallery.get(nonTopDataStyleName));
                row.createCell(3).setCellValue(task.getTaskText());
                row.getCell(3).setCellStyle( styleGallery.get(nonTopDataStyleName));
                row.createCell(4).setCellValue(task.getCost());
                row.getCell(4).setCellStyle( styleGallery.get(nonTopDataStyleName));
                row.createCell(5).setCellValue(task.getEffort());
                row.getCell(5).setCellStyle( styleGallery.get(nonTopDataStyleName));
                for (int i = 1; i <= maxEnd; i++) {
                    Cell timelineCell = row.createCell(5 + i);
                    if (i >= task.getTaskStart() && i <= task.getTaskEnd()) {
                       
                        timelineCell.setCellStyle(styleGallery.get(nonTopBarStyleName) );
                    } else {
                        timelineCell.setCellStyle(styleGallery.get(normalStyleName));
                    }
                }
            }

        }


        try {
			workbook.write(outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true; 

		}


}







