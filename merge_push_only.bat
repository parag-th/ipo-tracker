@echo off
echo Switching to main...
git checkout main

echo Merging inProgress into main...
git merge inProgress

echo Pushing to origin...
git push

echo Switching back to inProgress...
git checkout inProgress

echo Done!
pause