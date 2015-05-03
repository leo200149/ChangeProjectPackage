# ChangeProjectPackage
This is tool for change java project package.
This tool just for education use.

    Tool's core is use java.io API.
    Tool's UI is use JavaFX2(Java8).
    Tool use log4j to save log file.

##UI setting as below:

<img src="https://lh6.googleusercontent.com/-456_xns1tWw/VUXJCJabGJI/AAAAAAAADcg/vsCfzJaX5Gc/w1062-h1080-no/changePackageUI.jpg" width="500" height="400">

    1. Project: Please select your project file.
    2. Add:  Please select your java code source folder(can add mulitple folder)
    3. Old Package: Please input the package name that you want to change. e.g. hyc.edu.emotion
    4. New Package: Please input the new package name. e.g. com.hyc.emotion 
    5. Start Replace: After click,tool will begin to modify all old package file in the project,and move to new package. 
    6. Log monitor: It will show all modify and move file list.
    7. Log file: It will create at the runnable jar's same path.
      
#Please be careful for your operation
    1.Suggest to backup your project before use this tool.
    2.Improper operation,this tool have the opportunity to destroy your project structure.

##Known Defect as below:
    1.Old package name cannot just input unit word. e.g. Old Package: resource
      This tool cannot discern the unit word is attribute or package name.
    2.if project is Android app project, after change package finish, please check the AndroidManifest.xml.
      This tool will not replace the relative path in the file.
