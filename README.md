# cse-110-project-team-16

> cse-110-project-team-16 created by GitHub Classroom

Members:
- Yalu Ouyang
- Peter Krause
- Bryan Duncan
- Connor Pitts
- Andrew Yip
- Guy Holman

Hello, this README.md contains the instructions for running the Recipe App. It covers everything from downloading the base Java files to the software and libraries needed to fully run the application. 

The GitHub repo for the project can be found at https://github.com/ucsd-cse110-fa23/cse-110-project-team-16.

## Step 1: Downloading the base code

Please go to our GitHub project repo. Once there, please go to the branch titled **MS2-Demo**. This contains all the base Java code for the program. Please download it to your machine (whilst **preserving the file structure**). You can either do Git clone or Zipped files (*unzip it*). We'll be referring to this as the **source code** from now on.

## Step 2: Getting the necessary software

Before we can actually run the program, several piece of software is required:

- JDK version 17 (or higher). Download here from [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Visual Studio Code (in theory Eclipse also works but VScode worked the best from our experiences). Download here from [Microsoft](https://code.visualstudio.com/download)
- JavaFX 21. Download [here](https://gluonhq.com/products/javafx/)

>Please get the versions that matches your machine's OS/platform

## Step 3: Setting up the software

After you downloaded JavaFX 21, unzip it and move the folder to a directory that you can easily find.

After successfully installing VSCode, please open it up and use it to open the folder with the **source code**. 

Select **Run and Debug** on the left-hand-side menu bar (or press `Ctrl + Shift + D`). Create a new launch file (`launch.json`) and write to it the following content:

```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [         
        {
            "type": "java",
            "name": "RecipeApp Main",
            "request": "launch",
            "mainClass": "Main",
            "projectName": "cse-110-project-team-16",
            "vmArgs": "--module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.fxml"
        }
    ]
}

```

Replace the `<path-to-javafx-lib>` with the path to the JavaFX `/lib` folder on your machine.

E.g. If you moved the JavaFX folder to somewhere like `C:/example/path`, then the path should be something like `C:/example/path/openjfx-21.0.1_windows-x64_bin-sdk/javafx-sdk-21.0.1/lib`

Please remember to put quotes `"` around that path.

## Step 4: Running the code

You should be all set right now. Go to the **Run and Debug** menu, change the running option to `RecipeApp Main`, and click the green arrow to run the Recipe App.