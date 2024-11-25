# **SQL Executor**
version 1.0

---

## **Description**
The application operates the structure of a meta-script used for managing and executing SQL-based workflows.
This meta-script structure provides:
1. **Modularity**: Clear separation of sections for better maintainability.
2. **Reusability**: Parameterized snippets for dynamic query generation.
3. **Multi-Source Integration**: Ability to query multiple databases and connections.
4. **Post-Processing**: Advanced manipulation of query results with an internal local database (SQLite).

---

## **Script file structure**

Script file contains 3 essential parts:

- head section (mandatory)
- snippets section (mandatory if the sequences has snippet variables)
- sequence section (must be present at least 1)
- post sequence (1 per file, optional)

If any section is present in the file, all predefined variables inside are mandatory.

[Example sequence files](https://github.com/valdisdot/SQLExecutor/tree/main/example/scripts)

### **1. Head section**
The `## head` block includes metadata about the script, such as its name and key identifiers, which are used in UI and final result file name.

#### **Structure**
```
## head
## name: [Script Name]
## identifiers: [List of Identifiers]
## end
```

#### **Purpose**
- Describes the script and its context.
- Provides identifiers for categorization.

![Head Section GUI Example](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/head_section.png)

### **2. Snippets section**
The `## snippets` block defines reusable variables and conditions for parameterizing SQL queries.

#### **Structure**
```
## snippets
[Variable]: [Value]
[Condition]: [Value]
## end
```

### **Purpose**
- Declares variables to avoid hardcoding values.
- Simplifies query construction with pre-defined conditions.

![Snippets Section GUI Example](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/snippets_section.png)

### **3. Sequence Sections**
The `## sequence` blocks define SQL queries to be executed on specific database connections.

#### **Structure**
```
## sequence
## connection: [Connection Name]
## database: [Database Name]
## result-table: [Table Name]
[SQL Query]
## end
```

#### **Purpose**
- Executes queries on specified databases and connections.
- Temporarily stores query results in `result-table`s for further processing.

![Sequence Section GUI Example](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/sequence_section.png)

### **4. Post-Sequence Section**
The `## post-sequence` block aggregates and manipulates results from previous `## sequence` sections with internal temporary SQLite database.

#### **Structure**
```
## post-sequence
## result-table: [Result Table Name]
[SQLite Query for futher post-processing]
## end
```

#### **Purpose**
- Combines intermediate data from `result-table`s.
- Performs calculations, aggregations, and final formatting of results etc.

![Post-Sequence Section GUI Example](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/post_sequence_section.png)

---

## **Application & Database configuration**

[Example of the application.json](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/config/application.json)

[Example of the connection,json](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/config/connection.json)

---

## **Starting the application**

### **Parameters of the Start.main()**

The application accepts the following command-line arguments to control its behavior:

- `mode` (optional) - specifies the mode in which the application will run.
- - `cli` - launches the application in Command-Line interface (CLI) mode
- - `any other or absent` - launches the application in Graphical User interface (GUI) mode
- `configFolder` - specifies the folder where the application's configuration files are located, if not provided, the application will use the `config` folder in the application's root directory.

### **Quick start**

CLI-mode with custom `config` directory:
````
java -jar sqlexecutor.jar mode=cli configFolder=home/user/configs/sqlexecutor
````
GUI-mode with custom `config` directory:
````
java -jar sqlexecutor.jar configFolder=home/user/configs/sqlexecutor
````
GUI-mode a relative to the jar-file `config` directory:
````
java -jar sqlexecutor.jar
````
Run CLI-example:
````
java -cp sqlexecutor.jar example.CLIExample
````
Run GUI-example:
````
java -cp sqlexecutor.jar example.GUIExample
````
---

## **GUI Overview**

The GUI organizes and visualizes the components of the script in the menu panel (left sidebar) and editor panel, making it easier for users to manage, modify, and execute SQL queries and their associated tasks.

![GUI Example](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/gui_overview.png)

### **1. Menu**
The left sidebar lists all available scripts, categorized by their type and tags. This allows users to quickly identify and select different sections of the workflow.

**Features:**

- **Script Names**: Labeled with descriptive names, such as "Card Account Outcome Trend," "Deposit Account Saldo."
- **Tags**: Each script is color-coded with tags to indicate its purpose, such as `report`, `saldo`, `usage statistic`.
- **Open Button**: Allows users to open a specific script for further editing or execution.

![Menu Item](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/menu_item.png)

### 2. **Editor**
This section provides detailed information and editing options for the selected script. It includes various fields for user input, such as the query body, connection settings, and result table.

**Features:**

- **File Path**: Displays the full file path of the selected script (`D:\user\scripts\my_script.txt`).
- **Snippets (Variables)**: Allows users to define reusable variables (e.g., `deposit_id` and `dt`) that can be used within the queries.
- **Query Body**: Displays the SQL query for the first sequence in the script. Users can edit this query as needed.
- **Post-query**: The section where users can define post-query operations that manipulate the results of the previous query.
- **Final Result Table**: Displays the final result table, which in this case is `client card transactions`.

![Editor Panel](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/editor.png)

### 3. **Control panel**
This area allows users to reset, save, preview or execute the selected script. It includes buttons for:
- **Preview**: Check the query and operations before final execution.
- **Execute**: Run the script on the specified connection and database, generating the results.
- **Reset**: Revert any changes made to the script.
- **Save**: Save the script into its source file.

![Control panel](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/control_panel.png)

---

## CLI Overview

The application also provides the command line interface (CLI) mode for interacting with the SQLExecutor. The CLI mode allows users to execute SQL queries and manage reports directly through the terminal.

### 1. **Initialization**
When the application is launched in CLI mode, it initializes and lists all available sequence holders (script templates) that can be executed. Each sequence is assigned a unique number, which can be used to trigger the respective script.

![ClI start](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/cli_start.png)

### 2. **Executing a Sequence**
To execute a script, simply enter the corresponding sequence number (e.g., `3` for "Card Account Statement (detailed)"). The application will process the request and generate the results.

![ClI start](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/cli_executing.png)


### 3. **Menu Access**
Users can type `menu` to list all available sequence holders again, allowing them to select another script or operation.

![ClI start](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/cli_menu.png)


### 4. **Exit the Application**
To exit the application, type `exit` and the application will close.

![ClI start](https://github.com/valdisdot/SQLExecutor/blob/main/example/images/cli_exit.png)









