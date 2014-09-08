MouseStudio
===========

This repository contains code concerning issues of recording mouse paths in between two clicks and playing them back.

## Recording:
- system hook recording all mouse movements (see mouse logger)
- saving as csv file

## Plaback
- playback in recorded time (adjustable though)
- possibility to map mouse path to different locations
- acceleration
- easy handling of giant amount of recorded paths
- checking for validity (distance of start and endpoint -> note to self: add time limit, too)
- distance between start and end
- distance traveled

# Dependencies
- https://github.com/kwhat/jnativehook
- processing 2.2.1
- toxiclibs
