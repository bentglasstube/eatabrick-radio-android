#!/bin/sh

# make launcher icons

convert art/icon.png -scale 36x36 res/drawable-ldpi/ic_launcher.png
convert art/icon.png -scale 48x48 res/drawable-mdpi/ic_launcher.png
convert art/icon.png -scale 72x72 res/drawable-hdpi/ic_launcher.png
convert art/icon.png -scale 96x96 res/drawable-xhdpi/ic_launcher.png

# make notification icons

make_white="+level 100%,100%"
make_grey="+level 54%,54%"
reframe="-gravity center -background transparent -extent"

convert art/notification.png $make_white -scale 18x18 res/drawable-ldpi-v11/ic_notification.png
convert art/notification.png $make_white -scale 24x24 res/drawable-mdpi-v11/ic_notification.png
convert art/notification.png $make_white -scale 36x36 res/drawable-hdpi-v11/ic_notification.png
convert art/notification.png $make_white -scale 48x48 res/drawable-xhdpi-v11/ic_notification.png
convert art/notification.png $make_grey -scale 12x12 $reframe 12x19 res/drawable-ldpi/ic_notification.png
convert art/notification.png $make_grey -scale 16x16 $reframe 16x25 res/drawable-mdpi/ic_notification.png
convert art/notification.png $make_grey -scale 24x24 $reframe 24x38 res/drawable-hdpi/ic_notification.png
convert art/notification.png $make_grey -scale 32x32 $reframe 32x50 res/drawable-xhdpi/ic_notification.png

# make action bar icons

convert art/play.png -scale 18x18 res/drawable-ldpi/ic_menu_play.png
convert art/play.png -scale 24x24 res/drawable-mdpi/ic_menu_play.png
convert art/play.png -scale 36x36 res/drawable-hdpi/ic_menu_play.png
convert art/play.png -scale 48x48 res/drawable-xhdpi/ic_menu_play.png
convert art/pause.png -scale 18x18 res/drawable-ldpi/ic_menu_pause.png
convert art/pause.png -scale 24x24 res/drawable-mdpi/ic_menu_pause.png
convert art/pause.png -scale 36x36 res/drawable-hdpi/ic_menu_pause.png
convert art/pause.png -scale 48x48 res/drawable-xhdpi/ic_menu_pause.png
convert art/skip.png -scale 18x18 res/drawable-ldpi/ic_menu_skip.png
convert art/skip.png -scale 24x24 res/drawable-mdpi/ic_menu_skip.png
convert art/skip.png -scale 36x36 res/drawable-hdpi/ic_menu_skip.png
convert art/skip.png -scale 48x48 res/drawable-xhdpi/ic_menu_skip.png
