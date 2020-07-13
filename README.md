# Alfresco LDTP
* [Introduction](#introduction)
* [Requirements](#requirements)
* [LDTP Documentation](#ldtp-documentation)
* [Getting Started](#getting-started)c
* [Usage](#usage)

## Introduction
We are pleased to introduce Alfresco LDTP library (IN Progress): a Java based automation suite that will enable GUI testing (Windows and MAC) of Alfresco desktop applications.

In our open source solution we used the following repositories:
* [Windows LDTP](https://github.com/ldtp/cobra)
* [Python Automated Testing on Mac](https://github.com/pyatom/pyatom)

## Requirements

For MAC you will need:
* OS X system with Xcode installed (tested on 10.9)
If you experience any issues, please feel free to open a ticket in the [issue tracker](https://github.com/Alfresco/Sync-ldtp/issues).
* please also follow the [Getting Started with Pyatom](https://github.com/pyatom/pyatom#getting-started) section from official page.

For Windows you will need:
* Windows XP/ Windows 7 SP1 / Winows 8 - as a minimum requirement.
* please follow the [Installation Instruction of Cobra LDTP](https://github.com/ldtp/cobra#download) from official page.

## LDTP Documentation

Please take a look on [LDTP User Manual](http://ldtp.freedesktop.org/user-doc/)

# Build
The `Sync-ldtp` project uses _Travis CI_. \
The `.travis.yml` config file can be found in the root of the repository.


#### Stages and Jobs
1. **Build**: Build project
2. **Deploy**: Deployment by publishing a snapshot to Nexus
3. **Release**: If required to make a release, add the following text to the commit message `[trigger release]`.
Releases are made only from master branch.

#### Branches
Travis CI builds differ by branch:
* `master` branch:
  - regular builds which in a _Build_ stages;
  - deploying the artifact on nexus with the version mentioned in `pom.xml` on the _Deploy_ stage.
  - if needed, a release version can be done by adding `[trigger release]` to the commit message.
* `feature/*` / `fix/*` branches:
  - regular builds which include only the _Build_ stage;

All other branches are ignored.
In order to trigger a new build for other branch name, update the working copy of `.travis.yml` with your branch name.

## Getting Started

* Checkout the code and install all dependencies:
```
$ git clone https://github.com/Alfresco/Sync-ldtp
$ mvn clean install -DskipTests 
```

#### Run MAC OS X related tests:
```cmd
$ mvn clean test -Dgroups=MacOnly -DexcludedGroups=Office
```

If you have Office 2011 installed on your MAC you can run the Office unit tests as well:
```cmd
$ mvn clean test -Dtest=org.alfresco.os.mac.app.office.v2011.Excel2011Test
```

### Usage

Please take a look on the unit tests already implemented for our classes.
Bellow you will find also a snipet of how to create a simple folder as a End User will do in Finder:
```java
import java.io.File;

import org.alfresco.os.mac.app.FinderExplorer;

public class TestFinder
{

    public static void main(String[] args)
    {
        FinderExplorer finder = new FinderExplorer();
        File testFolder = new File(finder.getApplicationPath(), "SampleFolder");

        finder.openApplication(); // this will open the Default /Documents folder of the current user

        // creating a new folder
        finder.createFolder(testFolder);

        // you can also specify other default startup path, or navigate to another folder
        finder.openFolder(testFolder);

        // go up one level
        finder.goToEnclosingFolder();

        // or delete the folder
        finder.deleteFolder(testFolder);
        
        finder.exitApplication();
    }
}
```
