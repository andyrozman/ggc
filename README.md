![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/andyrozman/ggc/maven.yml)
[![Crowdin](https://badges.crowdin.net/gnu-gluco-control-core/localized.svg)](https://crowdin.com/project/gnu-gluco-control-core)
[![License: GPL v2](https://img.shields.io/badge/License-GPL_v2-blue.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)
![Known Vulnerabilities](https://snyk.io/test/github/andyrozman/ggc/badge.svg)

# GNU Gluco Control - ggc
This project was hosted on sourceforge for a long time, but since github has much option that can help with release management and 
user/customer management and support I decided to move project here (project was moved in begining of April)... Everything on sourceforge will stay as it, with all repositories 
set to read-only, and all old discussions and history still available there (https://sourceforge.net/p/ggc/), but all the code and releases
will be created from here.


## Old Webpage
At the moment code in this repository is not working (in middle od restructuring) yet. You can get old version through old webpage: https://ggc.sourceforge.net/ for now. Last working version is 0.7.1 


## Discussions
I enabled discussions here, which should be used, if you have any questions.

## Issues
If you find any issues or have feature requests, please use "Issues" functionality.

## Build
To build this project you will need to have certain prerequisties:
- download Java 8 or later (Amazon Coretto 8 is updated quite regularly with bug fix solutions)
- download Maven 3.6.3 or later, but needs to be lower than 3.8.x

To build project you need checkout this repository with --recursive flag, which will also download submodule atech-tools.
```
git clone git@github.com:andyrozman/ggc.git --recursive
```

Afterwards doing mvn build will be enough:
```
mvn clean install
```

You can run application by calling this (NOT WORKING YET):
```shell
java -jar ggc-desktop-app/ggc-desktop/target/ggc-desktop-0.8.0-jar-with-dependencies.jar
```


## How can you help with development?
Development in GGC is mostly done in this project, but some of base functionalities are implemented also in atech-tools (submodule). If you do changes on submodule, don't forget to check thoose changes in (separately).
Please fork your project and create relevant branch, after you are done open Pull Request.


## How can you use application?
I am in middle of big architectural change for GGC, so until this problem is solved, you will need one of old packages (available on sourceforge.net) 
until we get application running new way.

## Translations (crowdin)
Application is now being translated through Crowdin translation platform, so if you are interested here is invite and you can add your own language to the list. Once your language has reached 20% of main application translated I will add it to the application. If you wish to contrinute, please click on translation badge or use this link:
https://crowdin.com/project/gnu-gluco-control-core

## Documentation
At the moment documentation is under different sub projects in docs/doc folder. Main folder for documentation is under ggc-desktop-app/ggc-desktop/doc 