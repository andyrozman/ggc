![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/andyrozman/ggc/maven.yml)

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
To build project you need to checkout this repository with --recursive flag, which will also download submodule atech-tools. 

## How can you help with development?
Development in GGC is mostly done in this project, but some of base functionalities are implemented also in atech-tools (submodule). If you do changes on submodule, don't forget to check thoose changes in (separately)/

As by default downloading the project and running "mvn clean install" should by default get you running. Required version of Java is 1.8+ (I 
recomend using latest amazon-correto-8) and recommended Maven 3.5.2 (you should use version less than 3.8.0 or you might have problem building).

## How can you use application?
I am in middle of big architectural change for GGC, so until this problem is solved, you will need one of old packages (available on sourceforge.net) 
until we get application running new way.

