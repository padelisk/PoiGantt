# PoiGantt

A Java application that reads a project's tasks from a file and creates a **Gantt chart in Excel**.

Team project (3 students) for the *Software Development* course, University of Ioannina, 2024.

## What it does

- Loads tasks from TSV, XLS or XLSX files
- Sorts tasks and calculates the dates, cost and effort of tasks that are made of subtasks
- Filters tasks: all tasks, top-level tasks only, or tasks in an ID range
- Exports formatted Gantt charts to Excel, with custom styles

## Built with

Java 8 · Apache POI · JUnit 4 · Eclipse

## How to run

1. Import the project into Eclipse (*File → Import → Existing Projects into Workspace*).
2. Download [Apache POI 5.2.3](https://archive.apache.org/dist/poi/release/bin/) and put the jars in the `lib` folder.
3. Run `app.naive.NaiveClient`.

Example input and output files are in `src/test/resources/`.
