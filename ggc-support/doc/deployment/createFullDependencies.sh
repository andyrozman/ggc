#!/bin/sh

export GGC_VERSION=0.6
export GGC_DEPLOY_TARGET=/mnt/d/GGC/deploy
export GGC_ROOT=/home/andy/workspaces/andy-iplayground4/ggc




echo Prepare target directory

cd $GGC_DEPLOY_TARGET

rm -Rf $GGC_VERSION

mkdir $GGC_VERSION
cd $GGC_VERSION

echo Copy dependencies
mkdir dependencies
cd dependencies

echo ... GGC Core
cp $GGC_ROOT/ggc-core/target/dependency/*.* .

echo ... GGC Plugin Base
cp $GGC_ROOT/ggc-plugin_base/target/dependency/*.* .

echo ... GGC Nutrition
cp $GGC_ROOT/ggc-nutri/target/dependency/*.* .

echo ... GGC Help
cp $GGC_ROOT/ggc-help/target/dependency/*.* .

echo ... GGC Meter Plugin
cp $GGC_ROOT/ggc-meter/target/dependency/*.* .

echo ... GGC Pump Plugin
cp $GGC_ROOT/ggc-pump/target/dependency/*.* .

echo ... GGC CGMS Plugin
cp $GGC_ROOT/ggc-cgm/target/dependency/*.* .

echo ... GGC Desktop
cp $GGC_ROOT/ggc-desktop/target/dependency/*.* .


cd ..


echo Create repository

mkdir repository
cd repository

echo ... GGC Core
cp -R $GGC_ROOT/ggc-core/target/dependency/* .

echo ... GGC Plugin Base
cp -R $GGC_ROOT/ggc-plugin_base/target/dependency/* .

echo ... GGC Nutrition
cp -R $GGC_ROOT/ggc-nutri/target/dependency/* .

echo ... GGC Help
cp -R $GGC_ROOT/ggc-help/target/dependency/* .

echo ... GGC Meter Plugin
cp -R $GGC_ROOT/ggc-meter/target/dependency/* .

echo ... GGC Pump Plugin
cp -R $GGC_ROOT/ggc-pump/target/dependency/* .

echo ... GGC CGMS Plugin
cp -R $GGC_ROOT/ggc-cgm/target/dependency/* .

echo ... GGC Desktop
cp -R $GGC_ROOT/ggc-desktop/target/dependency/* .



cd ..




echo Copy documentation - this is done dinamically and sometime statically, check if you added any new docs

mkdir documentation
cd documentation

echo ... GGC Desktop
cp -R $GGC_ROOT/ggc-desktop/doc/* .

echo ... GGC Plugin Base
mkdir tools-base
cp $GGC_ROOT/ggc-plugin_base/docs/* ./tools-base/.

echo ... GGC Nutrition
mkdir plugin-nutrition
cp $GGC_ROOT/ggc-nutri/docs/* ./plugin-nutrition/.

echo ... GGC Help
mkdir help
cp $GGC_ROOT/ggc-help/docs/* ./help/.

echo ... GGC Meter Plugin
mkdir plugin-meter
cp $GGC_ROOT/ggc-meter/docs/* ./plugin-meter/.

echo ... GGC Pump Plugin
mkdir plugin-pump
cp $GGC_ROOT/ggc-pump/docs/* ./plugin-pump/.


echo ... GGC CGMS Plugin
mkdir plugin-cgms
cp $GGC_ROOT/ggc-cgm/doc/* ./plugin-cgms/.

echo ... GGC Core
mkdir core
cp $GGC_ROOT/ggc-core/docs/* ./core/.

echo All preparation for GGC deployment finished.

