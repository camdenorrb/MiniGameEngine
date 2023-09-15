#!/usr/bin/bash

echo "Uploading MiniGames to MiniComputer..."
rsync ../examples/Spleef/build/libs/Spleef.jar camdenorrb@MiniComputer:Servers/12oClock/Hub1/plugins/MiniGameEngine/Updates/Spleef.jar

echo "Uploading MiniGame Engine to MiniComputer..."
rsync ../build/libs/MiniGameEngine.jar camdenorrb@MiniComputer:Servers/12oClock/Hub1/plugins/MiniGameEngine.jar