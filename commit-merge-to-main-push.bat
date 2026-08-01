@echo off
echo Switching to inProgress and committing...
git checkout inProgress
git add .
set /p msg="Commit message: "
git commit -m "%msg%"

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