# mbmeteolacrossecaputre
App in java che cattura i dati della LaCrosse WS1640 e posta ad un server remoto

#Target Application

[https://github.com/marcoberri/mbmeteolacrosse](https://github.com/marcoberri/mbmeteolacrosse)

#Install on raspbian

This app using [te923.fukz.org](http://te923.fukz.org/documentation.html)
 

	sudo apt-get install libusb-dev gcc
	sudo apt-get install libusb-1.0-0-dev
	cd /home/pi
	wget http://te923.fukz.org/downloads/te923tool-0.6.1.tgz
	tar xvzf te923tool*
	cd te923tool*
	sudo gcc -Wall -lusb -o te923con te923con.c te923usb.c te923com.c `pkg-config --libs libusb`
	sudo cp te923con /usr/bin

#Configuration

* maven --> mvn package
* src/main/resources/configuration.properties


