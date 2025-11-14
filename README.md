

how to setup git?
open intellij idea on ubuntu
login to your github account

open terminal (Alt + F12)
type the following lines separately (one after the another) and hit enter
sudo apt update
sudo apt upgrade
sudo apt install git

open intellij idea and click "cone repository"
put this url and clone.
https://github.com/BihanDoo/Medicare.git




when adding new classes/files, be sure to click "add" in the "add file to git" dialog.

how to push? (update the project with your changes)
go to the commit page (Alt+0)
tick all the changes, type what you've changed in the commit message(should be meaningful), select "commit and push"
review changes and click "push"
*****make sure you are pushing to the origin:master, otherwise you are syncing to your fork for yourself. (it is fine if you know what you are doing, thats the professional way. but for now, we are syncing to the main branch- origin:master)
