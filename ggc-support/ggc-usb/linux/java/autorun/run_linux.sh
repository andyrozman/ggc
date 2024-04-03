#! /bin/sh
# try to cd to the correct directory wherever we're called from and ignore any failures
cd "$(dirname "${0}")/../.." || true

cd bin
sh run_linux.sh
# This won't have any effect - in Linux, environment changes inside a script don't affect the outside (shell, calling scrip, ...) environment.
# This will only do anything if the script is being sourced, and will end up one level above the entry point - is that what you want?
cd ../..
