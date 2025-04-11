# StayMaster
Fast and smooth hotel room booking and management

Steps to setup and run the project :-

1. Install Java Development Kit (JDK)

* Ensure you have the Java Development Kit (JDK) installed (preferably version 8 or higher).
* Set the JAVA_HOME environment variable to point to your JDK installation directory.

2. Install Eclipse IDE

* Download and install Eclipse IDE for Java Developers from the official Eclipse website.
* Ensure that the IDE is configured for Java development.

3. Import the Project into Eclipse
1. Open Eclipse IDE.
2. Navigate to:
File > Import > Existing Projects into Workspace.
3. Select the CodeCrafters-main directory as the root directory.
4. Ensure the project appears in the list and click Finish.
4. Check the Build Path
   1. Right-click on the project in the Project Explorer.
   2. Select:
Build Path > Configure Build Path.
   3. Review the Libraries tab to ensure all necessary dependencies are included.
5. Add JARs and Libraries to the Build Path
1. Add the libs Folder
      * Right-click on the project in Eclipse and select:
Build Path > Configure Build Path.
      * Go to the Libraries tab and click Add JARs....
      * Navigate to the libs folder in the project directory, select all .jar files, and click OK.
2. Add the jars Folder
         *Repeat the same process as above for the jars directory:
         * Go to Libraries tab > Add JARs.
         * Select all .jar files in the jars directory and click OK.
3. Verify Selected JAR Files
         *Ensure that all selected JAR files are listed in the Libraries tab under Build Path.
         * Click Apply and Close to save changes.
6. Configure the Database (PostgreSQL)
This project uses the PostgreSQL database. Follow these steps:
         1. Ensure PostgreSQL is downloaded, installed, and running.
         2. Use pgAdmin 4 to connect to your PostgreSQL instance.
         3. Create a database named: stayease.
         4. Verify the database connection settings in the project's source code. Example connection string:
jdbc:postgresql://localhost:5432/stayease
         5. Test database connectivity to ensure everything is properly configured.
7. Configure JavaFX
Since JavaFX is used in this project, you need to ensure its integration into Eclipse:
Steps to Add JavaFX:
            1. Locate the JavaFX SDK in your system. You can download it from the official JavaFX website.
            2. Extract the SDK to a known directory on your system.
            3. In Eclipse, right-click on the project and select:
Build Path > Configure Build Path.
            4. Go to the Libraries tab and click Add Library > JavaFX.
            5. Point the configuration to the JavaFX SDK's lib directory. Example path:
/path/to/javafx-sdk/lib
            6. Ensure --module-path is set properly in the run configuration.
8. Run the Project
               1. Locate the main class in the src folder.
               2. Right-click on the main class and select:
Run As > Java Application.
               3. Alternatively, configure the run settings:
Navigate to: Run > Run Configurations.
Ensure the main class is specified, and the JavaFX module path is added under VM arguments if necessary.
Example VM arguments for JavaFX:
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
