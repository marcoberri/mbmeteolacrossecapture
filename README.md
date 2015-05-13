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

#Usage
	usage: java -jar <jar_name>
	Version:0.0.1-SNAPSHOT
	build: 2015-05-13 14:14
	-d,--post-dump              post data with command: [/usr/bin/te923con -d] to url: [http://meteo.marcoberri.it/data/addData] store backup post in [/home/pi/meteo/backup.log]
	-f,--post-from-file <arg>   post file to url: [http://meteo.marcoberri.it/data/addData] no store backup in file
	-h,--help                   this help
	-s,--post-line              post data with command: [/usr/bin/te923con] to url: [http://meteo.marcoberri.it/data/addData] store backup post in [/home/pi/meteo/backup.log]



